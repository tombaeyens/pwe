package ch01;
import java.util.ArrayList;
import java.util.List;


/** An activity is a static description of one step in the workflow.
 * 
 * Subclasses of Activity represent different types of activities.  
 * Each activity type should have it's own subclass containing the 
 * specific runtime logic for the activity. */
public abstract class Activity {

  /** the unique id of the activity used to establish transitions with {@link Workflow#transition(String, String)} */
  String id;

  /** the list of transitions leaving this activity */
  List<Transition> outgoingTransitions = new ArrayList<>();
  
  public Activity(String id) {
    assert id!=null;
    this.id = id;
  }

  /** called when the workflow engine wants to execute this activity.  
   *
   * When implementing the start method, typically there is some activity type 
   * specific logic performed first. 
   * 
   * The start method implementation has essentially 3 ways to control the 
   * execution flow.
   * 
   * 1) {@link ActivityInstance#end()} is called to indicate that this activity 
   *    instance is done and the workflow should proceed.  Ending the activity 
   *    will cause all outgoing transitions to be taken.  That means that the destination 
   *    activities (Transition.to) of those transitions will then be executed.
   * 
   * 2) {@link ActivityInstance#take(Transition)} is called one or more times 
   *    to take an individual transition.  Taking a transition means the 
   *    activity instance is ended.   
   * 
   * 3) None of the above execution propagation methods is called.  In that 
   *    case the execution flow remains positioned in the activity instance 
   *    until the activity instance is ended externally. */
  public abstract void start(ActivityInstance activityInstance);
}
