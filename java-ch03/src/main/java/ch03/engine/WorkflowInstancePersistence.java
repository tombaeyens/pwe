package ch03.engine;

import java.util.List;

import ch03.data.TypedValue;
import ch03.engine.operation.Operation;
import ch03.engine.state.ExecutionState;
import ch03.model.ActivityInstance;
import ch03.model.VariableInstance;
import ch03.model.WorkflowInstance;


/**
 * @author Tom Baeyens
 */
public interface WorkflowInstancePersistence {

  String generateWorkflowInstanceId(WorkflowInstance workflowInstance);

  String generateVariableInstanceId(VariableInstance variableInstance);

  String generateActivityInstanceId(ActivityInstance activityInstance);

  void workflowInstanceCreated(WorkflowInstance workflowInstance);

  /** implies variableInstanceValueUpdated for the initial value */
  void variableInstanceCreated(VariableInstance variableInstance);

  /** variableInstance contains the new value */
  void variableInstanceValueUpdated(VariableInstance variableInstance, TypedValue oldValue);

  void activityInstanceCreated(ActivityInstance activityInstance);

  /** implies state update to ended */
  void activityInstanceEnded(ActivityInstance activityInstance);

  /** implies state update to ended */
  void workflowInstanceEnded(WorkflowInstance workflowInstance);

  /** activityInstance contains the new state */
  void activityInstanceStateUpdate(ActivityInstance activityInstance, ExecutionState oldState);

  /** workflowInstance contains the new state */
  void workflowInstanceStateUpdate(WorkflowInstance workflowInstance, ExecutionState oldState);

  void operationSynchronousAdded(Operation operation);

  void operationSynchronousRemoved(Operation current);

  void operationAsynchronousAdded(Operation operation);

  void savePoint(WorkflowInstance workflowInstance, List<Operation> operations, List<Operation> asyncOperations);

  /** No more work will be done by the Execution.
   * After this, the workflow instance is either ended or it will
   * wait for an external signal.  This could potentially take 
   * a long time.  After receiving the signal, the workflow 
   * instance could execute further. 
   * This is a good moment to update or overwrite the entire 
   * workflow instance data structure */
  void flush(WorkflowInstance workflowInstance);
}
