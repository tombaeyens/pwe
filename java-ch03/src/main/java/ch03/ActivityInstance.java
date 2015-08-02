package ch03;



/**
 * @author Tom Baeyens
 */
public class ActivityInstance extends ScopeInstance {
  
  Activity activity;
  
  public ActivityInstance(Activity activity, ScopeInstance parentScopeInstance, ExecutionState executionState) {
    this.scope = activity;
    this.activity = activity;
    this.parent = parentScopeInstance;
    this.state = executionState;
  }

  public boolean isEnded() {
    return state.isEnded();
  }

  public void end() {
    state = new Ended();
  }
}
