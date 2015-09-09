package ch03.model;

import java.util.LinkedHashMap;
import java.util.Map;

import ch03.data.InputExpression;
import ch03.data.OutputExpression;
import ch03.data.TypedValue;
import ch03.engine.ExecutionContextImpl;
import ch03.engine.ExecutionControllerImpl;
import ch03.util.ApiException;


public abstract class Scope {
  
  public String id;
  public Map<String,TypedValue> configuration = new LinkedHashMap<>();
  public Map<String,InputExpression> inputParameters = new LinkedHashMap<>();
  public Map<String,OutputExpression> outputParameters = new LinkedHashMap<>();
  public Map<String,Activity> activities = new LinkedHashMap<>();
  public Map<String,Variable> variables = new LinkedHashMap<>();
  public Map<String,Object> properties = new LinkedHashMap<>();

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
          ExecutionContextImpl executionContext,
          ExecutionControllerImpl executionController) {
    
  }
}
