package ch03.data;

import ch03.engine.Context;
import ch03.util.Strings;


/**
 * @author Tom Baeyens
 */
public class LessThanExpression extends OperatorExpression {

  public LessThanExpression(InputExpression left, InputExpression right) {
    super(left, right);
  }

  @Override
  public TypedValue operate(TypedValue leftValue, TypedValue rightValue, Context context) {
    
    return null;
  }

  public String toString() {
    return Strings.toString(left)+"<"+Strings.toString(right);
  }
}
