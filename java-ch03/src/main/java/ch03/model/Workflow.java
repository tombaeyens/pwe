package ch03.model;

import java.util.List;
import java.util.Map;

import ch03.data.InputExpression;
import ch03.data.OutputExpression;
import ch03.data.TypedValue;
import ch03.engine.Context;
import ch03.engine.Controller;
import ch03.engine.Engine;
import ch03.engine.EngineFactory;
import ch03.engine.EngineFactoryImpl;
import ch03.engine.ScopeListener;


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
    return engine.startWorkfowInstance(this, startData, startActivities);
  }
  
  public List<Activity> getStartActivities() {
    if (this.startActivities==null) {
      synchronized (this) {
        this.startActivities = getActivitiesWithoutIncomingTransitions();
      }
    }
    return startActivities;
  }

  public void onwards(WorkflowInstance workflowInstance, Context context, Controller controller) {
    // TODO if there is one, continue super activity instance for 
    // which this workflow instance is is a sub worklflow instance
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
  
  @Override
  public Workflow configurationValue(String key, Object value) {
    super.configurationValue(key, value);
    return this;
  }

  @Override
  public Workflow configurationTypedValue(String key, TypedValue typedValue) {
    super.configurationTypedValue(key, typedValue);
    return this;
  }
  
  @Override
  public Workflow inputParameter(String key, InputExpression inputExpression) {
    super.inputParameter(key, inputExpression);
    return this;
  }

  @Override
  public Workflow outputParameter(String key, OutputExpression outputExpression) {
    super.outputParameter(key, outputExpression);
    return this;
  }

  @Override
  public Workflow activity(Activity activity) {
    super.activity(activity);
    return this;
  }

  @Override
  public Workflow variable(Variable variable) {
    super.variable(variable);
    return this;
  }

  @Override
  public Workflow scopeListener(ScopeListener scopeListener) {
    super.scopeListener(scopeListener);
    return this;
  }
}
