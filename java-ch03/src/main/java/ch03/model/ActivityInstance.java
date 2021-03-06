package ch03.model;

import java.util.Map;

import ch03.data.TypedValue;
import ch03.engine.Engine;


/**
 * @author Tom Baeyens
 */
public class ActivityInstance extends ScopeInstance {
  
  protected Activity activity;
  
  public void message() {
    message(null);
  }
  
  public void message(Map<String,TypedValue> messageData) {
    Engine engine = getWorkflowInstance().getEngine();
    engine.message(this, messageData);
    engine.endWork();
  }
  
  @Override
  public boolean isActivityInstance() {
    return true;
  }
  
  @Override
  public boolean isWorkflowInstance() {
    return false;
  }

  @Override
  public WorkflowInstance getWorkflowInstance() {
    return parent.getWorkflowInstance();
  }

  public Activity getActivity() {
    return activity;
  }
  
  public void setActivity(Activity activity) {
    this.activity = activity;
  }

  public String toString() {
    return "("+activity.getId()+"|"+activity.getTypeName()+"|"+id+")"; 
  }
}
