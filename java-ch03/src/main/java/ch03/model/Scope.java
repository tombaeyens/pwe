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


public abstract class Scope {
  
  protected String id;
  protected Map<String,TypedValue> configuration;
  protected Map<String,InputExpression> inputParameters;
  protected Map<String,OutputExpression> outputParameters;
  protected Map<String,Activity> activities;
  protected List<Activity> autoStartActivities;
  protected Map<String,Variable> variables;
  protected List<ScopeListener> scopeListeners;
  protected List<Timer> timers;

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

  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }

  public Scope id(String id) {
    setId(id);
    return this;
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
      configuration = new LinkedHashMap<>();
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

  public List<Activity> findActivitiesWithoutIncomingTransitions() {
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
  
  public boolean hasActivity(String activityId) {
    return activities!=null && activities.containsKey(activityId);
  }

  public void setActivities(Map<String, Activity> activities) {
    this.activities = activities;
  }

  public <T extends Activity> T addActivity(T activity) {
    assert activity!=null;
    String activityId = activity.getId();
    assert activityId!=null;
    assert !hasActivity(activityId);
    if (activities==null) {
      activities = new LinkedHashMap<>();
    }
    activities.put(activityId, activity);
    return activity;
  }

  public Scope activity(Activity activity) {
    addActivity(activity);
    return this;
  }
  

  public List<Activity> getAutoStartActivities() {
    return this.autoStartActivities;
  }
  public void setAutoStartActivities(List<Activity> autoStartActivities) {
    this.autoStartActivities = autoStartActivities;
  }
  public Scope autoStartActivities(List<Activity> autoStartActivities) {
    this.autoStartActivities = autoStartActivities;
    return this;
  }
  public Scope autoStartActivity(Activity activity) {
    activity(activity);
    if (autoStartActivities==null) {
      autoStartActivities = new ArrayList<>();
    }
    autoStartActivities.add(activity);
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
  
  public List<Timer> getTimers() {
    return this.timers;
  }
  public void setTimers(List<Timer> timers) {
    this.timers = timers;
  }
  public Scope timer(Timer timer) {
    if (timers==null) {
      timers = new ArrayList<>();
    }
    timers.add(timer);
    return this;
  }
}
