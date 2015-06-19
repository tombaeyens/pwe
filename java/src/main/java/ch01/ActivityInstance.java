package ch01;

import java.util.Date;


public class ActivityInstance {

  WorkflowInstance workflowInstance;
  Activity activity;
  Date end;
  
  public ActivityInstance(Activity activity, WorkflowInstance workflowInstance) {
    this.activity = activity;
    this.workflowInstance = workflowInstance;
  }

  public void execute() {
    activity.execute(this);
  }

  public void end() {
    end = new Date();
    for (Transition transition: activity.outgoingTransitions) {
      take(transition);
    }
  }

  public void take(Transition transition) {
    end = new Date();
    workflowInstance.execute(transition.to);
  }
}
