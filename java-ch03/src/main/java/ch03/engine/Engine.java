package ch03.engine;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch03.data.InputExpression;
import ch03.data.OutputExpression;
import ch03.data.TypedValue;
import ch03.engine.context.SubContext;
import ch03.engine.operation.HandleMessage;
import ch03.engine.operation.Operation;
import ch03.engine.operation.StartWorkflow;
import ch03.engine.state.Starting;
import ch03.model.Activity;
import ch03.model.ActivityInstance;
import ch03.model.ScopeInstance;
import ch03.model.Variable;
import ch03.model.VariableInstance;
import ch03.model.Workflow;
import ch03.model.WorkflowInstance;


/** Represents the workflow interpretation when it's being allocated to a Thread. 
 *   
 * @author Tom Baeyens
 */
public class Engine {
  
  ScopeInstance scopeInstance;
  boolean isAsync = false;
  LinkedList<Operation> operations = null; // null is important. @see perform(Operation)
  LinkedList<Operation> asyncOperations = null;
  ContextImpl context = null;
  ControllerImpl controller = null;
  EngineListener engineListener = null;
  Asynchronizer asynchronizer = null;
  
  public Engine() {
    initializeExecutionContext();
    initializeExecutionController();
  }
  
  /** can be overriden by subclasses to customize context behavior */
  protected void initializeExecutionContext() {
    context = new ContextImpl(this);
  }

  /** can be overriden by subclasses to customize controller behavior */
  protected void initializeExecutionController() {
    controller = new ControllerImpl(this);
  }

  public ContextImpl getContext() {
    return context;
  }
  

  

  public WorkflowInstance handleActivityInstanceMessage(ActivityInstance activityInstance) {
    return handleActivityInstanceMessage(activityInstance, null);
  }

  public WorkflowInstance handleActivityInstanceMessage(ActivityInstance activityInstance, Map<String,TypedValue> messageData) {
    perform(new HandleMessage(activityInstance, messageData));
    return activityInstance.getWorkflowInstance();
  }
  
  public void enterScope() {
    context.initializeVariables();
  }

  public void leaveScope(SubContext subContext) {
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
      engineListener.operationSynchronousAdded(operation);
    } else {
      engineListener.operationAsynchronousAdded(operation);
      asyncOperations.add(operation);
    }
  }

  protected void executeOperations() {
    while (!operations.isEmpty()) {
      // This execution still has more work to do.
      // At this place the persistence is in a consistent state 
      // to be resumed later if things would crash further down.
      engineListener.savePoint(scopeInstance.getWorkflowInstance(), operations, asyncOperations);
      Operation current = operations.removeFirst();
      engineListener.operationSynchronousRemoved(current);
      this.scopeInstance = current.getScopeInstance();
      current.perform(this, context, controller);
    }
  }

  protected void executeAsynchronousOperations() {
    if (asyncOperations!=null && !asyncOperations.isEmpty()) {
      operations = asyncOperations;
      asyncOperations = null;
      
      asynchronizer.continueAsynchrous(this);
    }
    // No more work to be done
    engineListener.flush(scopeInstance.getWorkflowInstance());
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

  public EngineListener getEngineListener() {
    return engineListener;
  }

  
  public void setEngineListener(EngineListener engineListener) {
    this.engineListener = engineListener;
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

  
  public void setContext(ContextImpl executionContext) {
    this.context = executionContext;
  }

  
  public void setController(ControllerImpl executionController) {
    this.controller = executionController;
  }

  
  public void setAsynchronizer(Asynchronizer asynchronizer) {
    this.asynchronizer = asynchronizer;
  }
}
