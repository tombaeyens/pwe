package ch03;


/**
 * @author Tom Baeyens
 */
public class StartActivity extends Operation {

  public StartActivity(ActivityInstance activityInstance) {
    super(activityInstance);
  }
  
  @Override
  public void perform(ExecutionController controller) {
    activityInstance.activity.start(controller);
  }
}