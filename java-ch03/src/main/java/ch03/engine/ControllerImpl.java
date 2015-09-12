package ch03.engine;

import java.util.ArrayList;
import java.util.List;

import ch03.engine.operation.FlowEnded;
import ch03.engine.operation.StartActivity;
import ch03.engine.operation.StartWorkflow;
import ch03.engine.state.Created;
import ch03.engine.state.Ended;
import ch03.engine.state.ExecutionState;
import ch03.engine.state.Starting;
import ch03.engine.state.WaitingForMessage;
import ch03.model.Activity;
import ch03.model.ActivityInstance;
import ch03.model.ScopeInstance;
import ch03.model.Transition;
import ch03.model.Workflow;
import ch03.model.WorkflowInstance;


/**
 * @author Tom Baeyens
 */
public class ControllerImpl implements Controller {
  
  protected Engine engine;
  protected ContextImpl context;
  protected EngineListener engineListener;

  public WorkflowInstance createWorkfowInstance(Workflow workflow) {
    WorkflowInstance workflowInstance = engine.instantiateWorkflowInstance();
    workflowInstance.setEngine(engine);
    workflowInstance.setWorkflow(workflow);
    workflowInstance.setScope(workflow);
    workflowInstance.setState(new Starting());
    engineListener.transactionStartWorkflowInstance(workflowInstance);
    engine.setScopeInstance(workflowInstance);
    engine.enterScope();
    return workflowInstance;
  }

  public void startActivities(WorkflowInstance workflowInstance, List<Activity> startActivities) {
    engine.perform(new StartWorkflow(workflowInstance, startActivities));
  }

  /** starts the given scope */
  @Override
  public ActivityInstance startActivity(Activity activity) {
    return startActivity(activity, engine.scopeInstance);
  }

  /** starts the given scope */
  @Override
  public ActivityInstance startActivity(Activity activity, ScopeInstance parentScopeInstance) {
    if (activity==null || !context.isConditionMet(activity.condition)) {
      return null;
    }
    ActivityInstance activityInstance = engine.instantiateActivityInstance();
    activityInstance.setScope(activity);
    activityInstance.setActivity(activity);
    activityInstance.setState(new Created());
    activityInstance.setParent(parentScopeInstance);
    parentScopeInstance.getActivityInstances().add(activityInstance);

    engineListener.activityInstanceCreated(activityInstance);
    
    engine.perform(new StartActivity(activityInstance));
    
    return activityInstance;
  }

  @Override
  public List<ActivityInstance> startActivities(List<Activity> activities) {
    List<ActivityInstance> activityInstances = new ArrayList<>();
    if (!activities.isEmpty()) {
      // for each nested activity 
      for (Activity activity: activities) {
        startActivity(activity);
      }
    }
    return activityInstances;
  }
  
  public List<Transition> findTransitionsMeetingCondition(List<Transition> transitions) {
    List<Transition> transitionsMeetingCondition = new ArrayList<>();
    if (transitions!=null) {
      for (Transition transition : transitions) {
        if (context.isConditionMet(transition.condition)) {
          transitionsMeetingCondition.add(transition);
        }
      }
    }
    return transitions;
  }

  /** starts activities for the transition destination activities if the 
   * conditions in the transition and the destination activities are met. 
   * If no new activity instances are created, the parent is notified that the 
   * execution flow ended. */
  @Override
  public List<ActivityInstance> takeTransitions(List<Transition> transitions) {
    endScopeInstance();
    List<ActivityInstance> activityInstances = new ArrayList<>();
    List<Transition> transitionsMeetingCondition = findTransitionsMeetingCondition(transitions);
    for (Transition transitionMeetingCondition: transitionsMeetingCondition) {
      ActivityInstance activityInstance = takeTransitionWithoutEndingScopeInstance(transitionMeetingCondition);
      if (activityInstance!=null) {
        activityInstances.add(activityInstance);
      }
    }
    if (activityInstances.isEmpty()) {
      notifyParentFlowEnded();
    }
    return activityInstances;
  }

  /** starts the transition destination if the 
   * condition in the transition and the destination activity are met. 
   * If no new activity instance is created, the parent is notified that the 
   * execution flow ended. */
  @Override
  public ActivityInstance takeTransition(Transition transition) {
    endScopeInstance();
    ActivityInstance activityInstance = takeTransitionWithoutEndingScopeInstance(transition);
    if (activityInstance==null) {
      notifyParentFlowEnded();
    }
    return activityInstance;
  }

  public ActivityInstance takeTransitionWithoutEndingScopeInstance(Transition transition) {
    Activity to = transition!=null ? transition.to : null;
    if (to!=null) {
      return startActivity(to);
    }
    return null;
  }

  /** puts the current execution flow on hold in the current activity instance till 
   * an external service signals completion */
  @Override
  public void waitForExternalMessage() {
    setState(new WaitingForMessage()); 
  }

  /** ends the current scope instance and propagates 
   * the execution flow forward if there is more to be done.
   * 
   * If nested activity instances are still open, they are closed.
   * 
   * The current scope is either an activity or a workflow.
   * When called on an activity, first outgoing transitions are considered.
   * If there are 'active' transitions, they are taken.   
   * 
   * or, in case there are no active 
   * transitions, notifies the parent that the flow ended.  
   * Active transitions are 
   * outgoing transitions that meeting following criteria:
   * - condition is met (or no condition)
   * - transition has a destination activity.
   * If there are no active transitions, the execution flow 
   * propagates to the parent. */
  @Override
  public void onwards() {
    endScopeInstance();
    ScopeInstance scopeInstance = engine.scopeInstance;
    if (scopeInstance instanceof ActivityInstance) {
      ActivityInstance activityInstance = (ActivityInstance) scopeInstance;
      Activity activity = (Activity) scopeInstance.getScope();
      activity.onwards(activityInstance, context, this);
    } else {
      WorkflowInstance workflowInstance = (WorkflowInstance) scopeInstance;
      Workflow workflow = (Workflow) scopeInstance.getScope();
      workflow.onwards(workflowInstance, context, this);
    }
  }

  @Override
  public void notifyParentFlowEnded() {
    engine.addOperation(new FlowEnded((ActivityInstance)engine.scopeInstance));
  }

  public void setState(ExecutionState state) {
    ScopeInstance scopeInstance = engine.scopeInstance;
    ExecutionState oldState = scopeInstance.getState(); 
    scopeInstance.setState(state);
    if (scopeInstance.isActivityInstance()) {
      engineListener.activityInstanceStateUpdate((ActivityInstance)scopeInstance, oldState);
    } else {
      engineListener.workflowInstanceStateUpdate((WorkflowInstance)scopeInstance, oldState);
    }
  }
  
  public void endScopeInstance() {
    ScopeInstance scopeInstance = engine.scopeInstance;
    if (!scopeInstance.isEnded()) {
      scopeInstance.setState(new Ended());
      if (scopeInstance.isActivityInstance()) {
        engineListener.activityInstanceEnded((ActivityInstance)scopeInstance);
      } else {
        engineListener.workflowInstanceEnded((WorkflowInstance)scopeInstance);
      }
    }
  }

  
  public Engine getEngine() {
    return engine;
  }

  
  public void setEngine(Engine engine) {
    this.engine = engine;
  }

  
  public ContextImpl getContext() {
    return context;
  }

  
  public void setContext(ContextImpl context) {
    this.context = context;
  }

  
  public EngineListener getEngineListener() {
    return engineListener;
  }

  
  public void setEngineListener(EngineListener engineListener) {
    this.engineListener = engineListener;
  }
}
