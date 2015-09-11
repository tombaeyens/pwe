package ch03.engine;

import java.util.List;

import ch03.data.Condition;
import ch03.data.TypedValue;
import ch03.engine.context.SubContext;
import ch03.model.Transition;

/**
 * @author Tom Baeyens
 */
public interface Context {

  SubContext getExternalContext();
  Object findExternally(String key);
  <T> T findExternally(Class<T> type);

  TypedValue get(String key);

  void set(String key, TypedValue value);

  boolean isConditionMet(Condition condition);
  List<Transition> getOutgoingTransitionsMeetingCondition();
}