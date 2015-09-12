package ch03.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch03.data.TypedValue;
import ch03.engine.context.MapContext;
import ch03.engine.operation.StartActivity;
import ch03.engine.state.Created;
import ch03.engine.state.Ended;
import ch03.engine.state.ExecutionState;
import ch03.engine.state.Starting;
import ch03.engine.state.WaitingForMessage;
import ch03.model.Activity;
import ch03.model.ActivityInstance;
import ch03.model.Scope;
import ch03.model.ScopeInstance;
import ch03.model.Transition;
import ch03.model.Workflow;
import ch03.model.WorkflowInstance;
import ch03.util.Logger;
import ch03.util.LoggerFactory;


/**
 * @author Tom Baeyens
 */
public class ControllerImpl implements Controller {
  
  private static final Logger log = Engine.log;
  
  protected Engine engine;
  protected ContextImpl context;
  protected EngineListener engineListener;

  public WorkflowInstance startWorkfowInstance(Workflow workflow, Map<String, TypedValue> startData, List<Activity> startActivities) {
    WorkflowInstance workflowInstance = engine.instantiateWorkflowInstance();
    workflowInstance.setEngine(engine);
    workflowInstance.setWorkflow(workflow);
    workflowInstance.setScope(workflow);
    workflowInstance.setState(new Starting());
    engineListener.transactionStartWorkflowInstance(workflowInstance);
    log.debug("Created workflow instance %s", workflowInstance);
    engine.setScopeInstance(workflowInstance);
    engine.enterScope();
    applyStartData(workflow, workflowInstance, startData);
    if (startActivities==null) {
      startActivities = workflow.getStartActivities();
    }
    startActivityInstances(startActivities);
    return workflowInstance;
  }

  public void applyStartData(Workflow workflow, WorkflowInstance workflowInstance, Map<String, TypedValue> startData) {
    if (startData!=null && !startData.isEmpty() && workflow.getInputParameters()!=null) {
      ContextImpl context = engine.getContext();
      MapContext startDataContext = new MapContext("startData", startData);
      // adding the start data subcontext after the subcontext context
      context.addSubContext(0, startDataContext);
      Map<String, TypedValue> inputs = context.readInputs();
      context.removeSubContext(startDataContext);
      for (String inputKey: inputs.keySet()) {
        TypedValue inputValue = inputs.get(inputKey);
        context.setVariableInstanceValue(inputKey, inputValue);
      }
    }
  }

  /** starts the given scope */
  @Override
  public ActivityInstance startActivityInstance(Activity activity) {
    return startActivityInstance(activity, engine.scopeInstance);
  }

  /** starts the given scope */
  @Override
  public ActivityInstance startActivityInstance(Activity activity, ScopeInstance parentScopeInstance) {
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
    String format = parentScopeInstance.isActivityInstance() 
            ? "Create activity instance %s inside activity instance %s"
            : "Create activity instance %s";
    log.debug(format, activityInstance, parentScopeInstance);
    
    engine.perform(new StartActivity(activityInstance));
    return activityInstance;
  }

  @Override
  public List<ActivityInstance> startActivityInstances(List<Activity> activities) {
    List<ActivityInstance> activityInstances = new ArrayList<>();
    if (!activities.isEmpty()) {
      // for each nested activity 
      for (Activity activity: activities) {
        startActivityInstance(activity);
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
      log.debug("Taking transition to %s", to);
      return startActivityInstance(to, engine.getScopeInstance().getParent());
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
    ScopeInstance scopeInstance = engine.getScopeInstance();
    if (scopeInstance.isActivityInstance()) {
      ScopeInstance parentInstance = scopeInstance.getParent();
      Scope parentScope = parentInstance.getScope();
      engine.setScopeInstance(parentInstance);
      parentScope.flowEnded(parentInstance, (ActivityInstance)scopeInstance, engine.getContext(), engine.getController());
    } else {
      // TODO check if there's another activity instance waiting for this one to finish.
    }
  }

  public void setState(ExecutionState state) {
    ScopeInstance scopeInstance = engine.getScopeInstance();
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
        log.debug("Ending activity instance %s", scopeInstance);
        engineListener.activityInstanceEnded((ActivityInstance)scopeInstance);
      } else {
        log.debug("Ending workflow instance %s", scopeInstance);
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
