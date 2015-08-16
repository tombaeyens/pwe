package ch03.engine.operation;

import ch03.engine.Execution;
import ch03.engine.state.Starting;
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
    execution.setState(Starting.INSTANCE);
    activityInstance.activity.start(execution);
  }
}