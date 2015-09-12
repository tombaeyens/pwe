package ch03.concurrency.infrastructure;

import java.util.Map;

import ch03.data.TypedValue;
import ch03.engine.Context;
import ch03.engine.Controller;
import ch03.model.Activity;
import ch03.model.ActivityInstance;


/**
 * @author Tom Baeyens
 */
public class ExternalAsync extends Activity {
  
  @Override
  public void start(ActivityInstance activityInstance, Context context, Controller controller) {
    // create notification 
    // TODO make sure the notification only gets executed after workflow state is persisted
    //  --> maybe pass the notification as a parameter to the .waitForExternalMessage() ?
    
    controller.waitForExternalMessage();
  }
  
  @Override
  public void handleMessage(ActivityInstance activityInstance, Context context, Controller controller, Map<String, TypedValue> messageData) {
    // external message received
    
    controller.onwards();
  }

  @Override
  public boolean isAsynchronous() {
    return true;
  }
}
