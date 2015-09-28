package ch03.timers;

import ch03.model.ScopeInstance;


/**
 * @author Tom Baeyens
 */
public class Timer {

  protected String name;
  protected ScopeInstance scopeInstance;
  protected TimerHandler timerHandler;
  protected String time;
  protected boolean cancelOnScopeEnd;

  public ScopeInstance getScopeInstance() {
    return this.scopeInstance;
  }
  public void setScopeInstance(ScopeInstance scopeInstance) {
    this.scopeInstance = scopeInstance;
  }
  
  public TimerHandler getTimerHandler() {
    return timerHandler;
  }
  
  public void setTimerHandler(TimerHandler timerHandler) {
    this.timerHandler = timerHandler;
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
