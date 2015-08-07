package ch03.engine;

import ch03.model.ActivityInstance;


/**
 * @author Tom Baeyens
 */
public class StartActivity extends Operation {

  public StartActivity(ActivityInstance activityInstance) {
    super(activityInstance);
  }
  
  @Override
  public void perform(Execution execution) {
    activityInstance.activity.start(execution);
  }
}