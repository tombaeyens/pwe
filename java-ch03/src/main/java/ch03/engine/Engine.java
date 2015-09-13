package ch03.engine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch03.data.TypedValue;
import ch03.engine.context.MapContext;
import ch03.engine.operation.HandleMessage;
import ch03.engine.operation.Operation;
import ch03.engine.state.Starting;
import ch03.model.Activity;
import ch03.model.ActivityInstance;
import ch03.model.ScopeInstance;
import ch03.model.VariableInstance;
import ch03.model.Workflow;
import ch03.model.WorkflowInstance;
import ch03.util.Logger;
import ch03.util.LoggerFactory;


/** 
 * Represents the current execution point while a workflow is being interpretated.
 * @author Tom Baeyens
 */
public class Engine {
  
  public static final Logger log = LoggerFactory.getLogger(Engine.class);
  
  ScopeInstance scopeInstance;
  boolean isAsync = false;
  LinkedList<Operation> operations = new LinkedList<>();
  LinkedList<Operation> asyncOperations = new LinkedList<>();
  List<ExecutionListener> executionListeners;
  ContextImpl context = null;
  ControllerImpl controller = null;
  Persistence persistence = null;
  Asynchronizer asynchronizer = null;
  
  public ContextImpl getContext() {
    return context;
  }

  public WorkflowInstance startWorkfowInstanceSynchronous(Workflow workflow, Map<String, TypedValue> startData, List<Activity> startActivities) {
    WorkflowInstance workflowInstance = instantiateWorkflowInstance();
    workflowInstance.setEngine(this);
    workflowInstance.setWorkflow(workflow);
    workflowInstance.setScope(workflow);
    workflowInstance.setState(new Starting());
    persistence.transactionStartWorkflowInstance(workflowInstance);
    log.debug("Created workflow instance %s", workflowInstance);
    setScopeInstance(workflowInstance);
    enterScope();
    // applying the start data
    if (startData!=null && !startData.isEmpty() && workflow.getInputParameters()!=null) {
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
    // if there are no client specified start activities...
    if (startActivities==null) {
      // take the start activities defined by the workflow
      startActivities = workflow.getStartActivities();
    }
    List<ActivityInstance> activityInstances = controller.startActivityInstances(startActivities);
    if (activityInstances.isEmpty()) {
      controller.endScopeInstance();
    }
    executeSynchronousOperations(true);
    return workflowInstance;
  }

  public WorkflowInstance handleActivityInstanceMessage(ActivityInstance activityInstance) {
    return handleActivityInstanceMessage(activityInstance, null);
  }

  public WorkflowInstance handleActivityInstanceMessage(ActivityInstance activityInstance, Map<String,TypedValue> messageData) {
    activityInstance.getActivity().handleMessage(activityInstance, context, controller, messageData);
    executeSynchronousOperations(true);

    return activityInstance.getWorkflowInstance();
  }
  
  public void enterScope() {
    context.initializeVariables();
  }

  public void leaveScope() {
    // TODO cancel timers
  }
  
  public void addOperation(Operation operation) {
    if (isAsync || !operation.isAsynchronous()) {
      operations.add(operation);
      persistence.operationSynchronousAdded(operation);
    } else {
      persistence.operationAsynchronousAdded(operation);
      asyncOperations.add(operation);
    }
  }

  public void executeOperations() {
    executeSynchronousOperations(true);
    executeAsynchronousOperations();
  }

  public void executeSynchronousOperations(boolean skipFirstSave) {
    boolean save = !skipFirstSave;
    while (!operations.isEmpty()) {
      // At this place the persistence is in a consistent state 
      // to be resumed later if things would crash further down.
      Operation current = operations.getFirst();
      if (save) {
        if (current.requiresTransactionSave()) {
           transactionSave();
        }
      } else {
        save = true; 
      }
      operations.removeFirst();
      persistence.operationSynchronousRemoved(current);
      this.scopeInstance = current.getScopeInstance();
      current.perform(this, context, controller);
    }
  }

  protected void transactionSave() {
    persistence.transactionSave(scopeInstance.getWorkflowInstance(), operations, asyncOperations, executionListeners);
  }

  public void executeAsynchronousOperations() {
    WorkflowInstance workflowInstance = scopeInstance.getWorkflowInstance();
    if (asyncOperations!=null && !asyncOperations.isEmpty()) {
      operations = asyncOperations;
      asyncOperations = new LinkedList<>();
      asynchronizer.continueAsynchrous(this);
    }
    // No more work to be done
    
    // Ensure the persistence is updated so that incoming requests
    // will find the new state
    persistence.transactionEnd(workflowInstance, executionListeners);
    
    // Perform all notifications to external services
    if (executionListeners!=null) {
      for (ExecutionListener executionListener: executionListeners) {
        executionListener.executionEnded();
      }
    }
    operations = new LinkedList<>();
    asyncOperations = new LinkedList<>();
  }
  
  /** It's the responsibility of the asynchronizer to call this 
   * method in a different thread */
  public void continueAsynchrous() {
    isAsync = true;
    executeSynchronousOperations(false);
    // there should be no further asynchronous work here
  }
  
  /** Resumes a crashed workflow instance after being restored 
   * from persistence. */  
  public void resume() {
    executeSynchronousOperations(false);
    executeAsynchronousOperations();
  }
  
  public void addExecutionListener(ExecutionListener executionListener) {
    if (executionListeners==null) {
      executionListeners = new ArrayList<>();
    }
    executionListeners.add(executionListener);
    persistence.executionListenerAdded(executionListener);
  }

  /** moves the position of the execution up one level to the parent of the current scopeInstance */
  public void up() {
    scopeInstance = scopeInstance.getParent();
  }

  public WorkflowInstance instantiateWorkflowInstance() {
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
  
  public Asynchronizer getAsynchronizer() {
    return asynchronizer;
  }

  public ControllerImpl getController() {
    return controller;
  }

  public Persistence getPersistence() {
    return persistence;
  }

  public void setPersistence(Persistence persistence) {
    this.persistence = persistence;
  }

  public void setScopeInstance(ScopeInstance scopeInstance) {
    this.scopeInstance = scopeInstance;
  }

  public void setAsync(boolean isAsync) {
    this.isAsync = isAsync;
  }

  public void setOperations(LinkedList<Operation> operations) {
    this.operations = operations;
  }

  public void setAsyncOperations(LinkedList<Operation> asyncOperations) {
    this.asyncOperations = asyncOperations;
  }

  public void setContext(ContextImpl context) {
    this.context = context;
  }

  public void setController(ControllerImpl controller) {
    this.controller = controller;
  }

  public void setAsynchronizer(Asynchronizer asynchronizer) {
    this.asynchronizer = asynchronizer;
  }
  
  public List<ExecutionListener> getExecutionListeners() {
    return executionListeners;
  }

  public void setExecutionListeners(List<ExecutionListener> executionListeners) {
    this.executionListeners = executionListeners;
  }
}
