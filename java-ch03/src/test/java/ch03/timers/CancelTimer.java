package ch03.timers;

import ch03.engine.Context;
import ch03.engine.Listener;
import ch03.model.ScopeInstance;


/**
 * @author Tom Baeyens
 */
public class CancelTimer implements Listener {
  
  protected String name;

  @Override
  public void execute(ScopeInstance scopeInstance, Context context) {
    TimerService timerService = context.getExternal(TimerService.class);
    timerService.cancelTimer(scopeInstance, name);
  }

  public String getName() {
    return this.name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public CancelTimer name(String name) {
    this.name = name;
    return this;
  }
}
