package ch03;

public abstract class Operation {
  
  ScopeInstance scopeInstance;
  
  public abstract void perform(ExecutionController executionFlow);
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