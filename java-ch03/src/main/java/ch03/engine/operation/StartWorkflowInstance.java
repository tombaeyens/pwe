package ch03.engine.operation;

import java.util.List;
import java.util.Map;

import ch03.data.TypedValue;
import ch03.engine.Execution;
import ch03.engine.ExecutionContextImpl;
import ch03.engine.ExecutionControllerImpl;
import ch03.model.Activity;
import ch03.model.ScopeInstance;
import ch03.model.Trigger;
import ch03.model.Workflow;
import ch03.model.WorkflowInstance;


/**
 * @author Tom Baeyens
 */
public class StartWorkflowInstance extends Operation {

  Map<String, TypedValue> startData;
  List<Activity> startActivities;
  WorkflowInstance workflowInstance;
  
  public StartWorkflowInstance(WorkflowInstance workflowInstance, Map<String, TypedValue> startData, List<Activity> startActivities) {
    super(null);
    this.workflowInstance = workflowInstance;
    this.startData = startData;
    this.startActivities = startActivities;
  }
  
  @Override
  public ScopeInstance getActivityInstance() {
    return workflowInstance;
  }

  @Override
  public void perform(Execution execution, ExecutionContextImpl context, ExecutionControllerImpl controller) {
    Workflow workflow = workflowInstance.workflow;
    Trigger trigger = workflow.trigger;
    if (trigger!=null) {
      trigger.start(execution, startData);
    } else if (startData!=null) {
      for (String key: startData.keySet()) {
        TypedValue typedValue = startData.get(key);
        execution.setVariableInstanceValue(key, typedValue);
      }
    }
    if (startActivities==null) {
      startActivities = workflow.getStartActivities();
    }
    controller.startActivities(startActivities);
  }
}
