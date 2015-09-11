package ch03.workflow;

import java.util.ArrayList;
import java.util.List;

import ch03.engine.Context;
import ch03.engine.Controller;
import ch03.model.Activity;
import ch03.model.ActivityInstance;
import ch03.model.Transition;


/**
 * @author Tom Baeyens
 */
public class BpmnActivity extends Activity {

  /** takes the outgoing applicable transitions if there are any 
   * or propagates the execution flow to the parent otherwise */
  public void onwards(ActivityInstance activityInstance,
                      Context context,
                      Controller controller) {
    List<Transition> transitionsToTake = new ArrayList<>();
    Activity activity = activityInstance.getActivity();
    for (Transition outTransition: activity.outTransitions) {
      if (context.isConditionMet(outTransition.condition)) {
        transitionsToTake.add(outTransition);
      }
    }
    if (!transitionsToTake.isEmpty()) {
      controller.takeTransitions(transitionsToTake);
    } else {
      controller.onwards();
    }
  }
  
}
