package ch03.engine;

import java.util.LinkedList;
import java.util.Map;

import ch03.data.TypedValue;
import ch03.engine.operation.HandleMessage;
import ch03.engine.operation.Operation;
import ch03.model.ActivityInstance;
import ch03.model.ScopeInstance;
import ch03.model.VariableInstance;
import ch03.model.WorkflowInstance;


/** 
 * Represents the current execution point while a workflow is being interpretated.
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

  public void leaveScope() {
    // TODO cancel timers
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
    if (isAsync || !operation.isAsynchronous()) {
      operations.add(operation);
      engineListener.operationSynchronousAdded(operation);
    } else {
      engineListener.operationAsynchronousAdded(operation);
      if (asyncOperations==null) {
        asyncOperations = new LinkedList<>();
      }
      asyncOperations.add(operation);
    }
  }

  protected void executeOperations() {
    while (!operations.isEmpty()) {
      // This execution still has more work to do.
      // At this place the persistence is in a consistent state 
      // to be resumed later if things would crash further down.
      
      Operation current = operations.getFirst();
      if (current.requiresTransactionSave()) {
        engineListener.transactionSave(scopeInstance.getWorkflowInstance(), operations, asyncOperations);
      }
      operations.removeFirst();
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
    engineListener.transactionEnd(scopeInstance.getWorkflowInstance());
    operations = null;
    asyncOperations = null;
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

  public void setContext(ContextImpl context) {
    this.context = context;
  }

  public void setController(ControllerImpl controller) {
    this.controller = controller;
  }

  public void setAsynchronizer(Asynchronizer asynchronizer) {
    this.asynchronizer = asynchronizer;
  }
}
