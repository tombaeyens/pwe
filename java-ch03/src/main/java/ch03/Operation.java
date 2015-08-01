package ch03;


public abstract class Operation {
  
  ActivityInstance activityInstance;
  
  public abstract void perform(ExecutionController executionFlow);

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