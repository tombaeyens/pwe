package ch03.engine.operation;

import ch03.engine.Execution;
import ch03.engine.ExecutionContextImpl;
import ch03.engine.ExecutionControllerImpl;
import ch03.engine.state.Starting;
import ch03.model.Activity;
import ch03.model.ActivityInstance;


/**
 * @author Tom Baeyens
 */
public class StartActivity extends Operation {

  public StartActivity(ActivityInstance activityInstance) {
    super(activityInstance);
  }
  
  @Override
  public void perform(Execution execution, ExecutionContextImpl context, ExecutionControllerImpl controller) {
    controller.setState(Starting.INSTANCE);
    execution.getInputs();
    ActivityInstance activityInstance = (ActivityInstance) scopeInstance;
    Activity activity = (Activity) scopeInstance.getScope();
    activity.start(activityInstance, execution.getExecutionContext(), execution.getExecutionController());
    execution.setOutputs();
  }
}