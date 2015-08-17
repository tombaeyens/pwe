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
import ch03.engine.context.AllContexts;
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
  AllContexts allContexts = new AllContexts(this);
  Map<String, TypedValue> inputs;
  Map<String, TypedValue> outputs;
  
  WorkflowInstancePersistence persistence = null;
  Asynchronizer asynchronizer = null;
  
  public AllContexts getAllContexts() {
    return allContexts;
  }
  
  /** fluent setter for the external context */
  public Execution externalContext(Context externalContext) {
    allContexts.addExternal(externalContext);
    return this;
  }
  
  public Context getExternalContext() {
    return allContexts.getExternalContext();
  }
  
  public Object findExternally(String key) {
    Context externalContext = getExternalContext();
    return externalContext!=null ? externalContext.get(key) : null;
  }
  
  public <T> T findExternally(Class<T> type) {
    return (T) findExternally(type.getName());
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
  
  public Map<String,TypedValue> getInputs() {
    Map<String,TypedValue> inputs = new LinkedHashMap<>();
    Map<String,InputExpression> inputParameters = scopeInstance.scope.inputParameters;
    for (String key: inputParameters.keySet()) {
      InputExpression inputExpression = inputParameters.get(key);
      TypedValue typedValue = inputExpression.getTypedValue(allContexts);
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
        outputExpression.setTypedValue(allContexts, typedValue);
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
    activityInstance.scope = activity;
    activityInstance.activity = activity;
    activityInstance.state = new Created();
    activityInstance.parent = parentScopeInstance;
    parentScopeInstance.activityInstances.add(activityInstance);
    activityInstance.id = persistence.generateActivityInstanceId(activityInstance);

    persistence.activityInstanceCreated(activityInstance);
    
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
    return condition.evaluate(allContexts);
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
      if (scopeInstance instanceof ActivityInstance) {
        persistence.activityInstanceEnded((ActivityInstance)scopeInstance);
      } else {
        persistence.workflowInstanceEnded((WorkflowInstance)scopeInstance);
      }
    }
  }

  public void setState(ExecutionState state) {
    ExecutionState oldState = scopeInstance.state; 
    scopeInstance.state = state;
    if (scopeInstance instanceof ActivityInstance) {
      persistence.activityInstanceStateUpdate((ActivityInstance)scopeInstance, oldState);
    } else {
      persistence.workflowInstanceStateUpdate((WorkflowInstance)scopeInstance, oldState);
    }
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
      this.scopeInstance = current.getScopeInstance();
      current.perform(this);
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
    return allContexts.get(key);
  }

  public Object getValue(String key) {
    TypedValue typedValue = getTypedValue(key);
    return typedValue!=null ? typedValue.getValue(): null;
  }

  public TypedValue getTypedValue(InputExpression expression) {
    return expression.getTypedValue(allContexts);
  }

  public Object getValue(InputExpression expression) {
    TypedValue typedValue = expression.getTypedValue(allContexts);
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

  public void collectInputs() {
    inputs = new HashMap<>();
    outputs = new HashMap<>();
    ActivityInstance activityInstance = (ActivityInstance) scopeInstance;
    Activity activity = activityInstance.activity;
    if (activity.inputBindings!=null) {
      for (String inputKey: activity.inputBindings.keySet()) {
        InputExpression inputBinding = activity.inputBindings.get(inputKey);
        TypedValue input = inputBinding.getTypedValue(allContexts);
        inputs.put(inputKey, input);
      }
    }
  }

  public void propagateOutputs() {
    ActivityInstance activityInstance = (ActivityInstance) scopeInstance;
    Activity activity = activityInstance.activity;
    if (!outputs.isEmpty() && activity.outputBindings!=null) {
      for (String outputKey: outputs.keySet()) {
        TypedValue output = outputs.get(outputKey);
        OutputExpression outputBinding = activity.outputBindings.get(outputKey);
        outputBinding.setTypedValue(allContexts, output);
      }
    }
  }
}
