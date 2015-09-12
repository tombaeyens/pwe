package ch03.engine.operation;

import java.util.List;

import ch03.engine.ContextImpl;
import ch03.engine.ControllerImpl;
import ch03.engine.Engine;
import ch03.model.Activity;
import ch03.model.Workflow;
import ch03.model.WorkflowInstance;


/**
 * @author Tom Baeyens
 */
public class StartWorkflow extends Operation {

  List<Activity> startActivities;
  WorkflowInstance workflowInstance;
  
  public StartWorkflow(WorkflowInstance workflowInstance, List<Activity> startActivities) {
    super(workflowInstance);
    this.workflowInstance = workflowInstance;
    this.startActivities = startActivities;
  }
  
  @Override
  public void perform(Engine engine, ContextImpl context, ControllerImpl controller) {
    Workflow workflow = workflowInstance.workflow;
    if (startActivities==null) {
      startActivities = workflow.getStartActivities();
    }
    controller.startActivityInstances(startActivities);
  }
}
