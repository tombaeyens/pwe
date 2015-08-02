package ch03;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ch03.data.Condition;
import ch03.data.InputExpression;

/**
 * @author Tom Baeyens
 */
public class Activity extends Scope {

  Condition condition;
  Map<String,InputExpression> inputBindings = new LinkedHashMap<>();
  Map<String,InputExpression> outputBindings = new LinkedHashMap<>();
  Scope parent;
  List<Transition> inTransitions = new ArrayList<>();
  List<Transition> outTransitions = new ArrayList<>();
  
  public Transition createTransitionTo(Activity destination) {
    Transition transition = new Transition();
    transition.from = this;
    transition.from.outTransitions.add(transition);
    transition.to = destination;
    transition.to.inTransitions.add(transition);
    return transition;
  }

  public void start(ExecutionController controller) {
  }
}
