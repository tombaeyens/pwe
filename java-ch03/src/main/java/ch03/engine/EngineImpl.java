package ch03.engine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch03.data.TypedValue;
import ch03.engine.context.MapContext;
import ch03.engine.operation.Operation;
import ch03.engine.state.WaitingForActivities;
import ch03.model.Activity;
import ch03.model.ActivityInstance;
import ch03.model.ScopeInstance;
import ch03.model.Workflow;
import ch03.model.WorkflowInstance;
import ch03.util.Logger;
import ch03.util.LoggerFactory;


/** 
 * Represents the current execution point while a workflow is being interpretated.
 * @author Tom Baeyens
 */
public class EngineImpl implements Engine {
  
  public static final Logger log = LoggerFactory.getLogger(EngineImpl.class);

  
  ScopeInstance scopeInstance;
  LinkedList<Operation> operations = new LinkedList<>();
  List<ExternalAction> externalActions;
  ContextImpl context = null;
  ControllerImpl controller = null;
  Persistence persistence = null;
  
  public ContextImpl getContext() {
    return context;
  }

  @Override
  public WorkflowInstance startWorkflowInstance(Workflow workflow) {
    return startWorkflowInstance(workflow, null);
  }

  @Override
  public WorkflowInstance startWorkflowInstance(Workflow workflow, Map<String, TypedValue> startData) {
    assert workflow!=null;
    WorkflowInstance workflowInstance = new WorkflowInstance();
    workflowInstance.setEngine(this);
    workflowInstance.setWorkflow(workflow);
    workflowInstance.setScope(workflow);
    persistence.workStartWorkflowInstance(workflowInstance);
    log.debug("Created workflow instance %s", workflowInstance);
    setScopeInstance(workflowInstance);
    startScope();
    // applying the start data
    if (startData!=null && !startData.isEmpty()) {
      if (workflow.getInputParameters()!=null) {
        MapContext startDataContext = new MapContext("startData", startData);
        // adding the start data subcontext after the subcontext context
        context.addSubContext(0, startDataContext);
        Map<String, TypedValue> inputs = context.readInputs();
        context.removeSubContext(startDataContext);
        context.setVariableInstances(inputs);
      } else {
        context.setVariableInstances(startData);
      }
    }
    // if there are no client specified start activities...
    List<Activity> autoStartActivities = workflow.getAutoStartActivities();
    if (autoStartActivities!=null && !autoStartActivities.isEmpty()) {
      workflowInstance.setState(WaitingForActivities.INSTANCE);
      controller.startActivityInstances(autoStartActivities);
    }
    executeOperations();
    return workflowInstance;
  }

  public WorkflowInstance message(ActivityInstance activityInstance) {
    return message(activityInstance, null);
  }

  public WorkflowInstance message(ActivityInstance activityInstance, Map<String,TypedValue> messageData) {
    assert activityInstance!=null;
    WorkflowInstance workflowInstance = activityInstance.getWorkflowInstance();
    workflowInstance.setEngine(this);
    setScopeInstance(activityInstance);
    persistence.workStartHandleMessage(activityInstance, messageData);
    activityInstance.getActivity().message(activityInstance, context, controller, messageData);
    executeOperations();
    return workflowInstance;
  }
  
  public void startScope() {
    context.initializeVariables();
    notifyListeners(Listener.START);
  }

  public void endScope() {
    notifyListeners(Listener.END);
  }

  protected void notifyListeners(String eventName) {
    Map<String,List<Listener>> listeners = scopeInstance.getScope().getScopeListeners();
    if (listeners!=null) {
      List<Listener> eventListeners = listeners.get(eventName);
      if (eventListeners!=null) {
        for (Listener listener : eventListeners) {
          listener.execute(scopeInstance, context);
        }
      }
    }
  }

  public void addOperation(Operation operation) {
    operations.add(operation);
    persistence.operationSynchronousAdded(operation);
  }

  public void executeOperations() {
    while (!operations.isEmpty()) {
      // At this place the persistence is in a consistent state 
      // to be resumed later if things would crash further down.
      Operation current = operations.getFirst();
      if (current.requiresTransactionSave()) {
        persistence.workSave(scopeInstance.getWorkflowInstance(), operations, externalActions);
      }
      operations.removeFirst();
      persistence.operationSynchronousRemoved(current);
      this.scopeInstance = current.getScopeInstance();
      current.perform(this, context, controller);
    }
  }
  

  @Override
  public void endWork() {
    // If no activities were started...
    WorkflowInstance workflowInstance = scopeInstance.getWorkflowInstance();
    if (!workflowInstance.isEnded() && !workflowInstance.hasActivityInstances()) {
      // close the workflow instance
      setScopeInstance(workflowInstance);
      controller.endScopeInstance();
    }
    
    // No more work to be done
    // Ensure the persistence is updated so that incoming requests
    // will find the new state
    persistence.workEnd(workflowInstance, externalActions);
    operations = new LinkedList<>();
    
    if (externalActions!=null) {
      // Perform all notifications to external services
      ArrayList<ExternalAction> externalActionsCopy = new ArrayList<>(externalActions);
      externalActions = null;
      if (externalActionsCopy != null) {
        // the next loop could be performed async in parallel...
        for (int i = 0; i < externalActionsCopy.size(); i++) {
          ExternalAction externalAction = externalActionsCopy.get(i);
          externalAction.executionEnded(context);
          persistence.executionListenerRemove(i, externalAction);
        }
      }
    }
  }

  @Override
  public WorkflowInstance startActivityInstance(ScopeInstance parentScopeInstance, Activity activity, Map<String, TypedValue> activityStartData) {
    controller.startActivityInstance(activity, parentScopeInstance, activityStartData);
    return null;
  }

  @Override
  public WorkflowInstance endScopeInstance(ScopeInstance scopeInstance) {
    return null;
  }

  /** Resumes a crashed workflow instance after being restored 
   * from persistence. */  
  @Override
  public void resume(WorkflowInstance workflowInstance, LinkedList<Operation> operations) {
    this.operations = operations;
    executeOperations();
  }
  
  public void addExternalAction(ExternalAction externalAction) {
    if (externalActions==null) {
      externalActions = new ArrayList<>();
    }
    externalActions.add(externalAction);
    persistence.executionListenerAdded(externalAction);
  }

  /** moves the position of the execution up one level to the parent of the current scopeInstance */
  public void up() {
    scopeInstance = scopeInstance.getParent();
  }

  public ScopeInstance getScopeInstance() {
    return scopeInstance;
  }

  public LinkedList<Operation> getOperations() {
    return operations;
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

  public void setOperations(LinkedList<Operation> operations) {
    this.operations = operations;
  }

  public void setContext(ContextImpl context) {
    this.context = context;
  }

  public void setController(ControllerImpl controller) {
    this.controller = controller;
  }

  public List<ExternalAction> getExternalAction() {
    return externalActions;
  }

  public void setExternalAction(List<ExternalAction> externalActions) {
    this.externalActions = externalActions;
  }
}
