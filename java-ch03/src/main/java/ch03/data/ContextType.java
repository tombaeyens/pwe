package ch03.data;


public class ContextType implements Type {

  public static final ContextType INSTANCE = new ContextType();

  @Override
  public TypedValue dereference(Object value, String key) {
    return null;
  }

  @Override
  public TypedValue convertTo(Type targetType) {
    throw new UnsupportedOperationException();
  }

}
