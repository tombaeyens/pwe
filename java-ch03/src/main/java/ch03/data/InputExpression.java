package ch03.data;

import ch03.engine.context.Context;


/**
 * @author Tom Baeyens
 */
public interface InputExpression {

  TypedValue getTypedValue(Context context);
}
