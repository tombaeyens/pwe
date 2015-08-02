package ch03;


/**
 * @author Tom Baeyens
 */
public class TriggerInstance extends ScopeInstance {

  public TriggerInstance(Trigger trigger, WorkflowInstance workflowInstance) {
    this.scope = trigger;
    this.parent = workflowInstance;
  }

}
