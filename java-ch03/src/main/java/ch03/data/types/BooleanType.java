package ch03.data.types;

import ch03.data.Type;
import ch03.data.TypedValue;


/**
 * @author Tom Baeyens
 */
public class BooleanType extends AbstractType {
  
  public static BooleanType INSTANCE = new BooleanType();

  @Override
  public TypedValue get(Object value, String key) {
    return null;
  }

  @Override
  public TypedValue convertTo(Object value, Type targetType) {
    return null;
  }

  @Override
  public void set(Object object, String field, TypedValue value) {
  }
}
