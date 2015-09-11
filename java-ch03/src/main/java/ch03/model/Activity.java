package ch03.model;

import java.util.ArrayList;
import java.util.List;

import ch03.data.Condition;
import ch03.engine.Context;
import ch03.engine.Controller;

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
  public void start(ActivityInstance activityInstance, Context context, Controller controller) {
  }

  public void onwards(ActivityInstance activityInstance, Context context, Controller controller) {
    List<Transition> outgoingTransitions = activityInstance.activity.outTransitions;
    controller.takeTransitions(outgoingTransitions);
  }

  @Override
  public boolean isActivity() {
    return true;
  }

  @Override
  public boolean isWorkflow() {
    return false;
  }
}
