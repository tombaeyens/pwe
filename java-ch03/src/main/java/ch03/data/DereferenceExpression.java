package ch03.data;

import ch03.engine.context.Context;


/**
 * @author Tom Baeyens
 */
public class DereferenceExpression implements InputExpression, OutputExpression {
  
  InputExpression expression;
  String key;

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
}
