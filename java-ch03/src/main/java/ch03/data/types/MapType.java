package ch03.data.types;

import java.util.Map;

import ch03.data.Type;
import ch03.data.TypedValue;


/**
 * @author Tom Baeyens
 */
public class MapType extends AbstractType {
  
  public static MapType INSTANCE = new MapType();

  public static TypedValue typedValue(Map<String,Object> map) {
    return new TypedValue(INSTANCE, map);
  }

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
