package ch03.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ch03.engine.state.ExecutionState;
import ch03.util.ApiException;


/**
 * @author Tom Baeyens
 */
public abstract class ScopeInstance {
  
  protected String id;
  protected Scope scope;
  protected ScopeInstance parent;
  protected List<ActivityInstance> activityInstances = new ArrayList<>();
  /** variable instances by variable.id (not by variableInstance.id) */
  protected Map<String,VariableInstance> variableInstances = new LinkedHashMap<>();
  protected ExecutionState state;
  
  public abstract boolean isActivityInstance();
  public abstract boolean isWorkflowInstance();
  public abstract WorkflowInstance getWorkflowInstance();
  
  public boolean isEnded() {
    return state.isEnded();
  }

  public ScopeInstance getParent() {
    return parent;
  }
  
  /** searches for the activity instance and recurses over the nested activity instances. */
  public ActivityInstance findActivityInstanceByActivityIdRecursive(String activityId) {
    ApiException.checkNotNullParameter(activityId, "activityId");
    for (ActivityInstance activityInstance: activityInstances) {
      if (activityId.equals(activityInstance.activity.id)) {
        return activityInstance;
      }
      ActivityInstance theOne = activityInstance.findActivityInstanceByActivityIdRecursive(activityId);
      if (theOne!=null) {
        return theOne;
      }
    }
    return null;
  }

  /** searches for the activity instance and recurses over the nested activity instances. */
  public ActivityInstance findActivityInstanceByActivityInstanceId(String activityInstanceId) {
    ApiException.checkNotNullParameter(activityInstanceId, "activityInstanceId");
    for (ActivityInstance activityInstance: activityInstances) {
      if (activityInstanceId.equals(activityInstance.id)) {
        return activityInstance;
      }
      ActivityInstance theOne = activityInstance.findActivityInstanceByActivityInstanceId(activityInstanceId);
      if (theOne!=null) {
        return theOne;
      }
    }
    return null;
  }
  
  public List<ActivityInstance> getOpenActivityInstances() {
    List<ActivityInstance> activityInstances = new ArrayList<>();
    for (ActivityInstance activityInstance: activityInstances) {
      if (!activityInstance.isEnded()) {
        activityInstances.add(activityInstance);
      }
    }
    return activityInstances;
  }

  /** searches for the variable instance and recurses over the parents. */
  public VariableInstance findVariableInstanceByVariableIdRecursive(String variableId) {
    VariableInstance variableInstance = variableInstances.get(variableId);
    if (variableInstance!=null) {
      return variableInstance;
    }
    if (parent!=null) {
      return parent.findVariableInstanceByVariableIdRecursive(variableId);
    }
    return null;
  }
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public Scope getScope() {
    return scope;
  }
  
  public void setScope(Scope scope) {
    this.scope = scope;
  }
  
  public List<ActivityInstance> getActivityInstances() {
    return activityInstances;
  }
  
  public void setActivityInstances(List<ActivityInstance> activityInstances) {
    this.activityInstances = activityInstances;
  }
  
  public Map<String, VariableInstance> getVariableInstances() {
    return variableInstances;
  }
  
  public void setVariableInstances(Map<String, VariableInstance> variableInstances) {
    this.variableInstances = variableInstances;
  }
  
  public ExecutionState getState() {
    return state;
  }
  
  public void setState(ExecutionState state) {
    this.state = state;
  }
  
  public void setParent(ScopeInstance parent) {
    this.parent = parent;
  }
}
