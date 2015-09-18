package ch03.timers;

import ch03.engine.Context;
import ch03.engine.Listener;
import ch03.model.ScopeInstance;


/**
 * @author Tom Baeyens
 */
public class CancelTimers implements Listener {

  
  @Override
  public void execute(ScopeInstance scopeInstance, Context context) {
    TimerService timerService = context.getExternal(TimerService.class);
    timerService.cancelTimers(scopeInstance);
  }
}
