package ch03.taskservice;

import ch03.engine.Context;
import ch03.engine.ExternalAction;


/**
 * @author Tom Baeyens
 */
public class TaskCreateListener implements ExternalAction {

  Task task;
  
  public TaskCreateListener(Task task) {
    this.task = task;
  }

  @Override
  public void executionEnded(Context context) {
    TaskService taskService = context.getExternal(TaskService.class);
    taskService.createTask(task);
  }

}
