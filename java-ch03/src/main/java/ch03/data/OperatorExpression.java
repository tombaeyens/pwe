package ch03.data;

import ch03.engine.Context;


/**
 * @author Tom Baeyens
 */
public abstract class OperatorExpression  implements InputExpression {

  protected InputExpression left;
  protected InputExpression right;
  
  public OperatorExpression(InputExpression left, InputExpression right) {
    this.left = left;
    this.right = right;
  }

  public InputExpression getLeft() {
    return this.left;
  }
  public void setLeft(InputExpression left) {
    this.left = left;
  }
  public OperatorExpression left(InputExpression left) {
    this.left = left;
    return this;
  }
  
  public InputExpression getRight() {
    return this.right;
  }
  public void setRight(InputExpression right) {
    this.right = right;
  }
  public OperatorExpression right(InputExpression right) {
    this.right = right;
    return this;
  }
  
  @Override
  public TypedValue getTypedValue(Context context) {
    TypedValue leftValue = left!=null ? left.getTypedValue(context) : null;
    TypedValue rightValue = right!=null ? right.getTypedValue(context) : null;
    return operate(leftValue, rightValue, context);
  }
  
  public abstract TypedValue operate(TypedValue leftValue, TypedValue rightValue, Context context);
}
