package ch03.data.types;

import ch03.data.Type;
import ch03.data.TypedValue;


/**
 * @author Tom Baeyens
 */
public class ExceptionType extends AbstractType {

  public static final ExceptionType INSTANCE = new ExceptionType();

  @Override
  public TypedValue get(Object object, String field) {
    return null;
  }

  @Override
  public void set(Object object, String field, TypedValue value) {
  }

  @Override
  public TypedValue convertTo(Object value, Type targetType) {
    return null;
  }

}
