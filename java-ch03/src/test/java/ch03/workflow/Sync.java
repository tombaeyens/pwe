package ch03.workflow;

import ch03.engine.Context;
import ch03.engine.Controller;
import ch03.model.Activity;
import ch03.model.ActivityInstance;


/**
 * @author Tom Baeyens
 */
public class Sync extends Activity {
  
  @Override
  public void start(ActivityInstance activityInstance, Context context, Controller controller) {
    // do some synchronous automatic stuff
    controller.onwards();
  }
}
