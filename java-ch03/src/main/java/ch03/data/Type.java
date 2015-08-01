package ch03.data;


public interface Type {

  TypedValue dereference(Object value, String key);

  TypedValue convertTo(Type targetType);
  
  boolean equals(Object other);
  int hashCode();
}
