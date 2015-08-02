package ch03.data;


/**
 * @author Tom Baeyens
 */
public class ExceptionType implements Type {

  public static final ExceptionType INSTANCE = new ExceptionType();

  @Override
  public TypedValue get(Object object, String field) {
    return null;
  }

  @Override
  public void set(Object object, String field, TypedValue value) {
  }

  @Override
  public TypedValue convertTo(Type targetType) {
    return null;
  }

}
