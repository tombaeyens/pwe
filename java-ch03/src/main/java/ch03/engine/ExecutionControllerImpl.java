package ch03.engine;

import java.util.List;

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
  WorkflowInstancePersistence persistence;

  public ExecutionControllerImpl(Execution execution) {
    this.execution = execution;
    this.context = execution.executionContext;
    this.persistence = execution.persistence;
  }

  /** starts the given scope */
  @Override
  public void startActivity(Activity activity) {
    startActivity(activity, execution.scopeInstance);
  }

  /** starts the given scope */
  @Override
  public void startActivity(Activity activity, ScopeInstance parentScopeInstance) {
    if (activity==null || !context.isConditionMet(activity.condition)) {
      return;
    }
    ActivityInstance activityInstance = execution.instantiateActivityInstance();
    activityInstance.scope = activity;
    activityInstance.activity = activity;
    activityInstance.state = new Created();
    activityInstance.parent = parentScopeInstance;
    parentScopeInstance.activityInstances.add(activityInstance);
    activityInstance.id = persistence.generateActivityInstanceId(activityInstance);

    persistence.activityInstanceCreated(activityInstance);
    
    execution.perform(new StartActivity(activityInstance));
  }

  @Override
  public void startActivities(List<Activity> activities) {
    if (!activities.isEmpty()) {
      // for each nested activity 
      for (Activity activity: activities) {
        startActivity(activity);
      }
    }
  }
  
  /** takes the given transitions if there are any 
   * or propagates the execution flow to the parent otherwise */
  @Override
  public void takeTransitions(List<Transition> transitions) {
    endScopeInstance();
    if (transitions!=null) {
      for (Transition transition : transitions) {
        takeTransition(transition);
      }
    }
  }

  @Override
  public void takeTransition(Transition transition) {
    endScopeInstance();
    if (transition!=null
        && transition.to!=null
        && context.isConditionMet(transition.condition)) {
      startActivity(transition.to, execution.scopeInstance.getParent());
    }
  }

  /** puts the current execution flow on hold in the current activity instance till 
   * an external service signals completion */
  @Override
  public void waitForExternalMessage() {
    execution.scopeInstance.state = new WaitingForMessage(); 
  }

  /** ends the activity instance and propagates the execution flow to the parent */
  @Override
  public void onwards() {
    endScopeInstance();
    ScopeInstance scopeInstance = execution.scopeInstance;
    if (scopeInstance instanceof ActivityInstance) {
      ActivityInstance activityInstance = (ActivityInstance) scopeInstance;
      Activity activity = (Activity) scopeInstance.scope;
      activity.onwards(activityInstance, context, this);
    } else {
      WorkflowInstance workflowInstance = (WorkflowInstance) scopeInstance;
      Workflow workflow = (Workflow) scopeInstance.scope;
      workflow.onwards(workflowInstance, context, this);
    }
  }

  public void setState(ExecutionState state) {
    ScopeInstance scopeInstance = execution.scopeInstance;
    ExecutionState oldState = scopeInstance.state; 
    scopeInstance.state = state;
    if (scopeInstance instanceof ActivityInstance) {
      persistence.activityInstanceStateUpdate((ActivityInstance)scopeInstance, oldState);
    } else {
      persistence.workflowInstanceStateUpdate((WorkflowInstance)scopeInstance, oldState);
    }
  }
  
  public void endScopeInstance() {
    ScopeInstance scopeInstance = execution.scopeInstance;
    if (!scopeInstance.isEnded()) {
      scopeInstance.state = new Ended();
      if (scopeInstance instanceof ActivityInstance) {
        persistence.activityInstanceEnded((ActivityInstance)scopeInstance);
      } else {
        persistence.workflowInstanceEnded((WorkflowInstance)scopeInstance);
      }
    }
  }

  @Override
  public void notifyParentActivityInstanceEnded() {
  }
}
