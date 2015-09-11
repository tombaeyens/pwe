package ch03.workflow;

import ch03.engine.Context;
import ch03.engine.Controller;
import ch03.model.Activity;
import ch03.model.ActivityInstance;
import ch03.service.TaskService;


/**
 * @author Tom Baeyens
 */
public class TaskActivity extends Activity {

  @Override
  public void start(ActivityInstance activityInstance, Context context, Controller controller) {
    TaskService taskService = context.findExternally(TaskService.class);
    // TODO
    controller.onwards();
  }
}
