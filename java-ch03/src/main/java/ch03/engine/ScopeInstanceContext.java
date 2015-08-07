package ch03.engine;

import ch03.data.TypedValue;


/**
 * @author Tom Baeyens
 */
public class ScopeInstanceContext implements Context {

  Execution execution;

  public ScopeInstanceContext(Execution execution) {
    this.execution = execution;
  }

  @Override
  public TypedValue get(String key) {
    return null;
  }

  @Override
  public void set(String key, TypedValue value) {
  }
}
