package ch03.model;

import ch03.engine.Engine;


/**
 * @author Tom Baeyens
 */
public class WorkflowInstance extends ScopeInstance {
  
  public Workflow workflow;
  public Engine engine;

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

  
  public Engine getEngine() {
    return engine;
  }

  
  public void setEngine(Engine engine) {
    this.engine = engine;
  }
}
