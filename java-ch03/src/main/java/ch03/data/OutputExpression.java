package ch03.data;

import ch03.engine.context.Context;


/**
 * @author Tom Baeyens
 */
public interface OutputExpression {

  void setTypedValue(Context context, TypedValue value);

}
