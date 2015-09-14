package ch03.engine;

import java.util.ArrayList;
import java.util.List;

import ch03.engine.operation.StartActivity;
import ch03.engine.state.Created;
import ch03.engine.state.Ended;
import ch03.engine.state.ExecutionState;
import ch03.engine.state.WaitingForMessage;
import ch03.model.Activity;
import ch03.model.ActivityInstance;
import ch03.model.Scope;
import ch03.model.ScopeInstance;
import ch03.model.Transition;
import ch03.model.Workflow;
import ch03.model.WorkflowInstance;
import ch03.util.Logger;


/**
 * @author Tom Baeyens
 */
public class ControllerImpl implements Controller {
  
  private static final Logger log = EngineImpl.log;
  
  protected EngineImpl engine;
  protected ContextImpl context;
  protected Persistence persistence;


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

    persistence.activityInstanceCreated(activityInstance);
    String format = parentScopeInstance.isActivityInstance() 
            ? "Create activity instance %s inside activity instance %s"
            : "Create activity instance %s";
    log.debug(format, activityInstance, parentScopeInstance);
    
    engine.addOperation(new StartActivity(activityInstance));
    return activityInstance;
  }

  @Override
  public List<ActivityInstance> startActivityInstances(List<Activity> activities) {
    List<ActivityInstance> activityInstances = new ArrayList<>();
    if (!activities.isEmpty()) {
      // for each nested activity 
      for (Activity activity: activities) {
        ActivityInstance activityInstance = startActivityInstance(activity);
        activityInstances.add(activityInstance);
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

  /** adds a listener to be notified when the execution is done and 
   * persisted.  This listening is needed for notifications of external 
   * activity instances.  If external systems were already notified in the 
   * activity execution, their reaction could come back to the workflow engine 
   * before the current state is persisted.
   */
  @Override
  public void addExecutionListener(ExecutionListener executionListener) {
    engine.addExecutionListener(executionListener);
  }

  /** puts the current execution flow on hold in the current activity instance till 
   * an external service signals completion */
  @Override
  public void waitForExternalMessage() {
    log.debug("Waiting for external message "+engine.getScopeInstance());
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
      persistence.activityInstanceStateUpdate((ActivityInstance)scopeInstance, oldState);
    } else {
      persistence.workflowInstanceStateUpdate((WorkflowInstance)scopeInstance, oldState);
    }
  }
  
  public void endScopeInstance() {
    ScopeInstance scopeInstance = engine.scopeInstance;
    if (!scopeInstance.isEnded()) {
      scopeInstance.setState(new Ended());
      if (scopeInstance.isActivityInstance()) {
        log.debug("Ending activity instance %s", scopeInstance);
        persistence.activityInstanceEnded((ActivityInstance)scopeInstance);
      } else {
        log.debug("Ending workflow instance %s", scopeInstance);
        persistence.workflowInstanceEnded((WorkflowInstance)scopeInstance);
      }
    }
  }

  
  public EngineImpl getEngine() {
    return engine;
  }

  
  public void setEngine(EngineImpl engine) {
    this.engine = engine;
  }

  
  public ContextImpl getContext() {
    return context;
  }

  
  public void setContext(ContextImpl context) {
    this.context = context;
  }

  
  public Persistence getPersistence() {
    return persistence;
  }

  
  public void setPersistence(Persistence persistence) {
    this.persistence = persistence;
  }
}
