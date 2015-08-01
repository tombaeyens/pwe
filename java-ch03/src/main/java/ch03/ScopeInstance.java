package ch03;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class ScopeInstance {
  
  Scope scope;
  ScopeInstance parentScopeInstance;
  List<ScopeInstance> scopeInstances = new ArrayList<>();
  Map<String,Object> variables = new LinkedHashMap<>();
  ExecutionState state;
  
  public void initialize(Scope scope, ScopeInstance parentScopeInstance) {
    this.scope = scope;
    this.parentScopeInstance = parentScopeInstance;
    this.state = new Starting();
  }

  public void start() {
    start(new ExecutionController());
  }

  public void start(ExecutionController executionController) {
    executionController.scopeInstance = this;
    scope.start(executionController);
  }
  
  public Map<String, Object> getVariables() {
    return variables;
  }

  public boolean isEnded() {
    return state.isEnded();
  }

  public void end() {
    state = new Ended();
  }
}
