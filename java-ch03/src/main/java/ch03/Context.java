package ch03;

import ch03.data.TypedValue;


public interface Context {
  
  TypedValue get(String key);
  void set(String key, TypedValue value);
}
