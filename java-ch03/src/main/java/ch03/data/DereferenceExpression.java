package ch03.data;

import ch03.engine.context.Context;
import ch03.util.Strings;


/**
 * @author Tom Baeyens
 */
public class DereferenceExpression implements InputExpression, OutputExpression {
  
  InputExpression expression;
  String key;
  
  public DereferenceExpression(InputExpression expression, String key) {
    this.expression = expression;
    this.key = key;
  }

  @Override
  public TypedValue getTypedValue(Context context) {
    TypedValue targetValue = expression.getTypedValue(context);
    return targetValue.dereference(key);
  }

  @Override
  public void setTypedValue(Context context, TypedValue value) {
    TypedValue targetValue = expression.getTypedValue(context);
    targetValue.set(key, value);
  }

  @Override
  public String toString() {
    return Strings.toString(expression)+"."+key;
  }

  public InputExpression getExpression() {
    return expression;
  }

  public String getKey() {
    return key;
  }
}
