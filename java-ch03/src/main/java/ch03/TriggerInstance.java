package ch03;


public class TriggerInstance extends ScopeInstance {

  public TriggerInstance(Trigger trigger, WorkflowInstance workflowInstance) {
    this.scope = trigger;
    this.parent = workflowInstance;
  }

}
