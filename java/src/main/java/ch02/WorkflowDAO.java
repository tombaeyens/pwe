package ch02;

import java.util.Collection;
import java.util.List;

import ch01.Activity;
import ch01.Transition;


/** Data Access Object for workflows */
public class WorkflowDAO {

  Db db;

  public WorkflowDAO(Db db) {
    this.db = db;
  }
  
  public void saveWorkflow(DbWorkflow workflow) {
    Json workflowJson = toJsonWorkflow(workflow);
    String workflowJsonString = workflowJson.toString();
    System.out.println(workflowJsonString);
    db.save("workflows", workflow.getId(), workflowJsonString);
  }

  public DbWorkflow findWorkflowById(String workflowId) {
    String workflowJsonString = db.find("workflows", workflowId);
    return deserializeWorkflow(workflowJsonString);
  }
  
  protected Json toJsonWorkflow(DbWorkflow workflow) {
    return Json.object()
      .set("id", workflow.getId())
      .set("activities", toJsonActivities(workflow.getActivities().values()));
  }

  protected Json toJsonActivities(Collection<Activity> activities) {
    Json jsonActivities = Json.array();
    for (Activity activity: activities) {
      jsonActivities.add(toJsonActivity(activity));
    }
    return jsonActivities;
  }

  protected Json toJsonActivity(Activity activity) {
    Json activityJson = Json.object()
      .set("id", activity.getId())
      .set("type", activity.getClass().getName())
      .set("transitions", toJsonTransitions(activity.getOutgoingTransitions()));
    if (activity instanceof Configurable) {
      Json configurationJson = Json.object();
      ((Configurable)activityJson).writeConfiguration(configurationJson);
      activityJson.set("configuration", configurationJson);
    }
    return activityJson;
  }

  protected Json toJsonTransitions(List<Transition> transitions) {
    Json jsonTransitions = Json.array();
    for (Transition transition: transitions) {
      jsonTransitions.add(toJsonTransition(transition));
    }
    return jsonTransitions;
  }

  protected Json toJsonTransition(Transition transition) {
    return Json.object()
      .set("to", transition.getTo().getId());
  }

  protected DbWorkflow deserializeWorkflow(String serializedWorkflow) {
    return null;
  }
}
