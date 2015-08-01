package ch03;



public class WorkflowInstance extends ScopeInstance {
  
  Workflow workflow;
  TriggerInstance triggerInstance;
  
  public WorkflowInstance(Workflow workflow) {
    this.scope = workflow;
    this.workflow = workflow;
  }

  public boolean isEnded() {
    return state.isEnded();
  }

  public void end() {
    state = new Ended();
  }
}
