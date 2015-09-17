package ch03.activityworker;

import java.util.ArrayList;
import java.util.List;

import ch03.model.ActivityInstance;


public class ActivityWorker {

  List<String> workDone = new ArrayList<>();
  
  /** Invoked after the workflow instance state is persisted.
   * This notification transfers the flow of control from the 
   * workflow engine to the external activity worker.
   * 
   * For this demo/test, this is done synchronous, but in practice this is 
   * best done asynchronous. Otherwise the engine thread performing 
   * the notification is blocked unnecessary.  The engine already 
   * may perform the notifications asynchronously, but still it 
   * might be a good idea because typically this notification will 
   * be done over HTTP and it's better to free up the connection and 
   * server side thread. 
   *  
   * When the activity worker is done, it will pass the control 
   * flow back to the workflow engine by sending a message. 
   * 
   * As an alternative, instead of performing the work in response to 
   * the notification, the work to be done can be stored in a 
   * collection/table and a set of competing consumers can pull and 
   * perform the work from that collection/table. */
  public void notify(ActivityInstance activityInstance) {
    workDone.add("Worked activity "+activityInstance.getId());
    activityInstance.message();
  }
  
  public List<String> getWorkDone() {
    return workDone;
  }
}
