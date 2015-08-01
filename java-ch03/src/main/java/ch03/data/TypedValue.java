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
}
