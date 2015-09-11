package ch03.data;

import ch03.engine.Context;



/**
 * @author Tom Baeyens
 */
public interface InputExpression {

  TypedValue getTypedValue(Context context);
}
