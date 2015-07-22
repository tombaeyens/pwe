package ch02;

import java.util.List;

import ch01.ActivityInstance;


/** Data Access Object for workflow instances */
public class WorkflowInstanceDAO {
  
  Db db;
  WorkflowDAO workflowDAO;

  public WorkflowInstanceDAO(Db db, WorkflowDAO workflowDAO) {
    this.db = db;
    this.workflowDAO = workflowDAO;
  }

  public void saveWorkflowInstance(DbWorkflowInstance workflowInstance) {
    Json workflowInstanceJson = toJsonWorkflowInstance(workflowInstance);
    String workflowInstanceJsonString = workflowInstanceJson.toString();
    db.save("workflowInstances", workflowInstance.getId(), workflowInstanceJsonString);
  }

  protected Json toJsonWorkflowInstance(DbWorkflowInstance workflowInstance) {
    return Json.object()
      .set("id", workflowInstance.getId())
      .set("activityInstances", toJsonActivityInstances(workflowInstance.getActivityInstances()));
  }

  private Json toJsonActivityInstances(List<ActivityInstance> activityInstances) {
    Json jsonActivityInstances = Json.array();
    for (ActivityInstance activityInstance: activityInstances) {
      jsonActivityInstances.add(toJsonActivityInstance(activityInstance));
    }
    return jsonActivityInstances;
  }

  private Json toJsonActivityInstance(ActivityInstance activityInstance) {
    return Json.object()
      .set("id", Db.convertDateToString(activityInstance.getEnd()))
      .set("activityId", activityInstance.getActivity().getId());
  }
}
