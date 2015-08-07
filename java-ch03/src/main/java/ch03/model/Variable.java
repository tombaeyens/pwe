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
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public Type getType() {
    return type;
  }
  
  public void setType(Type type) {
    this.type = type;
  }
  
  public Object getInitialValue() {
    return initialValue;
  }
  
  public void setInitialValue(Object initialValue) {
    this.initialValue = initialValue;
  }
  
  public InputExpression getInitialValueExpression() {
    return initialValueExpression;
  }
  
  public void setInitialValueExpression(InputExpression initialValueExpression) {
    this.initialValueExpression = initialValueExpression;
  }
}
