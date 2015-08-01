package ch03;

import java.util.List;



public class Trigger extends Scope {
  
  List<Activity> startActivities;

  public void start(ExecutionController controller) {
    List<Activity> startActivities = (List<Activity>) controller.getValue("startActivities");
    if (startActivities==null) {
      startActivities = this.startActivities;
    }
    
    // move the controller to the workflow instance
    controller.scopeInstance = controller.scopeInstance.parent;
    
    controller.startActivities(startActivities);
  }
}
