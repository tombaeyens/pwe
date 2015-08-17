package ch03.engine.context;

import java.util.LinkedHashMap;
import java.util.Map;

import ch03.data.TypedValue;
import ch03.data.types.JavaBeanType;


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
    map.put(key, value);
  }
  
  /** fluent setter */
  public MapContext put(String key, TypedValue value) {
    set(key, value);
    return this;
  }
  
  public MapContext put(Object bean) {
    set(bean.getClass().getName(), new TypedValue(new JavaBeanType(), bean));
    return this;
  }
}
