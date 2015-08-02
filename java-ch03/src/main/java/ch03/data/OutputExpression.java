package ch03.data;

import ch03.Context;


/**
 * @author Tom Baeyens
 */
public interface OutputExpression {

  void set(Context context, TypedValue value);

}
