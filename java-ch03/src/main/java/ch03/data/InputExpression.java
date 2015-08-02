package ch03.data;

import ch03.Context;


/**
 * @author Tom Baeyens
 */
public interface InputExpression {

  TypedValue get(Context context);
}
