package ch03.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch03.data.Condition;
import ch03.data.InputExpression;
import ch03.data.OutputExpression;
import ch03.data.TypedValue;
import ch03.engine.Context;
import ch03.engine.Controller;
import ch03.engine.Listener;
import ch03.timers.Timer;

/**
 * @author Tom Baeyens
 */
public class Activity extends Scope {

  public Condition condition;
  public Scope parent;
  public List<Transition> incomingTransitions;
  public List<Transition> outgoingTransitions;
  
  /** @param activityInstance is part of the read-only data structure representing the workflow instance.
   * @param context provides read/write access to variables, read access to configuration and external context.
   * @param controller provides primitive operations to control the flow of execution. */
  public void start(ActivityInstance activityInstance, Context context, Controller controller) {
    onwards(activityInstance, context, controller);
  }

  public void message(ActivityInstance activityInstance, Context context, Controller controller, Map<String,TypedValue> messageData) {
    context.writeOutputs(messageData);
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
  
  public Transition createTransitionTo(Activity destination) {
    Transition transition = new Transition();
    transition.from = this;
    transition.from.addOutgoingTransition(transition);
    transition.to = destination;
    transition.to.addIncomingTransition(transition);
    return transition;
  }
  
  private void addIncomingTransition(Transition transition) {
    if (incomingTransitions==null) {
      incomingTransitions = new ArrayList<>();
    }
    incomingTransitions.add(transition);
  }

  private void addOutgoingTransition(Transition transition) {
    if (outgoingTransitions==null) {
      outgoingTransitions = new ArrayList<>();
    }
    outgoingTransitions.add(transition);
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

  public boolean hasOutgoingTransitions() {
    return outgoingTransitions!=null && !outgoingTransitions.isEmpty();
  }

  @Override
  public boolean isActivity() {
    return true;
  }

  @Override
  public boolean isWorkflow() {
    return false;
  }
  
  @Override
  public Activity id(String id) {
    return (Activity) super.id(id);
  }

  @Override
  public Activity configurationValue(String key, Object value) {
    return (Activity) super.configurationValue(key, value);
  }

  @Override
  public Activity configurationTypedValue(String key, TypedValue typedValue) {
    return (Activity) super.configurationTypedValue(key, typedValue);
  }
  
  @Override
  public Activity inputParameter(String key, InputExpression inputExpression) {
    return (Activity) super.inputParameter(key, inputExpression);
  }

  @Override
  public Activity outputParameter(String key, OutputExpression outputExpression) {
    return (Activity) super.outputParameter(key, outputExpression);
  }

  @Override
  public Activity activity(Activity activity) {
    return (Activity) super.activity(activity);
  }

  @Override
  public Activity autoStartActivity(Activity activity) {
    return (Activity) super.autoStartActivity(activity);
  }

  @Override
  public Activity variable(Variable variable) {
    return (Activity) super.variable(variable);
  }

  @Override
  public Activity listener(String eventName, Listener listener) {
    return (Activity) super.listener(eventName, listener);
  }

  @Override
  public Activity timer(Timer timer) {
    return (Activity) super.timer(timer);
  }
}
