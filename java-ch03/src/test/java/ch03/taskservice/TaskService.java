package ch03.taskservice;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Tom Baeyens
 */
public class TaskService {

  List<Task> tasks = new ArrayList<>();

  public void createTask(Task task) {
    tasks.add(task);
  }

  public List<Task> getTasks() {
    return tasks;
  }
}
