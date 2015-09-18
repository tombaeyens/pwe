package ch03.timers;

import java.util.Map;

import ch03.data.TypedValue;
import ch03.model.ScopeInstance;


/**
 * @author Tom Baeyens
 */
public class Timer {

  protected String name;
  protected ScopeInstance scopeInstance;
  protected Map<String,TypedValue> inputs;
  protected String time;
  protected boolean cancelOnScopeEnd;

  public ScopeInstance getScopeInstance() {
    return this.scopeInstance;
  }
  public void setScopeInstance(ScopeInstance scopeInstance) {
    this.scopeInstance = scopeInstance;
  }
  
  public Map<String, TypedValue> getInputs() {
    return inputs;
  }
  
  public void setInputs(Map<String, TypedValue> inputs) {
    this.inputs = inputs;
  }
  
  public String getTime() {
    return time;
  }
  
  public void setTime(String time) {
    this.time = time;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public boolean isCancelOnScopeEnd() {
    return cancelOnScopeEnd;
  }
  
  public void setCancelOnScopeEnd(boolean cancelOnScopeEnd) {
    this.cancelOnScopeEnd = cancelOnScopeEnd;
  }
}
