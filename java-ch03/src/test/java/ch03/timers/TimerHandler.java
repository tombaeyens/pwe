package ch03.timers;

import java.util.Map;

import ch03.data.TypedValue;


/**
 * @author Tom Baeyens
 */
public class TimerHandler {

  protected Map<String,TypedValue> inputs;
  
  public Map<String, TypedValue> getInputs() {
    return inputs;
  }
  
  public void setInputs(Map<String, TypedValue> inputs) {
    this.inputs = inputs;
  }
}
