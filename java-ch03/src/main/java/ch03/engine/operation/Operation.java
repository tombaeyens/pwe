package ch03.engine.operation;

import ch03.engine.ContextImpl;
import ch03.engine.ControllerImpl;
import ch03.engine.Engine;
import ch03.model.ScopeInstance;


public abstract class Operation {
  
  ScopeInstance scopeInstance;
  
  public abstract void perform(Engine engine, ContextImpl context, ControllerImpl controller);

  public Operation(ScopeInstance scopeInstance) {
    this.scopeInstance = scopeInstance;
  }
  
  public ScopeInstance getScopeInstance() {
    return scopeInstance;
  }
  
  public boolean isSynchonrous() {
    return true;
  }
  
  public String toString() {
    return getClass().getSimpleName()+
      "("+
      (scopeInstance.isActivityInstance() 
              ? scopeInstance.getWorkflowInstance().getId()+"-"+scopeInstance.getId()
              : scopeInstance.getId())+
      ")";
  }
}