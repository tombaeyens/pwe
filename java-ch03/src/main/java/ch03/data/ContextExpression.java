package ch03.data;

import ch03.engine.Context;


/**
 * @author Tom Baeyens
 */
public class ContextExpression implements InputExpression, OutputExpression {
  
  String key;

  @Override
  public TypedValue get(Context context) {
    return context.get(key);
  }

  @Override
  public void set(Context context, TypedValue value) {
    context.set(key, value);
  }
}
