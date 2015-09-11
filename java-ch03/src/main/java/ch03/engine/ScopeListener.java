package ch03.engine;

import ch03.model.ScopeInstance;


/**
 * @author Tom Baeyens
 */
public interface ScopeListener {

  void scopeStarting(ScopeInstance scopeInstance);
  void scopeEnding(ScopeInstance scopeInstance);
}
