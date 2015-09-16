package ch03.engine;

import java.util.List;
import java.util.Map;

import ch03.data.Condition;
import ch03.data.InputExpression;
import ch03.data.Type;
import ch03.data.TypedValue;
import ch03.engine.context.SubContext;
import ch03.model.Transition;

/**
 * @author Tom Baeyens
 */
public interface Context {

  SubContext getExternalContext();
  Object getExternal(String key);
  <T> T getExternal(Class<T> type);

  TypedValue getTypedValue(String key);
  TypedValue getTypedValue(InputExpression expression);
  <T> T getValue(String key);
  <T> T getValue(String key, Type type);
  <T> T getValue(InputExpression expression);
  <T> T getValue(InputExpression expression, Type type);
  void setTypedValue(String key, TypedValue value);

  Map<String,TypedValue> readInputs();
  void writeOutputs(Map<String,TypedValue> outputs);

  boolean isConditionMet(Condition condition);
  List<Transition> getOutgoingTransitionsMeetingCondition();
}