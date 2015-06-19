package ch01;
import ch01.Activity;
import ch01.ActivityInstance;


/** An activity type that logs a message on the console and 
 * propagates the execution immediately. */
public class Automatic extends Activity {

  public Automatic(String id) {
    super(id);
  }

  @Override
  public void start(ActivityInstance activityInstance) {
    System.out.println("Ending "+id+" and continuing immediately");
    activityInstance.end();
  }
}

