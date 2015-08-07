package ch03.engine;

import ch03.data.TypedValue;


/**
 * @author Tom Baeyens
 */
public interface Context {
  
  TypedValue get(String key);
  void set(String key, TypedValue value);
}
