package ch03.engine.context;

import java.util.LinkedHashMap;
import java.util.Map;

import ch03.data.TypedValue;
import ch03.data.types.ContextType;
import ch03.engine.Execution;


/**
 * @author Tom Baeyens
 */
public class ExecutionContext implements Context {
  
  Execution execution;
  Map<String,Context> contexts = new LinkedHashMap<>();
  
  public ExecutionContext(Execution execution) {
    contexts.put("scopeInstance", new ScopeInstanceContext(execution));
    contexts.put("scope", new ScopeContext(execution));
  }

  public void add(String name, Context context) {
    if (context!=null) {
      contexts.put(name, context);
    }
  }

  public void remove(String name) {
    contexts.remove(name);
  }

  @Override
  public TypedValue get(String key) {
    Context context1 = contexts.get(key);
    if (context1!=null) {
      return new TypedValue(ContextType.INSTANCE, context1);
    }
    for (Context context2: contexts.values()) {
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
}
