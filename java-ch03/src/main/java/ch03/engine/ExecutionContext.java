package ch03.engine;

import java.util.List;

import ch03.data.Condition;
import ch03.data.TypedValue;
import ch03.engine.context.Context;
import ch03.model.Transition;

/**
 * @author Tom Baeyens
 */
public interface ExecutionContext {

  void addExternal(Context externalContext);

  Context getExternalContext();

  Object findExternally(String key);

  <T> T findExternally(Class<T> type);

  void add(String name, Context context);

  void remove(String name);

  TypedValue get(String key);

  void set(String key, TypedValue value);

  boolean isConditionMet(Condition condition);
  
  List<Transition> getOutgoingTransitionsMeetingCondition();
}