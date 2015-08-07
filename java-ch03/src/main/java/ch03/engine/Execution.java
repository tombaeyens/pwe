package ch03.engine;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch03.data.Condition;
import ch03.data.InputExpression;
import ch03.data.OutputExpression;
import ch03.data.TypedValue;
import ch03.model.Activity;
import ch03.model.ActivityInstance;
import ch03.model.ScopeInstance;
import ch03.model.Transition;
import ch03.model.Variable;
import ch03.model.VariableInstance;
import ch03.model.Workflow;
import ch03.model.WorkflowInstance;
import ch03.model.WorkflowListener;


/**
 * @author Tom Baeyens
 */
public class Execution {
  
  ScopeInstance scopeInstance;
  boolean isAsync = false;
  LinkedList<Operation> operations = null; // null is important. @see perform(Operation)
  LinkedList<Operation> asyncOperations = null;
  ExecutionContext executionContext = new ExecutionContext(this);
  Asynchronizer asynchronizer = null;
  List<WorkflowListener> listeners;
  

  public void addContext(String name, Context context) {
    executionContext.add(name, context);
  }
  
  public void removeContext(String name) {
    executionContext.remove(name);
  }

  public WorkflowInstance startWorkflowInstance(Workflow workflow) {
    return startWorkflowInstance(workflow, null, null);
  }

  public WorkflowInstance startWorkflowInstance(Workflow workflow, Map<String, TypedValue> startData) {
    return startWorkflowInstance(workflow, startData, null);
  }

  public WorkflowInstance startWorkflowInstance(
          Workflow workflow, 
          Map<String, TypedValue> startData, 
          List<Activity> startActivities) {
    
    listeners = workflow.listeners;

    WorkflowInstance workflowInstance = instantiateWorkflowInstance();
    workflowInstance.workflow = workflow;
    workflowInstance.scope = workflow;
    workflowInstance.id = generateWorkflowInstanceId();
    workflowInstance.state = new Starting(); 

    scopeInstance = workflowInstance;
    enterScope();
    
    perform(new StartWorkflowInstance(workflowInstance, startData, startActivities));
    
    return workflowInstance;
  }
  
  public WorkflowInstance handleActivityInstanceMessage(ActivityInstance activityInstance) {
    return handleActivityInstanceMessage(activityInstance, null);
  }

  public WorkflowInstance handleActivityInstanceMessage(ActivityInstance activityInstance, Map<String,TypedValue> messageData) {
    perform(new HandleActivityInstanceMessage(activityInstance, messageData));
    return activityInstance.getWorkflowInstance();
  }

  
  public Map<String,TypedValue> getInputs() {
    Map<String,TypedValue> inputs = new LinkedHashMap<>();
    Map<String,InputExpression> inputParameters = scopeInstance.scope.inputParameters;
    for (String key: inputParameters.keySet()) {
      InputExpression inputExpression = inputParameters.get(key);
      TypedValue typedValue = inputExpression.get(executionContext);
      inputs.put(key, typedValue);
    }
    return inputs;
  }
  
  public void setOutputs(Map<String,TypedValue> outputs) {
    Map<String,OutputExpression> outputParameters = scopeInstance.scope.outputParameters;
    for (String key: outputParameters.keySet()) {
      TypedValue typedValue = outputs.get(key);
      OutputExpression outputExpression = outputParameters.get(key);
      if (outputExpression!=null) {
        outputExpression.set(executionContext, typedValue);
      } else {
        VariableInstance variableInstance = scopeInstance.findVariableInstanceByVariableIdRecursive(key);
        setVariableInstanceValue(variableInstance, typedValue);
      }
    }
  }
  
  public void enterScope() {
    Map<String,Variable> variables = scopeInstance.scope.variables;
    for (String key: variables.keySet()) {
      Variable variable = variables.get(key);
      createVariableInstance(variable);
    }
  }

  public void createVariableInstance(Variable variable) {
    VariableInstance variableInstance = instantiateVariableInstance();
    variableInstance.setId(generateVariableInstanceId());
    variableInstance.setVariable(variable);
    variableInstance.setScopeInstance(scopeInstance);
    scopeInstance.variableInstances.put(variable.getId(), variableInstance);

    if (variable.getInitialValue()!=null) {
      variableInstance.setTypedValue(new TypedValue(variable.getType(), variable.getInitialValue()));
    } else if (variable.getInitialValueExpression()!=null) {
      variableInstance.setTypedValue(getTypedValue(variable.getInitialValueExpression())); 
    }
  }

  public void setVariableInstanceValue(String variableId, TypedValue typedValue) {
    VariableInstance variableInstance = scopeInstance.findVariableInstanceByVariableIdRecursive(variableId);
    setVariableInstanceValue(variableInstance, typedValue);
  }

  protected void setVariableInstanceValue(VariableInstance variableInstance, TypedValue typedValue) {
    variableInstance.setTypedValue(typedValue);
  }

  public void leaveScope(Context context) {
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
    ActivityInstance activityInstance = instantiateActivityInstance();
    activityInstance.id = generateActivityInstanceId();
    activityInstance.scope = activity;
    activityInstance.activity = activity;
    activityInstance.state = new Starting();

    activityInstance.parent = parentScopeInstance;
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
    return condition.evaluate(executionContext);
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
      scopeInstance.state = new Ended();
    }
  }
  
  protected void perform(Operation operation) {
    if (operations==null) {
      operations = new LinkedList<>();
      addOperation(operation);
      executeOperations();
      if (asyncOperations!=null && !asyncOperations.isEmpty()) {
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

  public TypedValue getTypedValue(String key) {
    return executionContext.get(key);
  }

  public Object getValue(String key) {
    TypedValue typedValue = getTypedValue(key);
    return typedValue!=null ? typedValue.getValue(): null;
  }

  public TypedValue getTypedValue(InputExpression expression) {
    return expression.get(executionContext);
  }

  public Object getValue(InputExpression expression) {
    TypedValue typedValue = expression.get(executionContext);
    return typedValue!=null ? typedValue.getValue() : null;
  }

  /** moves the position of the execution up one level to the parent of the current scopeInstance */
  public void up() {
    scopeInstance = scopeInstance.parent;
  }

  /** puts the current execution flow on hold in the current activity instance till 
   * an external service signals completion */
  public void waitForExternalMessage() {
    scopeInstance.state = new WaitingForMessage(); 
  }

  protected WorkflowInstance instantiateWorkflowInstance() {
    return new WorkflowInstance();
  }

  protected ActivityInstance instantiateActivityInstance() {
    return new ActivityInstance();
  }

  protected VariableInstance instantiateVariableInstance() {
    return new VariableInstance();
  }

  protected String generateWorkflowInstanceId() {
    return null;
  }

  protected String generateActivityInstanceId() {
    return null;
  }

  protected String generateVariableInstanceId() {
    return null;
  }
}
