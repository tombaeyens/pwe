package ch03;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ch03.data.Condition;
import ch03.data.Expression;


public class Scope {
  
  Condition condition;
  Map<String,Object> configuration = new LinkedHashMap<>();
  Map<String,Expression> inputBindings = new LinkedHashMap<>();
  Map<String,Expression> outputBindings = new LinkedHashMap<>();
  Scope parent;
  List<Scope> activities = new ArrayList<>();
  List<Transition> inTransitions = new ArrayList<>();
  List<Transition> outTransitions = new ArrayList<>();
  
  public Transition createTransitionTo(Scope destination) {
    Transition transition = new Transition();
    transition.from = this;
    transition.from.outTransitions.add(transition);
    transition.to = destination;
    transition.to.inTransitions.add(transition);
    return transition;
  }

  public ScopeInstance createInstance() {
    return new ScopeInstance();
  }

  public void start(ExecutionController controller) {
  }
}
