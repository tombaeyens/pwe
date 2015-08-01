package ch03.data;


public class BooleanType implements Type {
  
  public static BooleanType INSTANCE = new BooleanType();

  @Override
  public TypedValue dereference(Object value, String key) {
    return null;
  }

  @Override
  public TypedValue convertTo(Type targetType) {
    return null;
  }
}
