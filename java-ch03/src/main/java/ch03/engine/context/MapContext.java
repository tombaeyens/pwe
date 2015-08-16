package ch03.engine.context;

import java.util.LinkedHashMap;
import java.util.Map;

import ch03.data.TypedValue;


/**
 * @author Tom Baeyens
 */
public class MapContext implements Context {
  
  Map<Object,TypedValue> map;
  
  public MapContext() {
    this(null);
  }

  public MapContext(Map<String,Object> map) {
    this.map = new LinkedHashMap<>();
    if (map!=null) {
      for (String key: map.keySet()) {
        Object value = map.get(key);
        this.map.put(key, TypedValue.getTypedValue(value));
      }
    }
  }

  @Override
  public TypedValue get(String key) {
    return map.get(key);
  }

  @Override
  public void set(String key, TypedValue value) {
  }
}
