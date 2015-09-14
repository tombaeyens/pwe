package ch03.engine.operation;

import java.util.Map;

import ch03.data.TypedValue;
import ch03.engine.ContextImpl;
import ch03.engine.ControllerImpl;
import ch03.engine.EngineImpl;
import ch03.model.ActivityInstance;


/**
 * @author Tom Baeyens
 */
public class HandleMessage extends Operation {

  Map<String, TypedValue> messageData;

  public HandleMessage(ActivityInstance activityInstance, Map<String, TypedValue> messageData) {
    super(activityInstance);
    this.messageData = messageData;
  }

  @Override
  public void perform(EngineImpl engine, ContextImpl context, ControllerImpl controller) {
  }
  
  public String toString() {
    String simpleName = getClass().getSimpleName();
    return messageData==null ? simpleName : simpleName+"("+messageData+")";
  }
}
