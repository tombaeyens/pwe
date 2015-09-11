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
import ch03.engine.context.MapContext;


/**
 * @author Tom Baeyens
 */
public class Workflow extends Scope {

  protected EngineFactory engineFactory;
  protected List<Activity> startActivities = null;
  
  public Workflow() {
    initializeEngineFactory();
  }

  protected void initializeEngineFactory() {
    engineFactory = new EngineFactory();
  }

  public WorkflowInstance start() {
    return start(null, null);
  }
  
  public WorkflowInstance start(Map<String, TypedValue> startData) {
    return start(startData, null);
  }

  public WorkflowInstance start(Map<String, TypedValue> startData, List<Activity> startActivities) {
    Engine engine = engineFactory.newEngine();
    ControllerImpl controller = engine.getController();
    WorkflowInstance workflowInstance = controller.createWorkfowInstance(this);
    applyStartData(engine, workflowInstance, startData);
    controller.startActivities(workflowInstance, startActivities);
    return workflowInstance;
  }
  
  protected void applyStartData(Engine engine, WorkflowInstance workflowInstance, Map<String, TypedValue> startData) {
    if (startData!=null && !startData.isEmpty() && inputParameters!=null) {
      ContextImpl context = engine.getContext();
      MapContext startDataContext = new MapContext("startData", startData);
      // adding the start data subcontext after the subcontext context
      context.addSubContext(0, startDataContext);
      Map<String, TypedValue> inputs = context.readInputs();
      context.removeSubContext(startDataContext);
      for (String inputKey: inputs.keySet()) {
        TypedValue inputValue = inputs.get(inputKey);
        context.setVariableInstanceValue(inputKey, inputValue);
      }
    }
  }

  public List<Activity> getStartActivities() {
    if (this.startActivities==null) {
      // I recall reading that this kind of initialization synchronization is not 100% threadsafe.
      // I don't recall what the proper solution was
      // But I think it will be sufficient
      synchronized (this) {
        List<Activity> startActivities = new ArrayList<>();
        for (Activity activity : activities.values()) {
          if (activity.inTransitions.isEmpty()) {
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
    this.engineFactory = engineFactory;
  }
}
