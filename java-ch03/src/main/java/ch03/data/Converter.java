package ch03.data;


/**
 * @author Tom Baeyens
 */
public interface Converter {

  boolean matches(TypedValue typedValue, Type type);

  Object convert(Object value);
}
