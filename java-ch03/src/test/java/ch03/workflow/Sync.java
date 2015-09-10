package ch03.workflow;

import ch03.engine.ExecutionContext;
import ch03.engine.ExecutionController;
import ch03.model.Activity;
import ch03.model.ActivityInstance;


/**
 * @author Tom Baeyens
 */
public class Sync extends Activity {
  
  @Override
  public void start(ActivityInstance activityInstance, ExecutionContext context, ExecutionController controller) {
    // do some synchronous automatic stuff
    controller.onwards();
  }
}
