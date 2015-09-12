package ch03.data;


/**
 * @author Tom Baeyens
 */
public class TypedValue {

  Type type;
  Object value;
  
  public TypedValue(Type type, Object value) {
    this.type = type;
    this.value = value;
  }

  public TypedValue dereference(String key) {
    return type.get(value, key);
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

  public void set(String key, TypedValue value) {
    type.set(this.value, key, value);
  }

  @Override
  public String toString() {
    return (type!=null ? type.toString() : "untyped")+"("+value+")";
  }
}
