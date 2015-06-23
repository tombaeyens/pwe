package ch02;

import ch01.Workflow;
import ch01.WorkflowInstance;


public class DbWorkflowInstance extends WorkflowInstance {

  String id;

  public DbWorkflowInstance(Workflow workflow) {
    super(workflow);
  }

  public DbWorkflowInstance() {
    super(null);
  }

  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
}
