package ch03.model;

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
  
  /** beware, when using a real asynchronizer, the returned workflow instance can be 
   * changed concurrently by asynchronous work.
   * A safe copy can be obtained only right before asynchronous work is started. */
  public WorkflowInstance start() {
    return start(null);
  }
  
  public WorkflowInstance start(Map<String, TypedValue> startData) {
    Engine engine = getEngineFactory().createEngine();
    WorkflowInstance workflowInstance = engine.startWorkflowInstance(this, startData);
    engine.endWork();
    return workflowInstance;
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

  public Workflow engineFactory(EngineFactory engineFactory) {
    setEngineFactory(engineFactory);
    return this;
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
  public Workflow id(String id) {
    return (Workflow) super.id(id);
  }

  @Override
  public Workflow configurationValue(String key, Object value) {
    return (Workflow) super.configurationValue(key, value);
  }

  @Override
  public Workflow configurationTypedValue(String key, TypedValue typedValue) {
    return (Workflow) super.configurationTypedValue(key, typedValue);
  }
  
  @Override
  public Workflow inputParameter(String key, InputExpression inputExpression) {
    return (Workflow) super.inputParameter(key, inputExpression);
  }

  @Override
  public Workflow outputParameter(String key, OutputExpression outputExpression) {
    return (Workflow) super.outputParameter(key, outputExpression);
  }

  @Override
  public Workflow activity(Activity activity) {
    return (Workflow) super.activity(activity);
  }

  @Override
  public Workflow autoStartActivity(Activity activity) {
    return (Workflow) super.autoStartActivity(activity);
  }

  @Override
  public Workflow variable(Variable variable) {
    return (Workflow) super.variable(variable);
  }

  @Override
  public Workflow scopeListener(ScopeListener scopeListener) {
    return (Workflow) super.scopeListener(scopeListener);
  }

  @Override
  public Workflow timer(Timer timer) {
    return (Workflow) super.timer(timer);
  }
}
