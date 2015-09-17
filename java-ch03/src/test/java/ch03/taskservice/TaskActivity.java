package ch03.taskservice;

import java.util.Map;

import ch03.data.TypedValue;
import ch03.engine.Context;
import ch03.engine.Controller;
import ch03.model.Activity;
import ch03.model.ActivityInstance;


/**
 * @author Tom Baeyens
 */
public class TaskActivity extends Activity {

  @Override
  public void start(ActivityInstance activityInstance, Context context, Controller controller) {
    String title = context.getValue("title");
    
    Task task = new Task();
    task.setTitle(title);
    task.addTaskListener(new TaskDoneListener(activityInstance));
    
    controller.addExternalAction(new TaskCreateListener(task));
    controller.waitForExternalMessage();
  }

  @Override
  public void message(ActivityInstance activityInstance, Context context, Controller controller, Map<String, TypedValue> messageData) {
    // same impl as the super class, but for clarity we include it here as well
    context.writeOutputs(messageData);
    controller.onwards();
  }
}
