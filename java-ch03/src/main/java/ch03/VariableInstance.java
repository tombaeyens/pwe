package ch03;

import ch03.data.TypedValue;


public class VariableInstance {

  String id;
  ScopeInstance scopeInstance;
  Variable variable;
  TypedValue typedValue;

  public VariableInstance(ScopeInstance scopeInstance, Variable variable, Object initialValue) {
    this.scopeInstance = scopeInstance;
    this.variable = variable;
    this.typedValue = TypedValue.getTypedValue(initialValue);
  }
}
