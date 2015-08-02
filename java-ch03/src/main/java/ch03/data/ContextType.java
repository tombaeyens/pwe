package ch03.data;


/**
 * @author Tom Baeyens
 */
public class ContextType implements Type {

  public static final ContextType INSTANCE = new ContextType();

  @Override
  public TypedValue get(Object value, String key) {
    return null;
  }

  @Override
  public TypedValue convertTo(Type targetType) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void set(Object object, String field, TypedValue value) {
  }

}
