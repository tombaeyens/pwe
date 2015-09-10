package ch03.model;


/**
 * @author Tom Baeyens
 */
public class WorkflowInstance extends ScopeInstance {
  
  public Workflow workflow;

  public WorkflowInstance getWorkflowInstance() {
    return this;
  }

  @Override
  public boolean isActivityInstance() {
    return false;
  }
  
  @Override
  public boolean isWorkflowInstance() {
    return true;
  }
  
  public Workflow getWorkflow() {
    return workflow;
  }
  
  public void setWorkflow(Workflow workflow) {
    this.workflow = workflow;
  }
}
