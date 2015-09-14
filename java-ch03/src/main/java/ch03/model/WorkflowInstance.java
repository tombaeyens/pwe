package ch03.model;

import ch03.engine.EngineImpl;


/**
 * @author Tom Baeyens
 */
public class WorkflowInstance extends ScopeInstance {
  
  public Workflow workflow;
  public EngineImpl engine;

  public WorkflowInstance getWorkflowInstance() {
    return this;
  }

  public String toString() {
    String workflowId = workflow.getId();
    return workflowId!=null 
            ? "("+workflowId+"|"+id+")"
            : "("+id+")"; 
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

  
  public EngineImpl getEngine() {
    return engine;
  }

  
  public void setEngine(EngineImpl engine) {
    this.engine = engine;
  }
}
