package ch03;

import java.util.LinkedHashMap;
import java.util.Map;

import ch03.data.ContextType;
import ch03.data.TypedValue;


public class ExecutionControllerContext implements Context {
  
  ExecutionController executionController;
  Map<String,Context> contexts = new LinkedHashMap<>();
  
  public ExecutionControllerContext(final ExecutionController executionController) {
    contexts.put("scopeInstance", new ScopeInstanceContext(executionController));
    contexts.put("scope", new ScopeContext(executionController));
    contexts.put("external", executionController.externalContext);
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
