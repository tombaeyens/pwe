package ch03.model;

import java.util.ArrayList;
import java.util.List;

import ch03.data.Condition;
import ch03.engine.ExecutionContext;
import ch03.engine.ExecutionController;

/**
 * @author Tom Baeyens
 */
public class Activity extends Scope {

  public Condition condition;
  public Scope parent;
  public List<Transition> inTransitions = new ArrayList<>();
  public List<Transition> outTransitions = new ArrayList<>();
  
  public Transition createTransitionTo(Activity destination) {
    Transition transition = new Transition();
    transition.from = this;
    transition.from.outTransitions.add(transition);
    transition.to = destination;
    transition.to.inTransitions.add(transition);
    return transition;
  }
  
  /** @param activityInstance is part of the read-only data structure representing the workflow instance.
   * @param context provides read/write access to variables, read access to configuration and external context.
   * @param controller provides primitive operations to control the flow of execution. */
  public void start(ActivityInstance activityInstance, ExecutionContext context, ExecutionController controller) {
  }

  public void onwards(ActivityInstance activityInstance, ExecutionContext context, ExecutionController controller) {
    List<Transition> activatedOutgoingTransitions = context.getOutgoingTransitionsMeetingCondition();
    if (!activatedOutgoingTransitions.isEmpty()) {
      controller.takeTransitions(activatedOutgoingTransitions);
    } else {
      controller.notifyParentActivityInstanceEnded();
    }
  }
}
