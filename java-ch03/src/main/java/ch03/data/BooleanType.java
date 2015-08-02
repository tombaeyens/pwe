package ch03.data;


/**
 * @author Tom Baeyens
 */
public class BooleanType implements Type {
  
  public static BooleanType INSTANCE = new BooleanType();

  @Override
  public TypedValue get(Object value, String key) {
    return null;
  }

  @Override
  public TypedValue convertTo(Type targetType) {
    return null;
  }

  @Override
  public void set(Object object, String field, TypedValue value) {
  }
}
