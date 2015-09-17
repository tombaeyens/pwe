package ch03.engine.context;

import ch03.data.TypedValue;
import ch03.engine.EngineImpl;
import ch03.model.VariableInstance;


/**
 * @author Tom Baeyens
 */
public class VariablesContext implements SubContext {

  EngineImpl engine;

  public VariablesContext(EngineImpl engine) {
    this.engine = engine;
  }

  @Override
  public TypedValue get(String key) {
    VariableInstance variableInstance = engine.getScopeInstance().findVariableInstanceByVariableIdRecursive(key);
    return variableInstance!=null ? variableInstance.getTypedValue() : null;
  }

  @Override
  public void set(String key, TypedValue value) {
  }

  @Override
  public String getName() {
    return "variables";
  }
}
