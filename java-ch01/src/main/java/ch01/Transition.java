package ch01;


/** Specifies an execution flow between 2 activities.
 * When the 'from' (aka source) activity ends, the 'to' (aka destination) activity 
 * is started.
 * Typically transitions are represented in diagrams as an arrow between the activity boxes. */
public class Transition {

  Activity from;
  Activity to;

  public Transition(Activity from, Activity to) {
    assert from!=null;
    assert to!=null;
    this.from = from;
    this.to = to;
  }

  public Activity getFrom() {
    return from;
  }

  public void setFrom(Activity from) {
    this.from = from;
  }

  public Activity getTo() {
    return to;
  }
  
  public void setTo(Activity to) {
    this.to = to;
  }
}
