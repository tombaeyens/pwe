package ch03.taskservice;


/**
 * @author Tom Baeyens
 */
public interface TaskListener {

  void taskCompleted(Task task);
}
