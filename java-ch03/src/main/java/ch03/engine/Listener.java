package ch03.engine;

import ch03.model.ScopeInstance;


/**
 * @author Tom Baeyens
 */
public interface Listener {
  
  String START = "start";
  String END = "end";

  void execute(ScopeInstance scopeInstance, Context context);
}
