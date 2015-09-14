package ch03.engine.operation;

import ch03.engine.ContextImpl;
import ch03.engine.ControllerImpl;
import ch03.engine.EngineImpl;
import ch03.engine.state.Starting;
import ch03.model.Activity;
import ch03.model.ActivityInstance;
import ch03.util.Logger;


/**
 * @author Tom Baeyens
 */
public class StartActivity extends Operation {
  
  private static final Logger log = EngineImpl.log;

  public StartActivity(ActivityInstance activityInstance) {
    super(activityInstance);
  }
  
  @Override
  public void perform(EngineImpl engine, ContextImpl context, ControllerImpl controller) {
    controller.setState(Starting.INSTANCE);
    Activity activity = activityInstance.getActivity();
    log.debug("Starting [%s|%s|%s]", activity.getId(), activity.getTypeName(), activityInstance.getId());
    activity.start(activityInstance, engine.getContext(), engine.getController());
  }

  @Override
  public boolean isAsynchronous() {
    return activityInstance.getActivity().isAsynchronous();
  }
  
  public boolean requiresTransactionSave() {
    return true;
  }
}