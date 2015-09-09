package ch03.engine;

import java.util.List;

import ch03.model.Activity;
import ch03.model.ScopeInstance;
import ch03.model.Transition;

/**
 * @author Tom Baeyens
 */
public interface ExecutionController {

  /** starts the given scope */
  void startActivity(Activity activity);

  /** starts the given scope */
  void startActivity(Activity activity, ScopeInstance parentScopeInstance);

  void startActivities(List<Activity> activities);

  void waitForExternalMessage();
  
  void takeTransitions(List<Transition> transitionsToTake);
  
  void takeTransition(Transition transition);
  
  void onwards();

  void notifyParentActivityInstanceEnded();
}