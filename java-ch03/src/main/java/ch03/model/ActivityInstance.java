package ch03.model;

import java.util.Map;

import ch03.data.TypedValue;
import ch03.engine.Engine;


/**
 * @author Tom Baeyens
 */
public class ActivityInstance extends ScopeInstance {
  
  protected Activity activity;
  
  public WorkflowInstance handleMessage() {
    return handleMessage(null);
  }
  public WorkflowInstance handleMessage(Map<String,TypedValue> messageData) {
    Engine engine = getWorkflowInstance().getEngine();
    WorkflowInstance handleActivityInstanceMessage = engine.handleActivityInstanceMessage(this, messageData);
    engine.executeAsynchronousOperations();
    return handleActivityInstanceMessage;
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
