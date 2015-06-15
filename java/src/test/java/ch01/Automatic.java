package ch01;
import ch01.Activity;
import ch01.ActivityInstance;


public class Automatic extends Activity {

  public Automatic(String id) {
    super(id);
  }

  @Override
  public void execute(ActivityInstance activityInstance) {
    System.out.println("Executing "+id);
    activityInstance.end();
  }
}

