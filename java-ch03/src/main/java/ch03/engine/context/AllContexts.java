package ch03.engine.context;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ch03.data.TypedValue;
import ch03.data.types.ContextType;
import ch03.engine.Execution;


/**
 * @author Tom Baeyens
 */
public class AllContexts implements Context {
  
  public static final String KEY_CONFIGURATIONS = "configurations";
  public static final String KEY_VARIABLES = "variables";
  public static final String KEY_EXTERNAL = "external";
  
  Execution execution;
  ScopeInstanceContext scopeInstanceContext;
  ScopeContext scopeContext;
  Context externalContext;
  Map<String,Context> contexts;
  List<String> searchOrder = null;
  
  public AllContexts(Execution execution) {
    this.execution = execution;
    scopeInstanceContext = new ScopeInstanceContext(execution);
    scopeContext = new ScopeContext(execution);
    contexts = new LinkedHashMap<>();
    contexts.put(KEY_VARIABLES, scopeInstanceContext);
    contexts.put(KEY_CONFIGURATIONS, scopeContext);
  }
  
  public void addExternal(Context externalContext) {
    this.externalContext = externalContext;
    add(KEY_EXTERNAL, externalContext);
  }
  
  public Context getExternalContext() {
    return externalContext;
  }

  public void add(String name, Context context) {
    if (context!=null) {
      if (searchOrder!=null && !contexts.containsKey(name)) {
        searchOrder.add(name);
      }
      contexts.put(name, context);
    }
  }

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
}
