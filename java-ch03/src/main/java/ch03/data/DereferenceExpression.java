package ch03.data;

import ch03.Context;


public class DereferenceExpression implements Expression {
  
  Expression expression;
  String key;

  @Override
  public TypedValue get(Context context) {
    TypedValue targetValue = expression.get(context);
    return targetValue.dereference(key);
  }

  @Override
  public void set(Context context, TypedValue value) {
  }
}
