package ch03.engine.operation;

import ch03.engine.ContextImpl;
import ch03.engine.ControllerImpl;
import ch03.engine.Engine;
import ch03.model.ActivityInstance;
import ch03.model.ScopeInstance;
import ch03.model.WorkflowInstance;


public abstract class Operation {
  
  protected ScopeInstance scopeInstance;
  protected WorkflowInstance workflowInstance;
  protected ActivityInstance activityInstance;
  
  public abstract void perform(Engine engine, ContextImpl context, ControllerImpl controller);

  public Operation(ScopeInstance scopeInstance) {
    this.scopeInstance = scopeInstance;
    if (scopeInstance.isActivityInstance()) {
      this.activityInstance = (ActivityInstance) scopeInstance;
    } else {
      this.workflowInstance = (WorkflowInstance) scopeInstance;
    }
  }
  
  public ScopeInstance getScopeInstance() {
    return scopeInstance;
  }
  
  public boolean isAsynchronous() {
    return false;
  }
  
  public String toString() {
    return getClass().getSimpleName()+
      "("+
      (activityInstance!=null 
              ? activityInstance.getWorkflowInstance().getId()+"-"+activityInstance.getId()
              : scopeInstance.getId())+
      ")";
  }
  
  public boolean requiresTransactionSave() {
    return false;
  }
}