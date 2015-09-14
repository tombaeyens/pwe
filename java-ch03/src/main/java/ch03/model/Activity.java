package ch03.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch03.data.Condition;
import ch03.data.TypedValue;
import ch03.engine.Context;
import ch03.engine.Controller;

/**
 * @author Tom Baeyens
 */
public class Activity extends Scope {

  public Condition condition;
  public Scope parent;
  public List<Transition> incomingTransitions = new ArrayList<>();
  public List<Transition> outgoingTransitions = new ArrayList<>();
  
  public Transition createTransitionTo(Activity destination) {
    Transition transition = new Transition();
    transition.from = this;
    transition.from.outgoingTransitions.add(transition);
    transition.to = destination;
    transition.to.incomingTransitions.add(transition);
    return transition;
  }
  
  /** @param activityInstance is part of the read-only data structure representing the workflow instance.
   * @param context provides read/write access to variables, read access to configuration and external context.
   * @param controller provides primitive operations to control the flow of execution. */
  public void start(ActivityInstance activityInstance, Context context, Controller controller) {
    onwards(activityInstance, context, controller);
  }

  public void handleMessage(ActivityInstance activityInstance, Context context, Controller controller, Map<String,TypedValue> messageData) {
    onwards(activityInstance, context, controller);
  }

  public void onwards(ActivityInstance activityInstance, Context context, Controller controller) {
    List<Transition> outgoingTransitions = activityInstance.activity.outgoingTransitions;
    controller.takeTransitions(outgoingTransitions);
  }
  
  public String getTypeName() {
    return getClass().getSimpleName();
  }

  public String toString() {
    return "["+id+"|"+getTypeName()+"]"; 
  }

  @Override
  public boolean isActivity() {
    return true;
  }

  @Override
  public boolean isWorkflow() {
    return false;
  }

  public boolean isAsynchronous() {
    return false;
  }

  
  public Condition getCondition() {
    return condition;
  }

  
  public void setCondition(Condition condition) {
    this.condition = condition;
  }

  
  public Scope getParent() {
    return parent;
  }

  
  public void setParent(Scope parent) {
    this.parent = parent;
  }

  
  public List<Transition> getIncomingTransitions() {
    return incomingTransitions;
  }

  
  public void setIncomingTransitions(List<Transition> incomingTransitions) {
    this.incomingTransitions = incomingTransitions;
  }

  
  public List<Transition> getOutgoingTransitions() {
    return outgoingTransitions;
  }

  
  public void setOutgoingTransitions(List<Transition> outgoingTransitions) {
    this.outgoingTransitions = outgoingTransitions;
  }
}
