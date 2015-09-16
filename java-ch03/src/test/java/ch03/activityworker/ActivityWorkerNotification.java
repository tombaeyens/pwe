package ch03.activityworker;

import ch03.engine.Context;
import ch03.engine.ExternalAction;
import ch03.model.ActivityInstance;


public class ActivityWorkerNotification implements ExternalAction {
  
  ActivityInstance activityInstance;
  
  public ActivityWorkerNotification(ActivityInstance activityInstance) {
    this.activityInstance = activityInstance;
  }
  
  @Override
  public void executionEnded(Context context) {
    ActivityWorker activityWorker = context.getExternal(ActivityWorker.class);
    activityWorker.notify(activityInstance);
  }
}