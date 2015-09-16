package ch03.service;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Tom Baeyens
 */
public class Task {

  String title;
  List<TaskListener> taskListeners = new ArrayList<>();
  
  public String getTitle() {
    return title;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }
  
  public List<TaskListener> getTaskListeners() {
    return taskListeners;
  }
  
  public void setTaskListeners(List<TaskListener> taskListeners) {
    this.taskListeners = taskListeners;
  }
  
  public void addTaskListener(TaskListener taskListener) {
    taskListeners.add(taskListener);
  }
}
