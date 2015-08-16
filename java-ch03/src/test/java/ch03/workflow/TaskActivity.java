package ch03.workflow;

import ch03.engine.Execution;
import ch03.engine.context.Context;
import ch03.model.Activity;
import ch03.service.TaskService;


/**
 * @author Tom Baeyens
 */
public class TaskActivity extends Activity {

  @Override
  public void start(Execution execution) {
    TaskService taskService = execution.findInExternalContext(TaskService.class);
    
    execution.onwards();
  }
  
}
