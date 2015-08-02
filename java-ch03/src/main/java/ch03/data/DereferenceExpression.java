package ch03.data;

import ch03.Context;


/**
 * @author Tom Baeyens
 */
public class DereferenceExpression implements InputExpression, OutputExpression {
  
  InputExpression expression;
  String key;

  @Override
  public TypedValue get(Context context) {
    TypedValue targetValue = expression.get(context);
    return targetValue.dereference(key);
  }

  @Override
  public void set(Context context, TypedValue value) {
    TypedValue targetValue = expression.get(context);
    targetValue.set(key, value);
  }
}
