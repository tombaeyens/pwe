package ch03.workflow;

import ch03.engine.Execution;
import ch03.model.Activity;


/** external activity (wait state) */
/**
 * @author Tom Baeyens
 */
public class Async extends Activity {

  @Override
  public void start(Execution execution) {
    
    
    execution.waitForExternalMessage();

    // External continuation has an engine defined reference
    // the activity could pass the engine defined reference externally 
    // as the flow moves outside the engine.
    // Then when the external asynchronous action is done, the 
    // external service has to call back the engine using this exernal continuation id
    
    // Alternatively, the external continuation is decorated with business 
    // data so that the external service later can find the appropriate 
    // external continuation based on the business data
  }

}
