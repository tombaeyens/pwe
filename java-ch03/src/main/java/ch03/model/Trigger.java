package ch03.model;

import java.util.Map;

import ch03.data.TypedValue;
import ch03.engine.Engine;



/**
 * @author Tom Baeyens
 */
public interface Trigger {

  void start(Engine engine, Map<String, TypedValue> startData);
  
}
