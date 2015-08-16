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
public class ScopeInstance {
  
  public String id;
  public Scope scope;
  public ScopeInstance parent;
  public List<ActivityInstance> activityInstances = new ArrayList<>();
  /** variable instances by variable.id (not by variableInstance.id) */
  public Map<String,VariableInstance> variableInstances = new LinkedHashMap<>();
  public ExecutionState state;
  
  public boolean isEnded() {
    return state.isEnded();
  }

  public ScopeInstance getParent() {
    return parent;
  }
  
  public WorkflowInstance getWorkflowInstance() {
    return parent.getWorkflowInstance();
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
}
