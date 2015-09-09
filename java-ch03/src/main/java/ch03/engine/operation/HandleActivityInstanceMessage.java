package ch03.engine.operation;

import java.util.Map;

import ch03.data.TypedValue;
import ch03.engine.Execution;
import ch03.engine.ExecutionContextImpl;
import ch03.engine.ExecutionControllerImpl;
import ch03.model.ActivityInstance;


/**
 * @author Tom Baeyens
 */
public class HandleActivityInstanceMessage extends Operation {

  Map<String, TypedValue> messageData;

  public HandleActivityInstanceMessage(ActivityInstance activityInstance, Map<String, TypedValue> messageData) {
    super(activityInstance);
    this.messageData = messageData;
  }

  @Override
  public void perform(Execution executionFlow, ExecutionContextImpl context, ExecutionControllerImpl controller) {
    // TODO
  }
}
