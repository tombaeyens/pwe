package ch03.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch03.data.Condition;
import ch03.data.InputExpression;
import ch03.data.OutputExpression;
import ch03.data.TypedValue;
import ch03.engine.context.Context;
import ch03.engine.operation.HandleActivityInstanceMessage;
import ch03.engine.operation.Operation;
import ch03.engine.operation.StartActivity;
import ch03.engine.operation.StartWorkflowInstance;
import ch03.engine.state.Created;
import ch03.engine.state.Ended;
import ch03.engine.state.ExecutionState;
import ch03.engine.state.Starting;
import ch03.engine.state.WaitingForMessage;
import ch03.model.Activity;
import ch03.model.ActivityInstance;
import ch03.model.ScopeInstance;
import ch03.model.Transition;
import ch03.model.Variable;
import ch03.model.VariableInstance;
import ch03.model.Workflow;
import ch03.model.WorkflowInstance;


/** Represents the workflow interpretation when it's being allocated to a Thread. 
 *   
 * @author Tom Baeyens
 */
public class Execution {
  
  ScopeInstance scopeInstance;
  boolean isAsync = false;
  LinkedList<Operation> operations = null; // null is important. @see perform(Operation)
  LinkedList<Operation> asyncOperations = null;
  ExecutionContextImpl executionContext = new ExecutionContextImpl(this);
  ExecutionControllerImpl executionController = new ExecutionControllerImpl(this);
  
  WorkflowInstancePersistence persistence = null;
  Asynchronizer asynchronizer = null;
  
  public ExecutionContextImpl getExecutionContext() {
    return executionContext;
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
    
    WorkflowInstance workflowInstance = instantiateWorkflowInstance();
    workflowInstance.workflow = workflow;
    workflowInstance.scope = workflow;
    workflowInstance.state = new Starting();
    workflowInstance.id = persistence.generateWorkflowInstanceId(workflowInstance);
    
    persistence.workflowInstanceCreated(workflowInstance);

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
  
  public void enterScope() {
    Map<String,Variable> variables = scopeInstance.scope.variables;
    for (String key: variables.keySet()) {
      Variable variable = variables.get(key);
      createVariableInstance(variable);
    }
  }

  public void createVariableInstance(Variable variable) {
    VariableInstance variableInstance = instantiateVariableInstance();
    variableInstance.setVariable(variable);
    variableInstance.setScopeInstance(scopeInstance);
    scopeInstance.variableInstances.put(variable.getId(), variableInstance);
    variableInstance.setId(persistence.generateVariableInstanceId(variableInstance));
    
    if (variable.getInitialValue()!=null) {
      variableInstance.setTypedValue(new TypedValue(variable.getType(), variable.getInitialValue()));
    } else if (variable.getInitialValueExpression()!=null) {
      variableInstance.setTypedValue(getTypedValue(variable.getInitialValueExpression())); 
    }

    persistence.variableInstanceCreated(variableInstance);
  }

  public void setVariableInstanceValue(String variableId, TypedValue typedValue) {
    VariableInstance variableInstance = scopeInstance.findVariableInstanceByVariableIdRecursive(variableId);
    setVariableInstanceValue(variableInstance, typedValue);
  }

  protected void setVariableInstanceValue(VariableInstance variableInstance, TypedValue newValue) {
    TypedValue oldValue = variableInstance.getTypedValue();
    persistence.variableInstanceValueUpdated(variableInstance, oldValue);
  }
  
  public void leaveScope(Context context) {
  }
  
  protected void perform(Operation operation) {
    if (operations==null) {
      operations = new LinkedList<>();
      addOperation(operation);
      executeOperations();
      executeAsynchronousOperations();

    } else {
      addOperation(operation);
    }
  }

  protected void addOperation(Operation operation) {
    if (isAsync || operation.isSynchonrous()) {
      operations.add(operation);
      persistence.operationSynchronousAdded(operation);
    } else {
      persistence.operationAsynchronousAdded(operation);
      asyncOperations.add(operation);
    }
  }

  protected void executeOperations() {
    while (!operations.isEmpty()) {
      // This execution still has more work to do.
      // At this place the persistence is in a consistent state 
      // to be resumed later if things would crash further down.
      persistence.savePoint(scopeInstance.getWorkflowInstance(), operations, asyncOperations);
      Operation current = operations.removeFirst();
      persistence.operationSynchronousRemoved(current);
      this.scopeInstance = current.getActivityInstance();
      current.perform(this, executionContext, executionController);
    }
  }

  protected void executeAsynchronousOperations() {
    if (asyncOperations!=null && !asyncOperations.isEmpty()) {
      operations = asyncOperations;
      asyncOperations = null;
      
      asynchronizer.continueAsynchrous(this);
    }
    // No more work to be done
    persistence.flush(scopeInstance.getWorkflowInstance());
  }
  
  /** It's the responsibility of the asynchronizer to call this 
   * method in a different thread */
  public void continueAsynchrous() {
    isAsync = true;
    executeOperations();
    // there should be no further asynchronous work here
  }
  
  /** Resumes a crashed workflow instance after being restored 
   * from persistence. */  
  public void resume() {
    executeOperations();
    executeAsynchronousOperations();
    
  }
  
  public TypedValue getTypedValue(String key) {
    return executionContext.get(key);
  }

  public Object getValue(String key) {
    TypedValue typedValue = getTypedValue(key);
    return typedValue!=null ? typedValue.getValue(): null;
  }

  public TypedValue getTypedValue(InputExpression expression) {
    return expression.getTypedValue(executionContext);
  }

  public Object getValue(InputExpression expression) {
    TypedValue typedValue = expression.getTypedValue(executionContext);
    return typedValue!=null ? typedValue.getValue() : null;
  }

  /** moves the position of the execution up one level to the parent of the current scopeInstance */
  public void up() {
    scopeInstance = scopeInstance.parent;
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

  public ScopeInstance getScopeInstance() {
    return scopeInstance;
  }

  
  public boolean isAsync() {
    return isAsync;
  }

  
  public LinkedList<Operation> getOperations() {
    return operations;
  }

  
  public LinkedList<Operation> getAsyncOperations() {
    return asyncOperations;
  }

  public WorkflowInstancePersistence getPersistence() {
    return persistence;
  }

  
  public Asynchronizer getAsynchronizer() {
    return asynchronizer;
  }

  public void getInputs() {
    Map<String,TypedValue> inputs = new LinkedHashMap<>();
    Map<String,InputExpression> inputParameters = scopeInstance.scope.inputParameters;
    for (String key: inputParameters.keySet()) {
      InputExpression inputExpression = inputParameters.get(key);
      TypedValue typedValue = inputExpression.getTypedValue(executionContext);
      inputs.put(key, typedValue);
    }
    executionContext.setInputs(inputs);
    executionContext.setOutputs(new HashMap<String,TypedValue>());
  }

  public void setOutputs() {
    Map<String,TypedValue> outputs = executionContext.getOutputs();
    Map<String,OutputExpression> outputParameters = scopeInstance.scope.outputParameters;
    for (String key: outputParameters.keySet()) {
      TypedValue typedValue = outputs.get(key);
      OutputExpression outputExpression = outputParameters.get(key);
      if (outputExpression!=null) {
        outputExpression.setTypedValue(executionContext, typedValue);
      } else {
        VariableInstance variableInstance = scopeInstance.findVariableInstanceByVariableIdRecursive(key);
        setVariableInstanceValue(variableInstance, typedValue);
      }
    }
    executionContext.setInputs(null);
    executionContext.setOutputs(null);
  }

  public ExecutionControllerImpl getExecutionController() {
    return executionController;
  }
}
