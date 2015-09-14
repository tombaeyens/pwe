package ch03;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ch03.engine.Context;
import ch03.engine.Controller;
import ch03.engine.Engine;
import ch03.engine.EngineFactory;
import ch03.engine.EngineFactoryImpl;
import ch03.model.Activity;
import ch03.model.ActivityInstance;
import ch03.model.Workflow;
import ch03.model.WorkflowInstance;


/**
 * @author Tom Baeyens
 */
public class WorkflowApiTest {
  
  public static class TestActivity extends Activity {
    @Override
    public void start(ActivityInstance activityInstance, Context context, Controller controller) {
      controller.waitForExternalMessage();
    }
  }

  @Test 
  public void testSimpleSynchronousApi() {
    Workflow workflow = new Workflow();
    workflow.add("a", new TestActivity());

    WorkflowInstance workflowInstance = workflow.start();

    workflowInstance
      .findActivityInstanceByActivityIdRecursive("a")
      .handleMessage();
    
    assertTrue(workflowInstance.isEnded());
  }
  
  @Test
  public void testAsynchronousWorkflowApi() {
    EngineFactory engineFactory = new EngineFactoryImpl();

    Workflow workflow = new Workflow();
    workflow.add("a", new TestActivity());

    Engine engine = engineFactory.createEngine();

    WorkflowInstance workflowInstance = engine.startWorkfowInstanceSynchronous(workflow);
    engine.executeAsynchronousOperations();
    
    ActivityInstance activityInstance = workflowInstance
      .findActivityInstanceByActivityIdRecursive("a");

    engine.handleActivityInstanceMessage(activityInstance);
    
    assertTrue(workflowInstance.isEnded());
  }

}
