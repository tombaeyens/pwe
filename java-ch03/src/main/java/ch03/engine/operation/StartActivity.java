package ch03.engine.operation;

import ch03.engine.ContextImpl;
import ch03.engine.ControllerImpl;
import ch03.engine.Engine;
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
  public void perform(Engine engine, ContextImpl context, ControllerImpl controller) {
    controller.setState(Starting.INSTANCE);
    ActivityInstance activityInstance = (ActivityInstance) scopeInstance;
    Activity activity = (Activity) scopeInstance.getScope();
    activity.start(activityInstance, engine.getContext(), engine.getController());
  }
}