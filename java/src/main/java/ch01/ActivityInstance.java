package ch01;

import java.util.Date;


/** One execution of an {@link Activity}. */
public class ActivityInstance {

  WorkflowInstance workflowInstance;
  Activity activity;
  Date end;
  
  public ActivityInstance(Activity activity, WorkflowInstance workflowInstance) {
    this.activity = activity;
    this.workflowInstance = workflowInstance;
  }

  /** ends this activity instances and propagates the workflow execution 
   * forward until no more work can be done now.  The propagation the 
   * execution means that all the outgoing transitions are taken. */
  public void end() {
    end = new Date();
    for (Transition transition: activity.outgoingTransitions) {
      take(transition);
    }
  }

  /** ends this activity instance and propagates the execution of the 
   * given transition */
  public void take(Transition transition) {
    end = new Date();
    workflowInstance.execute(transition.to);
  }

  
  public WorkflowInstance getWorkflowInstance() {
    return workflowInstance;
  }

  
  public Activity getActivity() {
    return activity;
  }

  
  public Date getEnd() {
    return end;
  }
}
