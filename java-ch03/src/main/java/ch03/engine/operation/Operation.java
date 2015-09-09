package ch03.engine.operation;

import ch03.engine.Execution;
import ch03.engine.ExecutionContextImpl;
import ch03.engine.ExecutionControllerImpl;
import ch03.model.ActivityInstance;


public abstract class Operation {
  
  ActivityInstance activityInstance;
  
  public abstract void perform(Execution execution, ExecutionContextImpl context, ExecutionControllerImpl controller);

  public Operation(ActivityInstance activityInstance) {
    this.activityInstance = activityInstance;
  }
  
  public ActivityInstance getActivityInstance() {
    return activityInstance;
  }
  
  public boolean isSynchonrous() {
    return true;
  }
}