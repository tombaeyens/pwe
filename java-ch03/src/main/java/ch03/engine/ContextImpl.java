package ch03.engine;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ch03.data.Condition;
import ch03.data.InputExpression;
import ch03.data.OutputExpression;
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
  
  public void setEngine(EngineImpl engine) {
    this.engine = engine;
    initializeVariablesContext();
    initializeConfigurationsContext();
  }

  protected void initializeVariablesContext() {
    addSubContext(new VariablesContext(engine));
  }
  
  protected void initializeConfigurationsContext() {
    addSubContext(new ConfigurationsContext(engine));
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
    return externalContext!=null ? externalContext.get(key) : null;
  }
  
  @Override
  public <T> T getExternal(Class<T> type) {
    return (T) getExternal(type.getName());
  }

  @Override
  public TypedValue get(String key) {
    for (SubContext subContext: subContexts) {
      TypedValue typedValue = subContext.get(key);
      if (typedValue!=null) {
        return typedValue;
      }
    }
    return null;
  }

  @Override
  public void set(String key, TypedValue value) {
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
    if (activity.outgoingTransitions.isEmpty()) {
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
  
  public Map<String,TypedValue> readInputs() {
    Map<String,TypedValue> inputs = new LinkedHashMap<>();
    Map<String,InputExpression> inputParameters = engine.getScopeInstance().getScope().getInputParameters();
    for (String key: inputParameters.keySet()) {
      InputExpression inputExpression = inputParameters.get(key);
      TypedValue typedValue = inputExpression.getTypedValue(this);
      inputs.put(key, typedValue);
    }
    return inputs;
  }

  public void writeOutputs(Map<String,TypedValue> outputs) {
    ScopeInstance scopeInstance = engine.getScopeInstance();
    Map<String,OutputExpression> outputParameters = scopeInstance.getScope().getOutputParameters();
    for (String key: outputParameters.keySet()) {
      TypedValue typedValue = outputs.get(key);
      OutputExpression outputExpression = outputParameters.get(key);
      if (outputExpression!=null) {
        outputExpression.setTypedValue(this, typedValue);
      } else {
        VariableInstance variableInstance = scopeInstance.findVariableInstanceByVariableIdRecursive(key);
        setVariableInstanceValue(variableInstance, typedValue);
      }
    }
  }
  
  public void initializeVariables() {
    ScopeInstance scopeInstance = engine.getScopeInstance();
    Map<String,Variable> variables = scopeInstance.getScope().getVariables();
    for (String key: variables.keySet()) {
      Variable variable = variables.get(key);
      TypedValue initialValue = null;
      if (variable.getInitialValue()!=null) {
        initialValue = new TypedValue(variable.getType(), variable.getInitialValue());
      } else if (variable.getInitialValueExpression()!=null) {
        initialValue = getTypedValue(variable.getInitialValueExpression()); 
      }
      createVariableInstance(scopeInstance, variable, variable.getId(), initialValue);
    }
  }

  /** creates a variable instance in the current scope */
  public VariableInstance createVariableInstance(String variableId, TypedValue typedValue) {
    return createVariableInstance(engine.getScopeInstance(), null, variableId, typedValue);
  }

  public VariableInstance createVariableInstance(ScopeInstance scopeInstance, Variable variable, String variableId, TypedValue initialValue) {
    VariableInstance variableInstance = engine.instantiateVariableInstance();
    variableInstance.setVariable(variable);
    variableInstance.setScopeInstance(scopeInstance);
    variableInstance.setTypedValue(initialValue);
    scopeInstance.getVariableInstances().put(variableId, variableInstance);
    engine.getPersistence().variableInstanceCreated(variableInstance);
    if (variable!=null) {
      log.debug("Created variable instance  [%s|%s] : %s", variableInstance.getId(), variable.getId(), variable.getType());
    } else if (initialValue!=null) {
      log.debug("Created dynamic variable instance [%s] %s ", variableInstance.getId(), initialValue.getType());
    }
    return variableInstance;
  }


  public void setVariableInstanceValue(String variableId, TypedValue typedValue) {
    ScopeInstance scopeInstance = engine.getScopeInstance();
    VariableInstance variableInstance = scopeInstance.findVariableInstanceByVariableIdRecursive(variableId);
    if (variableInstance==null) {
      variableInstance = createVariableInstanceDynamic(variableId, typedValue);
    } else {
      setVariableInstanceValue(variableInstance, typedValue);
    }
  }

  protected VariableInstance createVariableInstanceDynamic(String variableId, TypedValue typedValue) {
    WorkflowInstance workflowInstance = engine.getScopeInstance().getWorkflowInstance();
    return createVariableInstance(workflowInstance, null, variableId, typedValue);
  }

  protected void setVariableInstanceValue(VariableInstance variableInstance, TypedValue newValue) {
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
  

  public TypedValue getTypedValue(String key) {
    return get(key);
  }

  public Object getValue(String key) {
    TypedValue typedValue = get(key);
    return typedValue!=null ? typedValue.getValue(): null;
  }

  public TypedValue getTypedValue(InputExpression expression) {
    return expression.getTypedValue(this);
  }

  public Object getValue(InputExpression expression) {
    TypedValue typedValue = expression.getTypedValue(this);
    return typedValue!=null ? typedValue.getValue() : null;
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
}
