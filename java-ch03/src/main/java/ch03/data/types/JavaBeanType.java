package ch03.data.types;

import ch03.data.Type;
import ch03.data.TypedValue;


/**
 * @author Tom Baeyens
 */
public class JavaBeanType implements Type {
  
  public static JavaBeanType INSTANCE = new JavaBeanType();

  @Override
  public TypedValue get(Object object, String key) {
    throw new RuntimeException("TODO not implemented yet");
  }

  @Override
  public void set(Object object, String field, TypedValue value) {
    throw new RuntimeException("TODO not implemented yet");
  }

  @Override
  public TypedValue convertTo(Object object, Type targetType) {
    throw new RuntimeException("TODO not implemented yet");
  }
}
