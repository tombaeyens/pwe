package ch03.model;


/**
 * @author Tom Baeyens
 */
public class ActivityInstance extends ScopeInstance {
  
  protected Activity activity;
  
  public void handleMessage() {
    
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
}
