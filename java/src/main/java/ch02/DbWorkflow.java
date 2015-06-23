package ch02;

import ch01.Activity;
import ch01.Workflow;


public class DbWorkflow extends Workflow {

  String id;

  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }

  @Override
  public DbWorkflow activity(Activity activity) {
    super.activity(activity);
    return this;
  }

  @Override
  public DbWorkflow transition(String fromId, String toId) {
    super.transition(fromId, toId);
    return this;
  }
}
