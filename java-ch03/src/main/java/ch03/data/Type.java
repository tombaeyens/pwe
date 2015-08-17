package ch03.data;


/**
 * @author Tom Baeyens
 */
public interface Type {

  TypedValue get(Object object, String field);
  void set(Object object, String field, TypedValue value);

  TypedValue convertTo(Object object, Type targetType);

  /** used to check if conversion is needed */
  boolean equals(Object other);
  int hashCode();
}
