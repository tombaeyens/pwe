package ch03.engine.context;

import java.util.Map;

import ch03.data.TypedValue;
import ch03.data.types.JavaBeanType;


/**
 * @author Tom Baeyens
 */
public class MapContext implements SubContext {
  
  String name;
  Map<String,TypedValue> map;
  
  public MapContext(String name, Map<String,TypedValue> map) {
    this.name = name;
    this.map = map;
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

  @Override
  public String getName() {
    return name;
  }
}
