package ch03.concurrency.infrastructure;

import ch03.engine.Context;
import ch03.engine.Controller;
import ch03.model.Activity;
import ch03.model.ActivityInstance;


/** internal synchronous test activity 
 * @author Tom Baeyens
 */
public class InternalSync extends Activity {

  @Override
  public void start(ActivityInstance activityInstance, Context context, Controller controller) {
    // Do internal sync stuff
    // Activities should be synchronous if the things done  
    // here take are fast enough so that the the client 
    // thread can be blocked.
    // This way you're sure that this internal sync stuff is done 
    // before the thread of the client is returned.

    controller.onwards();
  }
}
