package ch03.data;

import ch03.engine.Context;



/**
 * @author Tom Baeyens
 */
public class NameExpression implements InputExpression, OutputExpression {
  
  String name;

  public NameExpression(String name) {
    this.name = name;
  }

  @Override
  public TypedValue getTypedValue(Context context) {
    return context.get(name);
  }

  @Override
  public void setTypedValue(Context context, TypedValue value) {
    context.set(name, value);
  }

  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String toString() {
    return "$"+name;
  }

}
