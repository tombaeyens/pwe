package ch03.engine;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ch03.data.Condition;
import ch03.data.Converter;
import ch03.data.InputExpression;
import ch03.data.OutputExpression;
import ch03.data.Type;
import ch03.data.TypedValue;
import ch03.engine.context.ConfigurationsContext;
import ch03.engine.context.SubContext;
import ch03.engine.context.VariablesContext;
import ch03.model.Activity;
import ch03.model.ScopeInstance;
import ch03.model.Transition;
import ch03.model.Variable;
import ch03.model.VariableInstance;
import ch03.model.WorkflowInstance;
import ch03.util.Logger;


/**
 * @author Tom Baeyens
 */
public class ContextImpl implements Context {

  private static final Logger log = EngineImpl.log;

  EngineImpl engine;
  VariablesContext variablesContext;
  ConfigurationsContext configurationsContext;
  SubContext externalContext;
  List<SubContext> subContexts = new ArrayList<>();
  List<Converter> converters;
  
  public void setEngine(EngineImpl engine) {
    this.engine = engine;
  }

  public void addSubContext(int index, SubContext subContext) {
    subContexts.add(index, subContext);
  }

  public void addSubContext(SubContext subContext) {
    subContexts.add(subContext);
  }
  
  public void removeSubContext(SubContext subContext) {
    subContexts.add(subContext);
  }

  @Override
  public SubContext getExternalContext() {
    return externalContext;
  }
  
  @Override
  public Object getExternal(String key) {
    SubContext externalContext = getExternalContext();
    TypedValue typedValue = externalContext!=null ? externalContext.get(key) : null;
    return typedValue!=null ? typedValue.getValue() : null;
  }
  
  @Override
  public <T> T getExternal(Class<T> type) {
    return (T) getExternal(type.getName());
  }

  @Override
  public TypedValue getTypedValue(String key) {
    for (SubContext subContext: subContexts) {
      TypedValue typedValue = subContext.get(key);
      if (typedValue!=null) {
        return typedValue;
      }
    }
    return null;
  }

  @Override
  public TypedValue getTypedValue(InputExpression expression) {
    return expression.getTypedValue(this);
  }

  @Override
  public <T> T getValue(String key) {
    TypedValue typedValue = getTypedValue(key);
    return (T) (typedValue!=null ? typedValue.getValue(): null);
  }

  @Override
  public <T> T getValue(String key, Type type) {
    TypedValue typedValue = getTypedValue(key);
    return shoehorn(typedValue, type);
  }

  @Override
  public <T> T getValue(InputExpression expression) {
    return (T) getValue(expression, null);
  }

  @Override
  public <T> T getValue(InputExpression expression, Type type) {
    TypedValue typedValue = expression.getTypedValue(this);
    return (T) shoehorn(typedValue, type);
  }

  @Override
  public void setTypedValue(String key, TypedValue value) {
    throw new RuntimeException("Not implemented yet");
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
    Activity activity = (Activity) engine.getScopeInstance().getScope();
    if (activity.hasOutgoingTransitions()) {
      return activity.outgoingTransitions;
    }
    List<Transition> outgoingTransitionsMeetingCondition = new ArrayList<>();
    for (Transition transition: activity.outgoingTransitions) {
      if (isConditionMet(transition.condition)) {
        outgoingTransitionsMeetingCondition.add(transition);
      }
    }
    return outgoingTransitionsMeetingCondition;
  }

  @Override
  public Map<String,TypedValue> readInputs() {
    Map<String,InputExpression> inputParameters = engine.getScopeInstance().getScope().getInputParameters();
    return readInputs(inputParameters);
  }

  @Override
  public Map<String,TypedValue> readInputs(Map<String,InputExpression> inputParameters) {
    Map<String,TypedValue> inputs = new LinkedHashMap<>();
    if (inputParameters!=null) {
      for (String key : inputParameters.keySet()) {
        InputExpression inputExpression = inputParameters.get(key);
        TypedValue typedValue = inputExpression.getTypedValue(this);
        inputs.put(key, typedValue);
      }
    }
    return inputs;
  }

  @Override
  public void writeOutputs(Map<String,TypedValue> outputs) {
    ScopeInstance scopeInstance = engine.getScopeInstance();
    Map<String,OutputExpression> outputParameters = scopeInstance.getScope().getOutputParameters();
    if (outputParameters!=null) {
      for (String key : outputParameters.keySet()) {
        TypedValue typedValue = outputs.get(key);
        OutputExpression outputExpression = outputParameters.get(key);
        if (outputExpression != null) {
          outputExpression.setTypedValue(this, typedValue);
        } else {
          VariableInstance variableInstance = scopeInstance.findVariableInstanceByVariableIdRecursive(key);
          setVariableInstance(variableInstance, typedValue);
        }
      }
    }
  }

  @Override
  public void setVariableInstances(Map<String, TypedValue> typedValues) {
    for (String inputKey: typedValues.keySet()) {
      TypedValue inputValue = typedValues.get(inputKey);
      setVariableInstance(inputKey, inputValue);
    }
  }

  /** creates a variable instance in the current scope */
  @Override
  public VariableInstance createVariableInstance(String variableId, TypedValue typedValue) {
    return createVariableInstance(engine.getScopeInstance(), null, variableId, typedValue);
  }
  
  @Override
  public VariableInstance createVariableInstanceInWorkflowInstance(String variableId, TypedValue typedValue) {
    WorkflowInstance workflowInstance = engine.getScopeInstance().getWorkflowInstance();
    return createVariableInstance(workflowInstance, null, variableId, typedValue);
  }

  @Override
  public VariableInstance createVariableInstance(String variableId, TypedValue typedValue, ScopeInstance scopeInstance) {
    return createVariableInstance(scopeInstance, null, variableId, typedValue);
  }

  @Override
  public void setVariableInstance(String variableId, TypedValue typedValue) {
    ScopeInstance scopeInstance = engine.getScopeInstance();
    VariableInstance variableInstance = scopeInstance.findVariableInstanceByVariableIdRecursive(variableId);
    if (variableInstance==null) {
      variableInstance = createVariableInstanceInWorkflowInstance(variableId, typedValue);
    } else {
      setVariableInstance(variableInstance, typedValue);
    }
  }

  @Override
  public void setVariableInstance(VariableInstance variableInstance, TypedValue newValue) {
    TypedValue oldValue = variableInstance.getTypedValue();
    variableInstance.setTypedValue(newValue);
    Variable variable = variableInstance.getVariable();
    if (variable!=null) {
      log.debug("Set variable value [%s|%s] = %s", variable.getId(), variableInstance.getId(), newValue);
    } else {
      log.debug("Set variable value [%s] = %s", variableInstance.getId(), newValue);
    }
    engine.getPersistence().variableInstanceValueUpdated(variableInstance, oldValue);
  }
  
  protected void initializeVariables() {
    ScopeInstance scopeInstance = engine.getScopeInstance();
    Map<String,Variable> variables = scopeInstance.getScope().getVariables();
    if (variables!=null) {
      for (String key : variables.keySet()) {
        Variable variable = variables.get(key);
        TypedValue initialValue = null;
        if (variable.getInitialValue() != null) {
          initialValue = new TypedValue(variable.getType(), variable.getInitialValue());
        } else if (variable.getInitialValueExpression() != null) {
          initialValue = getTypedValue(variable.getInitialValueExpression());
        }
        createVariableInstance(scopeInstance, variable, variable.getId(), initialValue);
      }
    }
  }

  protected VariableInstance createVariableInstance(ScopeInstance scopeInstance, Variable variable, String variableId, TypedValue initialValue) {
    VariableInstance variableInstance = new VariableInstance();
    variableInstance.setVariable(variable);
    variableInstance.setScopeInstance(scopeInstance);
    variableInstance.setTypedValue(initialValue);
    scopeInstance.addVariableInstance(variableId, variableInstance);
    engine.getPersistence().variableInstanceCreated(variableInstance);
    if (variable!=null) {
      log.debug("Created variable instance  [%s|%s] : %s", variableInstance.getId(), variable.getId(), variable.getType());
    } else if (initialValue!=null) {
      log.debug("Created dynamic variable instance [%s] %s ", variableInstance.getId(), initialValue.getType());
    }
    return variableInstance;
  }

  /** tries to convert the given typedValue to type */
  protected <T> T shoehorn(TypedValue typedValue, Type type) {
    Object value = typedValue!=null ? typedValue.getValue() : null;
    if (value==null) {
      return null;
    }
    Type sourceType = typedValue.getType();
    if (type!=null && !type.equals(sourceType)) {
      Converter converter = findConverter(typedValue, type);
      if (converter!=null) {
        value = converter.convert(value);
      }
    }
    return (T) value;
  }

  protected Converter findConverter(TypedValue typedValue, Type type) {
    if (converters!=null) {
      for (Converter converter: converters) {
        if (converter.matches(typedValue, type)) {
          return converter;
        }
      }
    }
    return null;
  }

  public EngineImpl getEngine() {
    return engine;
  }

  
  public VariablesContext getVariablesContext() {
    return variablesContext;
  }

  
  public void setVariablesContext(VariablesContext variablesContext) {
    this.variablesContext = variablesContext;
  }

  
  public ConfigurationsContext getScopeContext() {
    return configurationsContext;
  }

  
  public void setScopeContext(ConfigurationsContext configurationsContext) {
    this.configurationsContext = configurationsContext;
  }

  
  public List<SubContext> getSubContexts() {
    return subContexts;
  }

  
  public void setSubContexts(List<SubContext> subContexts) {
    this.subContexts = subContexts;
  }

  
  public void setExternalContext(SubContext externalContext) {
    this.externalContext = externalContext;
  }
  
  public List<Converter> getConverters() {
    return converters;
  }
  
  public void setConverters(List<Converter> converters) {
    this.converters = converters;
  }
}
