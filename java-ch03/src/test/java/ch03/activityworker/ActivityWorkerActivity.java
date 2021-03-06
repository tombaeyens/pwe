package ch03.activityworker;

import java.util.Map;

import ch03.data.TypedValue;
import ch03.engine.Context;
import ch03.engine.Controller;
import ch03.model.Activity;
import ch03.model.ActivityInstance;


public class ActivityWorkerActivity extends Activity {
  
  @Override
  public void start(ActivityInstance activityInstance, Context context, Controller controller) {
    controller.addExternalAction(new ActivityWorkerNotification(activityInstance));
    controller.waitForExternalMessage();

    // External continuation has an engine defined reference
    // the activity could pass the engine defined reference externally 
    // as the flow moves outside the engine.
    // Then when the external asynchronous action is done, the 
    // external service has to call back the engine using this exernal continuation id
    
    // Alternatively, the external continuation is decorated with business 
    // data so that the external service later can find the appropriate 
    // external continuation based on the business data
  }

  @Override
  public void message(ActivityInstance activityInstance, Context context, Controller controller, Map<String, TypedValue> messageData) {
    // same impl as the super class, but for clarity we include it here as well
    context.writeOutputs(messageData);
    controller.onwards();
  }
}