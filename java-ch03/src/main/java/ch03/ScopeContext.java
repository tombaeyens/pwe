package ch03;

import ch03.data.TypedValue;


public class ScopeContext implements Context {

  ExecutionController executionController;

  public ScopeContext(ExecutionController executionController) {
    this.executionController = executionController;
  }

  @Override
  public TypedValue get(String key) {
    return null;
  }

  @Override
  public void set(String key, TypedValue value) {
  }
}
