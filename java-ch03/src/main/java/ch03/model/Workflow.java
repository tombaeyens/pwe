package ch03.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch03.data.TypedValue;
import ch03.engine.WorkflowInstancePersistence;


/**
 * @author Tom Baeyens
 */
public class Workflow extends Scope {
  
  public Trigger trigger;
  public List<WorkflowInstancePersistence> listeners = new ArrayList<>();
  public List<Activity> startActivities = null;

  public List<Activity> getStartActivities() {
    if (this.startActivities==null) {
      // I recall reading that this kind of initialization synchronization is not 100% threadsafe.
      // I don't recall what the proper solution was
      // But I think it will be sufficient
      synchronized (this) {
        List<Activity> startActivities = new ArrayList<>();
        for (Activity activity : activities.values()) {
          if (activity.inTransitions.isEmpty()) {
            startActivities.add(activity);
          }
        }
        this.startActivities = startActivities;
      }
    }
    return startActivities;
  }

}
