package ch03.model;

import java.util.Map;

import ch03.data.TypedValue;
import ch03.engine.Execution;



/**
 * @author Tom Baeyens
 */
public interface Trigger {

  void start(Execution execution, Map<String, TypedValue> startData);
  
}
