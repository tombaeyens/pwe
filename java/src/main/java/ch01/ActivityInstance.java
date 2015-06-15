package ch01;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ActivityInstance {

  ActivityInstance parent;
  Activity activity;
  Date start;
  Date end;
  List<ActivityInstance> activityInstances = new ArrayList<>();
  
  public ActivityInstance(Activity activity, ActivityInstance parent) {
    this.activity = activity;
    this.parent = parent;
    this.start = new Date();
  }

  public void execute(Activity activity) {
    ActivityInstance activityInstance = new ActivityInstance(activity, this);
    activityInstances.add(activityInstance);
    activity.execute(activityInstance);
  }

  public void end() {
    end = new Date();
    for (Transition transition: activity.transitions) {
      take(transition);
    }
  }

  public void take(Transition transition) {
    end = new Date();
    parent.execute(transition.to);
  }
}
