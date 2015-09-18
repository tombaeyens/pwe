package ch03.engine;

import java.util.List;

import ch03.model.Activity;
import ch03.model.ActivityInstance;
import ch03.model.ScopeInstance;
import ch03.model.Transition;

/**
 * @author Tom Baeyens
 */
public interface Controller {

  /** starts a nested activity instance for the given activity in the current scope. */
  ActivityInstance startActivityInstance(Activity activity);

  /** starts nested activity instances for the given activities in the current scope. */
  List<ActivityInstance> startActivityInstances(List<Activity> activities);

  /** starts an activity instance for the given activity nested in the given parentScopeInstance */
  ActivityInstance startActivityInstance(Activity activity, ScopeInstance parentScopeInstance);

  /** action performed after the current execution flow has ended.
   * This is the best way to attach notifications that continue the flow.  Cause if you 
   * perform those notifications inside the activity, then potetentially the message to 
   * continue will arrive before the state is persisted. */
  void addExternalAction(ExternalAction externalAction);
  
  void waitForExternalMessage();
  
  List<ActivityInstance> takeTransitions(List<Transition> transitionsToTake);
  
  ActivityInstance takeTransition(Transition transition);
  
  void onwards();

  void notifyParentFlowEnded();
}