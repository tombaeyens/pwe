package ch03.engine.context;

import ch03.data.TypedValue;
import ch03.engine.Engine;


/**
 * @author Tom Baeyens
 */
public class ConfigurationsContext implements SubContext {

  Engine engine;

  public ConfigurationsContext(Engine engine) {
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
    return "configurations";
  }
}
