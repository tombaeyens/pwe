package ch03.data;

import ch03.engine.context.Context;


/**
 * @author Tom Baeyens
 */
public class ContextExpression implements InputExpression, OutputExpression {
  
  String key;

  @Override
  public TypedValue getTypedValue(Context context) {
    return context.get(key);
  }

  @Override
  public void setTypedValue(Context context, TypedValue value) {
    context.set(key, value);
  }
}
