package ch03.data;


public class TypedValue {

  Type type;
  Object value;
  
  public TypedValue(Type type, Object value) {
    this.type = type;
    this.value = value;
  }

  public TypedValue dereference(String key) {
    return type.dereference(value, key);
  }
  
  public Type getType() {
    return type;
  }

  public Object getValue() {
    return value;
  }

  public static TypedValue getTypedValue(Object value) {
    if (value==null || (value instanceof TypedValue)) {
      return (TypedValue) value;
    }
    return new TypedValue(null, value);
  }
}
