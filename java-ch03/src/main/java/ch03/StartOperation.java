package ch03;


/**
 * @author Tom Baeyens
 */
public class StartOperation extends Operation {

  public StartOperation(ActivityInstance activityInstance) {
    super(activityInstance);
  }

  @Override
  public void perform(ExecutionController executionFlow) {
  }
}
