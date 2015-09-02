package ch03.data;

import ch03.engine.context.Context;


/**
 * @author Tom Baeyens
 */
public class TypedValueExpression implements InputExpression {

  TypedValue typedValue;

  public TypedValueExpression(Type type, Object value) {
    this.typedValue = new TypedValue(type, value);
  }

  @Override
  public TypedValue getTypedValue(Context context) {
    return typedValue;
  }

  public String toString() {
    return "'"+typedValue.value+"'";
  }

  public TypedValue getTypedValue() {
    return typedValue;
  }

  public Object getValue() {
    return typedValue!=null ? typedValue.value : null;
  }
}
