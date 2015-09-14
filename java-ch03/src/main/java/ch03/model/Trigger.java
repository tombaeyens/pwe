package ch03.model;

import java.util.Map;

import ch03.data.TypedValue;
import ch03.engine.EngineImpl;



/**
 * @author Tom Baeyens
 */
public interface Trigger {

  void start(EngineImpl engine, Map<String, TypedValue> startData);
  
}
