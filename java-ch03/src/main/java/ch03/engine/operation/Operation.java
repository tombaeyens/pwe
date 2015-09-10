package ch03.engine.operation;

import ch03.engine.Execution;
import ch03.engine.ExecutionContextImpl;
import ch03.engine.ExecutionControllerImpl;
import ch03.model.ScopeInstance;


public abstract class Operation {
  
  ScopeInstance scopeInstance;
  
  public abstract void perform(Execution execution, ExecutionContextImpl context, ExecutionControllerImpl controller);

  public Operation(ScopeInstance scopeInstance) {
    this.scopeInstance = scopeInstance;
  }
  
  public ScopeInstance getScopeInstance() {
    return scopeInstance;
  }
  
  public boolean isSynchonrous() {
    return true;
  }
}