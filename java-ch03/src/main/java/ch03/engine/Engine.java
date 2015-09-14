package ch03.engine;

import java.util.List;
import java.util.Map;

import ch03.data.TypedValue;
import ch03.model.Activity;
import ch03.model.ActivityInstance;
import ch03.model.Workflow;
import ch03.model.WorkflowInstance;

/**
 * @author Tom Baeyens
 */
public interface Engine {

  WorkflowInstance startWorkfowInstanceSynchronous(Workflow workflow);

  WorkflowInstance startWorkfowInstanceSynchronous(Workflow workflow, Map<String, TypedValue> startData);

  WorkflowInstance startWorkfowInstanceSynchronous(Workflow workflow, Map<String, TypedValue> startData, List<Activity> startActivities);

  WorkflowInstance handleActivityInstanceMessage(ActivityInstance activityInstance);
  
  WorkflowInstance handleActivityInstanceMessage(ActivityInstance activityInstance, Map<String,TypedValue> messageData);

  void executeAsynchronousOperations();
}