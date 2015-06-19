package ch01;

import java.util.ArrayList;
import java.util.List;


/** One execution of a {@link Workflow}. */
public class WorkflowInstance {
  
  Workflow workflow;
  List<ActivityInstance> activityInstances = new ArrayList<>();
  
  public WorkflowInstance(Workflow workflow) {
    this.workflow = workflow;
  }

  /** Start executing this workflow instance by executing all the {@link Workflow#startActivities}.
   * 
   * This method will only return as soon as the workflow instance (and all activity instance inside) 
   * have reached a wait state.
   * 
   * This method should only be called once. */
  public void start() {
    assert activityInstances.size()==0;
    for (Activity startActivity: workflow.startActivities) {
      execute(startActivity);
    }
  }

  /** executes an activity by creating a new activity instance and starting the activity.
   * This is normally called by the workflow engine code itself. 
   * After the activity is started, the propagation of the execution flow is delegated 
   * to the activity. */ 
  public void execute(Activity activity) {
    ActivityInstance activityInstance = new ActivityInstance(activity, this);
    activityInstances.add(activityInstance);
    activity.start(activityInstance);
  }
}
