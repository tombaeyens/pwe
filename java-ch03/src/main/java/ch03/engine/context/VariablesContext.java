package ch03.engine.context;

import ch03.data.TypedValue;
import ch03.engine.EngineImpl;


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
    return null;
  }

  @Override
  public void set(String key, TypedValue value) {
  }

  @Override
  public String getName() {
    return "variables";
  }
}
