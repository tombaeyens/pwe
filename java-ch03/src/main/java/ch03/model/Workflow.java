package ch03.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch03.data.TypedValue;
import ch03.engine.Context;
import ch03.engine.ContextImpl;
import ch03.engine.Controller;
import ch03.engine.ControllerImpl;
import ch03.engine.Engine;
import ch03.engine.EngineFactory;
import ch03.engine.EngineFactoryImpl;
import ch03.engine.context.MapContext;


/**
 * @author Tom Baeyens
 */
public class Workflow extends Scope {

  protected EngineFactory engineFactory;
  protected List<Activity> startActivities = null;
  
  public WorkflowInstance start() {
    return start(null, null);
  }
  
  public WorkflowInstance start(Map<String, TypedValue> startData) {
    return start(startData, null);
  }

  public WorkflowInstance start(Map<String, TypedValue> startData, List<Activity> startActivities) {
    return getEngineFactory()
            .createEngine()
            .getController()
            .startWorkfowInstance(this, startData, startActivities);
  }
  
  public List<Activity> getStartActivities() {
    if (this.startActivities==null) {
      // I recall reading that this kind of initialization synchronization is not 100% threadsafe.
      // I don't recall what the proper solution was
      // But I think it will be sufficient
      synchronized (this) {
        List<Activity> startActivities = new ArrayList<>();
        for (Activity activity : activities.values()) {
          if (activity.incomingTransitions.isEmpty()) {
            startActivities.add(activity);
          }
        }
        this.startActivities = startActivities;
      }
    }
    return startActivities;
  }

  public void onwards(WorkflowInstance workflowInstance, Context context, Controller controller) {
  }

  public String toString() {
    return "["+(id!=null?id+"]":"workflow]"); 
  }

  @Override
  public boolean isActivity() {
    return false;
  }
  
  @Override
  public boolean isWorkflow() {
    return true;
  }

  public void setStartActivities(List<Activity> startActivities) {
    this.startActivities = startActivities;
  }

  public EngineFactory getEngineFactory() {
    return this.engineFactory;
  }

  public void setEngineFactory(EngineFactory engineFactory) {
    if (engineFactory==null) {
      engineFactory = new EngineFactoryImpl();
    }
    this.engineFactory = engineFactory;
  }
}
