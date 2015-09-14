package ch03.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch03.data.TypedValue;
import ch03.engine.Context;
import ch03.engine.Controller;
import ch03.engine.Engine;
import ch03.engine.EngineFactory;
import ch03.engine.EngineFactoryImpl;


/**
 * @author Tom Baeyens
 */
public class Workflow extends Scope {

  protected boolean isStartAsynchronous;
  protected EngineFactory engineFactory;
  protected List<Activity> startActivities = null;
  
  /** beware, when using a real asynchronizer, the returned workflow instance can be 
   * changed concurrently by asynchronous work.
   * A safe copy can be obtained only right before asynchronous work is started. */
  public WorkflowInstance start() {
    return start(null, null);
  }
  
  public WorkflowInstance start(Map<String, TypedValue> startData) {
    return start(startData, null);
  }

  public WorkflowInstance start(Map<String, TypedValue> startData, List<Activity> startActivities) {
    Engine engine = getEngineFactory().createEngine();
    WorkflowInstance workflowInstance = engine.startWorkfowInstanceSynchronous(this, startData, startActivities);
    engine.executeAsynchronousOperations();
    return workflowInstance;
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
    if (engineFactory==null) {
      engineFactory = new EngineFactoryImpl();
    }
    return engineFactory;
  }

  public void setEngineFactory(EngineFactory engineFactory) {
    if (engineFactory==null) {
      engineFactory = new EngineFactoryImpl();
    }
    this.engineFactory = engineFactory;
  }

  
  public boolean isStartAsynchronous() {
    return isStartAsynchronous;
  }
  
  public void setStartAsynchronous(boolean isStartAsynchronous) {
    this.isStartAsynchronous = isStartAsynchronous;
  }

  /** does the workflow need to be saved between applying the data 
   * and starting the start activities. */
  public boolean isStartSaved() {
    return false;
  }
}
