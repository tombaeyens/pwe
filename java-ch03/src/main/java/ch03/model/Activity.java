package ch03.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ch03.data.Condition;
import ch03.data.InputExpression;
import ch03.data.OutputExpression;
import ch03.engine.Execution;

/**
 * @author Tom Baeyens
 */
public class Activity extends Scope {

  public Condition condition;
  public Map<String,InputExpression> inputBindings = new LinkedHashMap<>();
  public Map<String,OutputExpression> outputBindings = new LinkedHashMap<>();
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
  
  public Activity inputBinding(String key, InputExpression inputExpression) {
    inputBindings.put(key, inputExpression);
    return this;
  }

  public Activity outputBinding(String key, OutputExpression outputExpression) {
    outputBindings.put(key, outputExpression);
    return this;
  }

  public void start(Execution execution) {
  }
}
