package ch01;
import java.util.ArrayList;
import java.util.List;


public abstract class Activity {

  String id;
  List<Transition> outgoingTransitions = new ArrayList<>();
  
  public Activity(String id) {
    this.id = id;
  }

  public abstract void execute(ActivityInstance activityInstance);
}
