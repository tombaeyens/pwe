package ch03.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch03.data.Type;
import ch03.data.TypedValue;
import ch03.engine.operation.Operation;
import ch03.engine.state.ExecutionState;
import ch03.model.ActivityInstance;
import ch03.model.ScopeInstance;
import ch03.model.VariableInstance;
import ch03.model.WorkflowInstance;


/**
 * @author Tom Baeyens
 */
public class PersistenceImpl implements Persistence {
  
  List<String> updates = new ArrayList<>();
  WorkflowInstance workflowInstance;
  
  protected void addUpdate(String update) {
    updates.add(update);
  }
  
  static int nextWorkflowInstanceId = 1;
  public String nextWorkflowInstanceId(WorkflowInstance workflowInstance) {
    return Integer.toString(nextWorkflowInstanceId++);
  }
  
  static int nextActivityInstanceId = 1;
  public String nextActivityInstanceId(ActivityInstance activityInstance) {
    return Integer.toString(nextActivityInstanceId++);
  }
  
  static int nextVariableInstanceId = 1;
  public String nextVariableInstanceId(VariableInstance variableInstance) {
    return Integer.toString(nextVariableInstanceId++);
  }
  
  @Override
  public void transactionStartWorkflowInstance(WorkflowInstance workflowInstance) {
    String workflowInstanceId = nextWorkflowInstanceId(workflowInstance);
    workflowInstance.setId(workflowInstanceId);
    System.out.println(" | Transaction starts for new workflow instance "+workflowInstance);
    this.workflowInstance = workflowInstance;
    this.updates = new ArrayList<>();
    addUpdate("Create workflow instance "+workflowInstanceId);
  }

  @Override
  public void transactionStartHandleMessage(ActivityInstance activityInstance, Map<String, TypedValue> messageData) {
    System.out.println(" | Transaction starts to handle message for activity instance "+activityInstance);
    this.workflowInstance = activityInstance.getWorkflowInstance();
    this.updates = new ArrayList<>();
  }

  @Override
  public void variableInstanceCreated(VariableInstance variableInstance) {
    ScopeInstance scopeInstance = variableInstance.getScopeInstance();
    String variableInstanceId = nextVariableInstanceId(variableInstance);
    variableInstance.setId(variableInstanceId);
    TypedValue initialValue = variableInstance.getTypedValue();
    addUpdate("Create variable instance "+variableInstance+
              " in "+scopeInstance+
              (initialValue!=null ? " with "+initialValue : ""));
  }

  @Override
  public void activityInstanceCreated(ActivityInstance activityInstance) {
    String activityInstanceId = nextActivityInstanceId(activityInstance);
    activityInstance.setId(activityInstanceId);
    addUpdate("Create activity instance "+activityInstance);
  }

  @Override
  public void variableInstanceValueUpdated(VariableInstance variableInstance, TypedValue oldValue) {
    addUpdate("Update variable instance "+variableInstance+" = "+variableInstance.getTypedValue());
  }

  @Override
  public void activityInstanceEnded(ActivityInstance activityInstance) {
    addUpdate("Mark activity instance "+activityInstance+" as ended");
  }

  @Override
  public void workflowInstanceEnded(WorkflowInstance workflowInstance) {
    addUpdate("Mark workflow instance "+workflowInstance+" as ended");
  }

  @Override
  public void activityInstanceStateUpdate(ActivityInstance activityInstance, ExecutionState oldState) {
    addUpdate("Update state of activity instance "+activityInstance+" to "+activityInstance.getState());
  }

  @Override
  public void workflowInstanceStateUpdate(WorkflowInstance workflowInstance, ExecutionState oldState) {
    addUpdate("Update state of workflow instance "+workflowInstance+" to "+workflowInstance.getState());
  }

  @Override
  public void operationSynchronousAdded(Operation operation) {
    addUpdate("Add operation "+operation);
  }

  @Override
  public void operationSynchronousRemoved(Operation operation) {
    addUpdate("Removed first operation "+operation);
  }

  @Override
  public void operationAsynchronousAdded(Operation operation) {
    addUpdate("Add async operation "+operation);
  }

  @Override
  public void transactionSave(WorkflowInstance workflowInstance, List<Operation> operations, List<Operation> asyncOperations) {
    System.out.println(" | Transaction savepoint for "+workflowInstance.getId());
    for (String update: updates) {
      System.out.println(" | "+update);
    }
    this.updates = new ArrayList<>();
  }

  @Override
  public void transactionEnd(WorkflowInstance workflowInstance) {
    System.out.println("Transaction ends for "+workflowInstance.getId());
  }
}