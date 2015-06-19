package ch01;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class Workflow {

  Map<String,Activity> activities = new LinkedHashMap<>();
  List<Activity> startActivities = new ArrayList<>();

  public Workflow activity(Activity activity) {
    activities.put(activity.id, activity);
    startActivities.add(activity);
    return this;
  }

  public Workflow transition(String fromId, String toId) {
    Activity from = activities.get(fromId);
    Activity to = activities.get(toId);
    from.outgoingTransitions.add(new Transition(from, to));
    startActivities.remove(to);
    return this;
  }
}
