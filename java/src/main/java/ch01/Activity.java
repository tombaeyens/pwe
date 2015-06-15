package ch01;
import java.util.ArrayList;
import java.util.List;


public class Activity {

  String id;
  List<Activity> activities = new ArrayList<>();
  List<Transition> transitions = new ArrayList<>(); // outgoing transitions
  
  public Activity(String id) {
    this.id = id;
  }

  public Activity transition(String fromId, String toId) {
    Activity from = findActivity(fromId);
    Activity to = findActivity(toId);
    from.transitions.add(new Transition(from, to));
    return this;
  }

  public Activity activity(Activity activity) {
    activities.add(activity);
    return this;
  }
  
  public Activity findActivity(String activityId) {
    for (Activity a: activities) {
      if (activityId.equals(a.id)) {
        return a;
      }
    }
    return null;
  }

  public ActivityInstance start() {
    ActivityInstance activityInstance = new ActivityInstance(this, null);
    execute(activityInstance);
    return activityInstance;
  }

  public void execute(ActivityInstance activityInstance) {
    List<Activity> startActivities = new ArrayList<>(activities);
    for (Activity activity: activities) {
      for (Transition transition: activity.transitions) {
        startActivities.remove(transition.to);
      }
    }
    for (Activity startActivity: startActivities) {
      activityInstance.execute(startActivity);
    }
  }
}
