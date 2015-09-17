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
  public void testSimpleApi() {
    Workflow workflow = new Workflow();
    workflow.add("a", new TestActivity());

    WorkflowInstance workflowInstance = workflow.start();

    workflowInstance
      .findActivityInstanceByActivityIdRecursive("a")
      .message();
    
    assertTrue(workflowInstance.isEnded());
  }
  
  @Test
  public void testCustomEngineFactory() {
    EngineFactory engineFactory = new EngineFactoryImpl();

    Workflow workflow = new Workflow();
    // Simplest way to set the engine factory is to set it in 
    // the worklfow.  There it will be found by Workflow.start
    // and ActivityInstance.message
    workflow.setEngineFactory(engineFactory);
    workflow.add("a", new TestActivity());

    workflow.start();
    
    // Another alternative is to create your own engine and 
    // then start the workflow instance on the engine itself.
    // This can be handy if you want to configure the engine
    Engine engine = engineFactory.createEngine();
    WorkflowInstance workflowInstance = engine.startWorkfowInstance(workflow);
    
    ActivityInstance activityInstance = workflowInstance
      .findActivityInstanceByActivityIdRecursive("a");

    // The downside of using the engine is that you have to 
    // use it everywhere you interact.
    engine.message(activityInstance);
    
    assertTrue(workflowInstance.isEnded());
  }
}
