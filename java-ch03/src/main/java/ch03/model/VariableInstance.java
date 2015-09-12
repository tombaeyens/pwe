package ch03.model;

import ch03.data.Type;
import ch03.data.TypedValue;


/**
 * @author Tom Baeyens
 */
public class VariableInstance {

  protected String id;
  protected ScopeInstance scopeInstance;
  protected Variable variable;
  protected TypedValue typedValue;
  
  public Type getType() {
    if (variable!=null && variable.getType()!=null) {
      return variable.getType();
    } else if (typedValue!=null && typedValue.getType()!=null) {
      return typedValue.getType();
    }
    return null;
  }
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public ScopeInstance getScopeInstance() {
    return scopeInstance;
  }
  
  public void setScopeInstance(ScopeInstance scopeInstance) {
    this.scopeInstance = scopeInstance;
  }
  
  public Variable getVariable() {
    return variable;
  }
  
  public void setVariable(Variable variable) {
    this.variable = variable;
  }
  
  public TypedValue getTypedValue() {
    return typedValue;
  }
  
  public void setTypedValue(TypedValue typedValue) {
    this.typedValue = typedValue;
  }
}
