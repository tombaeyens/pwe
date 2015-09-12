package ch03.concurrency.infrastructure;

import ch03.engine.Context;
import ch03.engine.Controller;
import ch03.model.Activity;
import ch03.model.ActivityInstance;


/**
 * @author Tom Baeyens
 */
public class InternalAsync extends Activity {
  
  @Override
  public void start(ActivityInstance activityInstance, Context context, Controller controller) {
    // Do internal async stuff here.
    // Activities should be asynchronous if the things done  
    // here take too long for the client thread to wait on it.
    
    controller.onwards();
  }

  @Override
  public boolean isAsynchronous() {
    // Activities can decide at runtime if they 
    // are synchronous or asynchronous.
    // This activity always is asynchronous.
    return true;
  }
}
