package ch02;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
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
  
  /** serializes the workflow and stores the serialized 
   * form in the db. */
  public void saveWorkflow(DbWorkflow workflow) {
    String workflowJsonString = workflowToJsonString(workflow);
    db.save("workflows", workflow.getId(), workflowJsonString);
  }

  /** finds the deserialized workflow in the db, deserializes it
   * and returns the workflow object. */
  public DbWorkflow findWorkflowById(String workflowId) {
    String workflowJsonString = db.find("workflows", workflowId);
    return jsonStringToWorkflow(workflowJsonString);
  }

  /** serializes a workflow object to a json string */
  public static String workflowToJsonString(DbWorkflow workflow) {
    return Json.object()
      .set("id", workflow.getId())
      .set("activities", activitiesToJson(workflow.getActivities().values()))
      .toString();
  }

  /** deserializes a json string to a workflow object */
  public static DbWorkflow jsonStringToWorkflow(String workflowJson) {
    Json json = Json.read(workflowJson);
    DbWorkflow workflow = new DbWorkflow();
    workflow.setId(json.at("id").asString());
    Json activitiesJson = json.at("activities");
    workflow.setActivities(jsonToActivities(activitiesJson));
    parseTransitions(activitiesJson, workflow);
    return workflow;
  }

  protected static Json activitiesToJson(Collection<Activity> activities) {
    Json jsonActivities = Json.array();
    for (Activity activity: activities) {
      jsonActivities.add(activityToJson(activity));
    }
    return jsonActivities;
  }

  protected static Json activityToJson(Activity activity) {
    Json activityJson = Json.object()
      .set("id", activity.getId())
      .set("type", activity.getClass().getName())
      .set("transitions", transitionsToJson(activity.getOutgoingTransitions()));
    if (activity instanceof Configurable) {
      Json configurationJson = Json.object();
      ((Configurable)activityJson).writeConfiguration(configurationJson);
      activityJson.set("configuration", configurationJson);
    }
    return activityJson;
  }

  protected static Json transitionsToJson(List<Transition> transitions) {
    Json jsonTransitions = Json.array();
    for (Transition transition: transitions) {
      jsonTransitions.add(transitionToJson(transition));
    }
    return jsonTransitions;
  }

  protected static Json transitionToJson(Transition transition) {
    return Json.object()
      .set("to", transition.getTo().getId());
  }

  protected static List<Activity> jsonToActivities(Json activitiesJson) {
    List<Activity> activities = new ArrayList<>();
    for (Json activityJson: activitiesJson.asJsonList()) {
      Activity activity = jsonToActivity(activityJson);
      activities.add(activity);
    }
    return activities;
  }

  protected static Activity jsonToActivity(Json activityJson) {
    String type = activityJson.at("type").asString();
    try {
      String activityId = activityJson.at("id").asString();
      Class<?> activityClass = (Class<?>) Class.forName(type);
      Constructor activityConstructor = activityClass.getDeclaredConstructor(new Class[]{String.class});
      Activity activity = (Activity) activityConstructor.newInstance(new Object[]{activityId}); 
      activity.setId(activityId);
      if (activity instanceof Configurable) {
        Json configurationJson = activityJson.at("configuration");
        ((Configurable)activityJson).readConfiguration(configurationJson);
      }
      return activity;
    } catch (Exception e) {
      throw new RuntimeException("Can't load activity type class "+type+": "+e, e);
    }
  }

  protected static void parseTransitions(Json activitiesJson, DbWorkflow workflow) {
    for (Json activityJson: activitiesJson.asJsonList()) {
      String activityId = activityJson.at("id").asString();
      for (Json transitionJson: activityJson.at("transitions").asJsonList())  {
        String toActivityId = transitionJson.at("to").asString();
        workflow.transition(activityId, toActivityId);
      }
    }
  }
}
