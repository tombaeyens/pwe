package ch03.data;

import ch03.Context;


public class ConvertExpression implements Expression {
  
  Expression expression;
  Type targetType;

  @Override
  public TypedValue get(Context context) {
    TypedValue typedValue = expression.get(context);
    if (typedValue==null) {
      return null;
    }
    Type type = typedValue.type;
    return type.convertTo(targetType);
  }

  @Override
  public void set(Context context, TypedValue value) {
  }
}
