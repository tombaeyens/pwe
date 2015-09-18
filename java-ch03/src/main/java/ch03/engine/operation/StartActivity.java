package ch03.engine.operation;

import java.util.Map;

import ch03.data.TypedValue;
import ch03.engine.ContextImpl;
import ch03.engine.ControllerImpl;
import ch03.engine.EngineImpl;
import ch03.engine.context.MapContext;
import ch03.engine.state.Starting;
import ch03.model.Activity;
import ch03.model.ActivityInstance;
import ch03.util.Logger;


/**
 * @author Tom Baeyens
 */
public class StartActivity extends Operation {
  
  private static final Logger log = EngineImpl.log;

  Map<String, TypedValue> activityStartData;
  
  public StartActivity(ActivityInstance activityInstance, Map<String, TypedValue> activityStartData) {
    super(activityInstance);
    this.activityStartData = activityStartData;
  }
  
  @Override
  public void perform(EngineImpl engine, ContextImpl context, ControllerImpl controller) {
    Activity activity = activityInstance.getActivity();
    log.debug("Starting [%s|%s|%s]", activity.getId(), activity.getTypeName(), activityInstance.getId());
    
    MapContext startDataContext = null;
    if (activityStartData!=null && !activityStartData.isEmpty()) {
      if (activity.getInputParameters()!=null) {
        startDataContext = new MapContext("startData", activityStartData);
        // adding the start data subcontext after the subcontext context
        context.addSubContext(0, startDataContext);
      } else {
        context.setVariableInstances(activityStartData);
      }
    } 
    
    activity.start(activityInstance, engine.getContext(), engine.getController());
    if (startDataContext!=null) {
      context.removeSubContext(startDataContext);
    }
  }

  public boolean requiresTransactionSave() {
    return true;
  }
}