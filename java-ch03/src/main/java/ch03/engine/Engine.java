package ch03.engine;

import java.util.LinkedList;
import java.util.Map;

import ch03.data.TypedValue;
import ch03.engine.operation.Operation;
import ch03.model.Activity;
import ch03.model.ActivityInstance;
import ch03.model.ScopeInstance;
import ch03.model.Workflow;
import ch03.model.WorkflowInstance;

/**
 * @author Tom Baeyens
 */
public interface Engine {

  WorkflowInstance startWorkflowInstance(Workflow workflow);

  WorkflowInstance startWorkflowInstance(Workflow workflow, Map<String, TypedValue> workflowStartData);

  WorkflowInstance message(ActivityInstance activityInstance);
  
  WorkflowInstance message(ActivityInstance activityInstance, Map<String,TypedValue> messageData);
  
  WorkflowInstance startActivityInstance(ScopeInstance parentScopeInstance, Activity activity, Map<String, TypedValue> activityStartData);

  WorkflowInstance endScopeInstance(ScopeInstance scopeInstance);

  /** Resumes a crashed workflow instance after being restored 
   * from persistence. */  
  void resume(WorkflowInstance workflowInstance, LinkedList<Operation> operations);

  void endWork();
}