package ch03.engine.operation;

import java.util.Map;

import ch03.data.TypedValue;
import ch03.engine.ContextImpl;
import ch03.engine.ControllerImpl;
import ch03.engine.Engine;
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
  public void perform(Engine executionFlow, ContextImpl context, ControllerImpl controller) {
    // TODO
  }
  
  public String toString() {
    String simpleName = getClass().getSimpleName();
    return messageData==null ? simpleName : simpleName+"("+messageData+")";
  }
}
