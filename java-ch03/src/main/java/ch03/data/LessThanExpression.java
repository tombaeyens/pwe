package ch03.data;

import ch03.engine.Context;


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
    return (left!=null ? left.toString() : "null")+"<"+(right!=null ? right.toString() : "null");
  }
}
