package ch03.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ch03.data.Condition;
import ch03.data.TypedValue;
import ch03.data.types.ContextType;
import ch03.engine.context.Context;
import ch03.engine.context.ScopeContext;
import ch03.engine.context.ScopeInstanceContext;
import ch03.model.Activity;
import ch03.model.Transition;


/**
 * @author Tom Baeyens
 */
public class ExecutionContextImpl implements Context, ExecutionContext {
  
  public static final String KEY_CONFIGURATIONS = "configurations";
  public static final String KEY_VARIABLES = "variables";
  public static final String KEY_EXTERNAL = "external";
  
  Execution execution;
  ScopeInstanceContext scopeInstanceContext;
  ScopeContext scopeContext;
  Context externalContext;
  Map<String,Context> contexts;
  List<String> searchOrder = null;
  
  Map<String, TypedValue> inputs;
  Map<String, TypedValue> outputs;

  public ExecutionContextImpl(Execution execution) {
    this.execution = execution;
    scopeInstanceContext = new ScopeInstanceContext(execution);
    scopeContext = new ScopeContext(execution);
    contexts = new LinkedHashMap<>();
    contexts.put(KEY_VARIABLES, scopeInstanceContext);
    contexts.put(KEY_CONFIGURATIONS, scopeContext);
  }
  
  @Override
  public void addExternal(Context externalContext) {
    this.externalContext = externalContext;
    add(KEY_EXTERNAL, externalContext);
  }
  
  @Override
  public Context getExternalContext() {
    return externalContext;
  }
  
  @Override
  public Object findExternally(String key) {
    Context externalContext = getExternalContext();
    return externalContext!=null ? externalContext.get(key) : null;
  }
  
  @Override
  public <T> T findExternally(Class<T> type) {
    return (T) findExternally(type.getName());
  }

  @Override
  public void add(String name, Context context) {
    if (context!=null) {
      if (searchOrder!=null && !contexts.containsKey(name)) {
        searchOrder.add(name);
      }
      contexts.put(name, context);
    }
  }

  @Override
  public void remove(String name) {
    contexts.remove(name);
    if (searchOrder!=null) {
      searchOrder.remove(name);
    }
  }

  @Override
  public TypedValue get(String key) {
    Context context1 = contexts.get(key);
    if (context1!=null) {
      return new TypedValue(ContextType.INSTANCE, context1);
    }
    Collection<String> contextNames = null;
    if (searchOrder!=null) {
      contextNames = searchOrder;
    } else {
      contextNames = contexts.keySet();
    }
    for (String contextName: contextNames) {
      Context context2 = contexts.get(contextName);
      TypedValue typedValue = context2.get(key);
      if (typedValue!=null) {
        return typedValue;
      }
    }
    return null;
  }

  @Override
  public void set(String key, TypedValue value) {
  }
  
  public Map<String, TypedValue> getInputs() {
    return inputs;
  }

  
  public void setInputs(Map<String, TypedValue> inputs) {
    this.inputs = inputs;
  }

  
  public Map<String, TypedValue> getOutputs() {
    return outputs;
  }

  
  public void setOutputs(Map<String, TypedValue> outputs) {
    this.outputs = outputs;
  }

  @Override
  public boolean isConditionMet(Condition condition) {
    if (condition==null) {
      return true;
    }
    return condition.evaluate(this);
  }

  @Override
  public List<Transition> getOutgoingTransitionsMeetingCondition() {
    Activity activity = (Activity) execution.scopeInstance.getScope();
    if (activity.outTransitions.isEmpty()) {
      return activity.outTransitions;
    }
    List<Transition> outgoingTransitionsMeetingCondition = new ArrayList<>();
    for (Transition transition: activity.outTransitions) {
      if (isConditionMet(transition.condition)) {
        outgoingTransitionsMeetingCondition.add(transition);
      }
    }
    return outgoingTransitionsMeetingCondition;
  }
}
