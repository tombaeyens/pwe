package ch03.data;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import ch03.data.types.ExceptionType;
import ch03.engine.Context;


/**
 * @author Tom Baeyens
 */
public class MethodExpression implements InputExpression {
  
  Method method;
  InputExpression objectExpression;
  List<InputExpression> argExpressions;
  Type returnValueType;

  @Override
  public TypedValue get(Context context) {
    Object object = objectExpression.get(context).value;
    Object[] args = null;
    if (argExpressions!=null) {
      List<Object> argsList = new ArrayList<>();
      for (InputExpression argExpression: argExpressions) {
        Object argValue = argExpression.get(context).value;
        argsList.add(argValue);
      }
      args = argsList.toArray();
    }
    try {
      Object returnValue = method.invoke(object, args);
      return new TypedValue(returnValueType, returnValue);
    } catch (Exception e) {
      e.printStackTrace();
      // euh.. not sure if this is the best solution:
      return new TypedValue(ExceptionType.INSTANCE, e);
    }
  }
}
