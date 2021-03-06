package ch03.engine;

import java.util.List;
import java.util.Map;

import ch03.data.TypedValue;
import ch03.engine.operation.Operation;
import ch03.engine.state.ExecutionState;
import ch03.model.ActivityInstance;
import ch03.model.ScopeInstance;
import ch03.model.VariableInstance;
import ch03.model.WorkflowInstance;
import ch03.timers.Timer;


/** 
 * interface for connecting persistent storage to the workflow instance execution updates.
 * @author Tom Baeyens 
 */
public interface Persistence {

  /** also must assign the id */
  void workStartWorkflowInstance(WorkflowInstance workflowInstance);

  /** also must assign the id */
  void workStartHandleMessage(ActivityInstance activityInstance, Map<String,TypedValue> messageData);

  /** implies variableInstanceValueUpdated for the initial value. 
   * also must assign the id */
  void variableInstanceCreated(VariableInstance variableInstance);

  /** variableInstance contains the new value */
  void variableInstanceValueUpdated(VariableInstance variableInstance, TypedValue oldValue);

  /** also must assign the id */
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

  void executionListenerAdded(ExternalAction externalAction);

  /** called before an activity instance is started and 
   * when the updates and operations given are in a consistent state to 
   * be saved for the purpose of recovery. 
   * The transaction will keep on executing afterwards till {@link #transactionEnd(WorkflowInstance)}
   * is called. */
  void workSave(WorkflowInstance workflowInstance, List<Operation> operations, List<ExternalAction> externalActions);

  /** No more work will be done by the engine for this workflow instance.
   * After this, the workflow instance is either ended or it will
   * wait for an external signal.  This could potentially take 
   * a long time.  After receiving the signal, the workflow 
   * instance could execute further. 
   * This is a good moment to update or overwrite the entire 
   * workflow instance data structure 
   * @param externalActions */
  void workEnd(WorkflowInstance workflowInstance, List<ExternalAction> externalActions);

  void executionListenerRemove(int executionListenerIndex, ExternalAction externalAction);
}
