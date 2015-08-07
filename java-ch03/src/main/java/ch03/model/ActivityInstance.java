package ch03.model;

import ch03.engine.Ended;
import ch03.engine.ExecutionState;



/**
 * @author Tom Baeyens
 */
public class ActivityInstance extends ScopeInstance {
  
  public Activity activity;
  
  public boolean isEnded() {
    return state.isEnded();
  }

  public void end() {
    state = new Ended();
  }
}
