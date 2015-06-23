package ch01;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** A workflow specifies an execution flow and is composed of activities
 * and transitions.
 * 
 * A workflow typically has a diagram representation: the activities are boxes 
 * and the transitions are the arrows between the boxes. 
 * 
 * A workflow can be executed many times.  
 * Each execution of a workflow is called a {@link WorkflowInstance} */
public class Workflow {

  /** All activities of this workflow.
   * Activity.id's are mapped to activities */
  Map<String,Activity> activities = new LinkedHashMap<>();
  
  /** The subset of activities that have to be started 
   * when a new workflow instance starts.  
   * All activities that do not have incoming transitions 
   * are start activities. (like in BPMN)
   * This memberfield is initialized in methods 
   * {@link #activity(Activity)} and {@link #transition(String, String)} */
  List<Activity> startActivities = new ArrayList<>();

  /** Adds an activity to this workflow */
  public Workflow activity(Activity activity) {
    activities.put(activity.id, activity);
    startActivities.add(activity);
    return this;
  }

  /** Adds a transition to this workflow.
   * This simplified implementation requires that the activities are already 
   * added before the transition is created. */
  public Workflow transition(String fromId, String toId) {
    Activity from = activities.get(fromId);
    assert from!=null;
    Activity to = activities.get(toId);
    assert to!=null;
    from.outgoingTransitions.add(new Transition(from, to));
    startActivities.remove(to);
    return this;
  }
  
  public Map<String, Activity> getActivities() {
    return activities;
  }

  public List<Activity> getStartActivities() {
    return startActivities;
  }
}
