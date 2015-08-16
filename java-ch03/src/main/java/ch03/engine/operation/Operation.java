package ch03.engine.operation;

import ch03.engine.Execution;
import ch03.model.ActivityInstance;
import ch03.model.ScopeInstance;


public abstract class Operation {
  
  ActivityInstance activityInstance;
  
  public abstract void perform(Execution execution);

  public Operation(ActivityInstance activityInstance) {
    this.activityInstance = activityInstance;
  }
  
  public ScopeInstance getScopeInstance() {
    return activityInstance;
  }
  
  public boolean isSynchonrous() {
    return true;
  }
}