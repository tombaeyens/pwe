package ch03;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ch03.data.InputExpression;
import ch03.data.OutputExpression;
import ch03.data.TypedValue;


/**
 * @author Tom Baeyens
 */
public class ScopeInstance {
  
  Scope scope;
  ScopeInstance parent;
  List<ActivityInstance> activityInstances = new ArrayList<>();
  /** variable instances by variable.id (not by variableInstance.id) */
  Map<String,VariableInstance> variableInstances = new LinkedHashMap<>();
  ExecutionState state;
  
  protected void initialize(Context context) {
    for (String key: scope.variables.keySet()) {
      Variable variable = scope.variables.get(key);
      InputExpression inputExpression = scope.inputs.get(key);
      TypedValue initialTypedValue = null;
      if (inputExpression!=null) {
        initialTypedValue = inputExpression.get(context);
      } else if (variable!=null) {
        initialTypedValue = context!=null ? context.get(key) : null;
      }
      createVariableInstance(variable, initialTypedValue);
    }
  }

  protected void destroy(Context context) {
    for (String key: scope.outputs.keySet()) {
      VariableInstance variableInstance = variableInstances.get(key);
      TypedValue typedValue = variableInstance!=null ? variableInstance.typedValue : null;
      if (typedValue!=null) {
        OutputExpression outputExpression = scope.outputs.get(key);
        outputExpression.set(context, variableInstance.typedValue);
      }
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
