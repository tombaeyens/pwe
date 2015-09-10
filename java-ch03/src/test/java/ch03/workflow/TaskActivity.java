package ch03.workflow;

import ch03.engine.ExecutionContext;
import ch03.engine.ExecutionController;
import ch03.model.Activity;
import ch03.model.ActivityInstance;
import ch03.service.TaskService;


/**
 * @author Tom Baeyens
 */
public class TaskActivity extends Activity {

  @Override
  public void start(ActivityInstance activityInstance, ExecutionContext context, ExecutionController controller) {
    TaskService taskService = context.findExternally(TaskService.class);
    // TODO
    controller.onwards();
  }
}
