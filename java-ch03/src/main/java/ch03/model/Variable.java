package ch03.model;

import ch03.data.InputExpression;
import ch03.data.Type;


/**
 * @author Tom Baeyens
 */
public class Variable {

  protected String id;
  protected Type type;
  protected Object initialValue;
  protected InputExpression initialValueExpression;
  
  public String toString() {
    return "$"+id;
  }
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public Variable id(String id) {
    this.id = id;
    return this;
  }
  
  public Type getType() {
    return type;
  }
  
  public void setType(Type type) {
    this.type = type;
  }
  
  public Variable type(Type type) {
    this.type = type;
    return this;
  }
  
  public Object getInitialValue() {
    return initialValue;
  }
  
  public void setInitialValue(Object initialValue) {
    this.initialValue = initialValue;
  }

  public Variable initialValue(Object initialValue) {
    this.initialValue = initialValue;
    return this;
  }

  public InputExpression getInitialValueExpression() {
    return initialValueExpression;
  }
  
  public void setInitialValueExpression(InputExpression initialValueExpression) {
    this.initialValueExpression = initialValueExpression;
  }

  public Variable initialValueExpression(InputExpression initialValueExpression) {
    this.initialValueExpression = initialValueExpression;
    return this;
  }
}
