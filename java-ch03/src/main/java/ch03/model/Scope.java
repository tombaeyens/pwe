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
  public Map<String,TypedValue> configuration = new LinkedHashMap<>();
  public Map<String,InputExpression> inputParameters = new LinkedHashMap<>();
  public Map<String,OutputExpression> outputParameters = new LinkedHashMap<>();
  public Map<String,Activity> activities = new LinkedHashMap<>();
  public Map<String,Variable> variables = new LinkedHashMap<>();
  public Map<String,Object> properties = new LinkedHashMap<>();
  public List<ScopeListener> scopeListeners = new ArrayList<>();

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

  public void activityInstanceEnded(
          ActivityInstance endedActivityInstance, 
          ScopeInstance parentInstance, 
          ContextImpl executionContext,
          ControllerImpl executionController) {
    
  }
  
  public abstract boolean isActivity();
  public abstract boolean isWorkflow();

  
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

  
  public Map<String, InputExpression> getInputParameters() {
    return inputParameters;
  }

  
  public void setInputParameters(Map<String, InputExpression> inputParameters) {
    this.inputParameters = inputParameters;
  }

  
  public Map<String, OutputExpression> getOutputParameters() {
    return outputParameters;
  }

  
  public void setOutputParameters(Map<String, OutputExpression> outputParameters) {
    this.outputParameters = outputParameters;
  }

  
  public Map<String, Activity> getActivities() {
    return activities;
  }

  
  public void setActivities(Map<String, Activity> activities) {
    this.activities = activities;
  }

  
  public Map<String, Variable> getVariables() {
    return variables;
  }

  
  public void setVariables(Map<String, Variable> variables) {
    this.variables = variables;
  }

  
  public Map<String, Object> getProperties() {
    return properties;
  }
  
  public void setProperties(Map<String, Object> properties) {
    this.properties = properties;
  }

  
  public List<ScopeListener> getScopeListeners() {
    return scopeListeners;
  }

  public void setScopeListeners(List<ScopeListener> scopeListeners) {
    this.scopeListeners = scopeListeners;
  }
}
