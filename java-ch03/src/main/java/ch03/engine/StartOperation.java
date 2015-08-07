package ch03.engine;

import ch03.model.ActivityInstance;


/**
 * @author Tom Baeyens
 */
public class StartOperation extends Operation {

  public StartOperation(ActivityInstance activityInstance) {
    super(activityInstance);
  }

  @Override
  public void perform(Execution executionFlow) {
  }
}
