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

  /** ends this activity instances and propagates the execution over 
   * all the outgoing transitions. */
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
}
