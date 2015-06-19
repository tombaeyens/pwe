package ch01;
import ch01.Activity;
import ch01.ActivityInstance;


/** An activity type that logs a message on the console and 
 * propagates the execution immediately. */
public class Wait extends Activity {

  public Wait(String id) {
    super(id);
  }

  @Override
  public void start(ActivityInstance activityInstance) {
    System.out.println("Waiting in "+id+" till the activity instance is ended externally");
  }
}

