package ch03;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class ScopeInstance {
  
  Scope scope;
  ScopeInstance parent;
  List<ActivityInstance> activityInstances = new ArrayList<>();
  /** variable instances by variable.id (not by variableInstance.id) */
  Map<String,VariableInstance> variableInstances = new LinkedHashMap<>();
  ExecutionState state;
  
  protected void initializeVariableInstances(Map<String, Object> initialData) {
    for (String key: scope.variables.keySet()) {
      Variable variable = scope.variables.get(key);
      Object initialValue = initialData!=null ? initialData.remove(key) : null;
      createVariableInstance(variable, initialValue);
    }
  }

  protected void createVariableInstance(Variable variable, Object initialValue) {
    VariableInstance variableInstance = new VariableInstance(this, variable, initialValue);
    variableInstances.put(variable.id, variableInstance);
  }

  public boolean isEnded() {
    return state.isEnded();
  }

  public void end() {
    state = new Ended();
  }

  public ScopeInstance getParent() {
    return parent;
  }
}
