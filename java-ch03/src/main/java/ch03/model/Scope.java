package ch03.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ch03.data.InputExpression;
import ch03.data.OutputExpression;
import ch03.data.TypedValue;
import ch03.engine.ContextImpl;
import ch03.engine.ControllerImpl;
import ch03.engine.ScopeListener;
import ch03.util.ApiException;


public abstract class Scope {
  
  public String id;
  public Map<String,TypedValue> configuration = null;
  public Map<String,InputExpression> inputParameters = null;
  public Map<String,OutputExpression> outputParameters = null;
  public Map<String,Activity> activities = null;
  public Map<String,Variable> variables = null;
  public List<ScopeListener> scopeListeners = null;

  public <T extends Activity> T add(String activityId, T activity) {
    ApiException.checkNotNullParameter(activityId, "activityId");
    ApiException.checkTrue(!activities.containsKey(activityId), "Scope '%s' already contains an activity with id '%s'", id, activityId);
    ApiException.checkNotNullParameter(activity, "activity");
    assert !activities.containsKey(activityId);
    assert activity!=null;
    activity.id = activityId;
    activities.put(activityId, activity);
    return activity;
  }

  public void flowEnded(
          ScopeInstance scopeInstance, 
          ActivityInstance endedActivityInstance, 
          ContextImpl context,
          ControllerImpl controller) {
    if (scopeInstance.getOpenActivityInstances().isEmpty()) {
      controller.endScopeInstance();
      controller.notifyParentFlowEnded();
    }
  }
  
  public abstract boolean isActivity();
  public abstract boolean isWorkflow();

  public List<Activity> getActivitiesWithoutIncomingTransitions() {
    List<Activity> startActivities = new ArrayList<>();
    if (activities!=null) {
      for (Activity activity : activities.values()) {
        if (activity.incomingTransitions.isEmpty()) {
          startActivities.add(activity);
        }
      }
    }
    return startActivities;
  }

  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }

  
  public Map<String, TypedValue> getConfiguration() {
    return configuration;
  }

  
  public void setConfiguration(Map<String, TypedValue> configuration) {
    this.configuration = configuration;
  }

  public Scope configurationValue(String key, Object value) {
    configurationTypedValue(key, new TypedValue(null, value));
    return this;
  }

  public Scope configurationTypedValue(String key, TypedValue typedValue) {
    assert key != null;
    if (configuration==null) {
      configuration.put(key, typedValue);
    }
    configuration.put(key, typedValue);
    return this;
  }

  public Map<String, InputExpression> getInputParameters() {
    return inputParameters;
  }

  
  public void setInputParameters(Map<String, InputExpression> inputParameters) {
    this.inputParameters = inputParameters;
  }

  public Scope inputParameter(String key, InputExpression inputExpression) {
    assert inputExpression != null;
    if (inputParameters==null) {
      inputParameters = new LinkedHashMap<>();
    }
    inputParameters.put(key, inputExpression);
    return this;
  }
  
  public Map<String, OutputExpression> getOutputParameters() {
    return outputParameters;
  }

  
  public void setOutputParameters(Map<String, OutputExpression> outputParameters) {
    this.outputParameters = outputParameters;
  }

  public Scope outputParameter(String key, OutputExpression outputExpression) {
    assert outputExpression != null;
    if (outputParameters==null) {
      outputParameters = new LinkedHashMap<>();
    }
    outputParameters.put(key, outputExpression);
    return this;
  }
  
  public Map<String, Activity> getActivities() {
    return activities;
  }

  
  public void setActivities(Map<String, Activity> activities) {
    this.activities = activities;
  }
  
  public Scope activity(Activity activity) {
    String activityId = activity.getId();
    assert activityId != null;
    if (activities==null) {
      activities = new LinkedHashMap<>();
    }
    activities.put(activityId, activity);
    return this;
  }
  
  public Map<String, Variable> getVariables() {
    return variables;
  }

  
  public void setVariables(Map<String, Variable> variables) {
    this.variables = variables;
  }

  public Scope variable(Variable variable) {
    String variableId = variable.getId();
    assert variableId != null;
    if (variables==null) {
      variables = new LinkedHashMap<>();
    }
    variables.put(variableId, variable);
    return this;
  }
  
  public List<ScopeListener> getScopeListeners() {
    return scopeListeners;
  }

  public void setScopeListeners(List<ScopeListener> scopeListeners) {
    this.scopeListeners = scopeListeners;
  }
  
  public Scope scopeListener(ScopeListener scopeListener) {
    assert scopeListener != null;
    if (scopeListeners==null) {
      scopeListeners = new ArrayList<>();
    }
    scopeListeners.add(scopeListener);
    return this;
  }
  
}
