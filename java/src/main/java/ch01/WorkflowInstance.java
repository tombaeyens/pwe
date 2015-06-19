package ch01;

import java.util.ArrayList;
import java.util.List;


public class WorkflowInstance {
  
  Workflow workflow;
  List<ActivityInstance> activityInstances = new ArrayList<>();
  
  public WorkflowInstance(Workflow workflow) {
    this.workflow = workflow;
  }

  public void start() {
    for (Activity startActivity: workflow.startActivities) {
      execute(startActivity);
    }
  }

  public void execute(Activity activity) {
    ActivityInstance activityInstance = new ActivityInstance(activity, this);
    activityInstances.add(activityInstance);
    activity.execute(activityInstance);
  }
}
