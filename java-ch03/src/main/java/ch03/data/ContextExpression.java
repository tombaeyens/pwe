package ch03.data;

import ch03.Context;


public class ContextExpression implements Expression {
  
  String key;

  @Override
  public TypedValue get(Context context) {
    return context.get(key);
  }

  @Override
  public void set(Context context, TypedValue value) {
  }

}
