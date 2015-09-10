package ch03.engine;

import java.util.ArrayList;
import java.util.List;

import ch03.engine.operation.FlowEnded;
import ch03.engine.operation.StartActivity;
import ch03.engine.state.Created;
import ch03.engine.state.Ended;
import ch03.engine.state.ExecutionState;
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
public class ExecutionControllerImpl implements ExecutionController {
  
  Execution execution;
  ExecutionContextImpl context;
  ExecutionListener listener;

  public ExecutionControllerImpl(Execution execution) {
    this.execution = execution;
    this.context = execution.executionContext;
    this.listener = execution.listener;
  }

  /** starts the given scope */
  @Override
  public ActivityInstance startActivity(Activity activity) {
    return startActivity(activity, execution.scopeInstance);
  }

  /** starts the given scope */
  @Override
  public ActivityInstance startActivity(Activity activity, ScopeInstance parentScopeInstance) {
    if (activity==null || !context.isConditionMet(activity.condition)) {
      return null;
    }
    ActivityInstance activityInstance = execution.instantiateActivityInstance();
    activityInstance.setScope(activity);
    activityInstance.setActivity(activity);
    activityInstance.setState(new Created());
    activityInstance.setParent(parentScopeInstance);
    parentScopeInstance.getActivityInstances().add(activityInstance);
    activityInstance.setId(listener.generateActivityInstanceId(activityInstance));

    listener.activityInstanceCreated(activityInstance);
    
    execution.perform(new StartActivity(activityInstance));
    
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
    ScopeInstance scopeInstance = execution.scopeInstance;
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
    execution.addOperation(new FlowEnded((ActivityInstance)execution.scopeInstance));
  }

  public void setState(ExecutionState state) {
    ScopeInstance scopeInstance = execution.scopeInstance;
    ExecutionState oldState = scopeInstance.getState(); 
    scopeInstance.setState(state);
    if (scopeInstance instanceof ActivityInstance) {
      listener.activityInstanceStateUpdate((ActivityInstance)scopeInstance, oldState);
    } else {
      listener.workflowInstanceStateUpdate((WorkflowInstance)scopeInstance, oldState);
    }
  }
  
  public void endScopeInstance() {
    ScopeInstance scopeInstance = execution.scopeInstance;
    if (!scopeInstance.isEnded()) {
      scopeInstance.setState(new Ended());
      if (scopeInstance instanceof ActivityInstance) {
        listener.activityInstanceEnded((ActivityInstance)scopeInstance);
      } else {
        listener.workflowInstanceEnded((WorkflowInstance)scopeInstance);
      }
    }
  }
}
