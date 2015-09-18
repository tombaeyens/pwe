package ch03.timers;

import ch03.model.ScopeInstance;


/**
 * @author Tom Baeyens
 */
public interface TimerService {

  void createTimer(Timer timer);

  void cancelTimers(ScopeInstance scopeInstance);

  void cancelTimer(ScopeInstance scopeInstance, String name);
}
