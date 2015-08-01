package ch03;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch03.data.Condition;
import ch03.data.Expression;
import ch03.data.TypedValue;


public class ExecutionController {
  
  ScopeInstance scopeInstance;
  boolean isAsync = false;
  LinkedList<Operation> operations = null; // null is important. @see perform(Operation)
  LinkedList<Operation> asyncOperations = null;
  ExecutionControllerContext context = new ExecutionControllerContext(this);
  Asynchronizer asynchronizer = null;
  Context externalContext; 
  List<WorkflowListener> listeners;
  
  public ExecutionController(ScopeInstance scopeInstance, Context externalContext, List<WorkflowListener> listeners) {
    this.scopeInstance = scopeInstance;
    this.externalContext = externalContext;
    this.listeners = listeners;
  }

  public void startTrigger(Trigger trigger, Map<String, Object> initialData) {
    WorkflowInstance workflowInstance = (WorkflowInstance) scopeInstance;
    workflowInstance.state = new Starting(); 
    workflowInstance.initializeVariableInstances(initialData);
    workflowInstance.triggerInstance = new TriggerInstance(trigger, workflowInstance);
    workflowInstance.triggerInstance.initializeVariableInstances(initialData);
    trigger.start(this);
  }

  /** starts the given scope */
  public ActivityInstance startActivity(Activity activity) {
    return startActivity(activity, this.scopeInstance);
  }

  /** starts the given scope */
  public ActivityInstance startActivity(Activity activity, ScopeInstance parentScopeInstance) {
    if (activity==null || !isConditionMet(activity.condition, parentScopeInstance)) {
      return null;
    }
    ActivityInstance activityInstance = new ActivityInstance(activity, parentScopeInstance, new Starting());
    parentScopeInstance.activityInstances.add(activityInstance);
    perform(new StartActivity(activityInstance));
    return activityInstance;
  }

  public List<ActivityInstance> startActivities(List<Activity> activities) {
    List<ActivityInstance> activityInstances = new ArrayList<>();
    if (!activities.isEmpty()) {
      // for each nested activity 
      for (Activity activity: activities) {
        ActivityInstance activityInstance = startActivity(activity);
        if (activityInstance!=null) {
          activityInstances.add(activityInstance);
        }
      }
    }
    return activityInstances;
  }
  
  /** ends the activity instance and propagates the execution flow to the parent */
  public void end() {
    endScopeInstance();
    ScopeInstance parent = scopeInstance.getParent();
    if (parent!=null) {
      scopeInstance = parent;
    }
  }

  /** takes the outgoing applicable transitions if there are any 
   * or propagates the execution flow to the parent otherwise */
  public void onwards() {
    List<Transition> transitionsToTake = new ArrayList<>();
    Activity activity = ((ActivityInstance)scopeInstance).activity;
    for (Transition outTransition: activity.outTransitions) {
      if (isConditionMet(outTransition.condition, scopeInstance)) {
        transitionsToTake.add(outTransition);
      }
    }
    if (!transitionsToTake.isEmpty()) {
      takeTransitions(transitionsToTake);
    } else {
      end();
    }
  }

  protected boolean isConditionMet(Condition condition, ScopeInstance scopeInstance) {
    if (condition==null) {
      return true;
    }
    return condition.evaluate(context);
  }

  /** takes the given transitions if there are any 
   * or propagates the execution flow to the parent otherwise */
  public List<ActivityInstance> takeTransitions(List<Transition> transitions) {
    endScopeInstance();
    List<ActivityInstance> activityInstances = new ArrayList<>();
    if (transitions!=null) {
      for (Transition transition : transitions) {
        ActivityInstance activityInstance = takeTransition(transition);
        if (activityInstance!=null) {
          activityInstances.add(activityInstance);
        }
      }
    }
    return activityInstances;
  }

  public ActivityInstance takeTransition(Transition transition) {
    endScopeInstance();
    if (transition==null
        || transition.to==null
        || !isConditionMet(transition.condition, scopeInstance)) {
      return null;
    }
    return startActivity(transition.to, scopeInstance.getParent());
  }

  protected void endScopeInstance() {
    if (!scopeInstance.isEnded()) {
      scopeInstance.end();
    }
  }
  
  protected void perform(Operation operation) {
    if (operations==null) {
      operations = new LinkedList<>();
      addOperation(operation);
      executeOperations();
      if (!asyncOperations.isEmpty()) {
        operations = asyncOperations;
        asyncOperations = null;
        asynchronizer.continueAsynchrous(this);
      }
    } else {
      addOperation(operation);
    }
  }

  protected void addOperation(Operation operation) {
    if (isAsync || operation.isSynchonrous()) {
      operations.add(operation);
    } else {
      asyncOperations.add(operation);
    }
  }

  public void continueAsynchrous() {
    isAsync = true;
    executeOperations();
  }

  protected void executeOperations() {
    while (!operations.isEmpty()) {
      Operation current = operations.removeFirst();
      this.scopeInstance = current.getScopeInstance();
      current.perform(this);
    }
  }

  public TypedValue get(String key) {
    Expression expression = scopeInstance.scope.inputs.get(key);
    if (expression!=null) {
      return get(expression);
    }
    return context.get(key);
  }

  public Object getValue(String key) {
    TypedValue typedValue = get(key);
    return typedValue!=null ? typedValue.getValue(): null;
  }

  public TypedValue get(Expression expression) {
    return expression.get(context);
  }

  public Object getValue(Expression expression) {
    TypedValue typedValue = expression.get(context);
    return typedValue!=null ? typedValue.getValue() : null;
  }
}
