package ch03;

import static org.junit.Assert.*;

import org.junit.Test;

import ch03.engine.Context;
import ch03.engine.Controller;
import ch03.engine.Engine;
import ch03.engine.EngineFactory;
import ch03.engine.EngineFactoryImpl;
import ch03.engine.PersistenceImpl;
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
    // @formatter:off
    Workflow workflow = new Workflow()
     .autoStartActivity(new TestActivity()
       .id("t"));
    // @formatter:on

    WorkflowInstance workflowInstance = workflow.start();

    ActivityInstance activityInstance = workflowInstance.findActivityInstanceByActivityIdRecursive("t");
    activityInstance.message();

    assertTrue(workflowInstance.isEnded());
  }

  @Test
  public void testCustomEngineFactory() {
    EngineFactory engineFactory = createCustomizedEngineFactory();

    // Simplest way to set the engine factory is to set it in
    // the worklfow. There it will be found by Workflow.start
    // and ActivityInstance.message
    // @formatter:off
    Workflow workflow = new Workflow()
      .engineFactory(engineFactory)
      .autoStartActivity(new TestActivity()
        .id("t"));
    // @formatter:on

    // Uses the custom engine set in the workflow:
    WorkflowInstance workflowInstance = workflow.start();

    ActivityInstance activityInstance = workflowInstance.findActivityInstanceByActivityIdRecursive("t");
    activityInstance.message();
  }

  @Test
  public void testDirectEngineUsageToCombineMultipleOperations() {
    EngineFactory engineFactory = new EngineFactoryImpl();
    
    Activity t1 = new TestActivity()
      .id("t1");
    Activity t2 = new TestActivity()
      .id("t2");
    Workflow workflow = new Workflow()
      // Note these activities are NOT auto starting activities
      .activity(t1)
      // Note these activities are NOT auto starting activities
      .activity(t2);

    Engine engine = engineFactory.createEngine();
    WorkflowInstance workflowInstance = engine.startWorkflowInstance(workflow);
    engine.startActivityInstance(workflowInstance, t1, null);
    engine.startActivityInstance(workflowInstance, t2, null);
    engine.endWork();

    assertNotNull(workflowInstance.findActivityInstanceByActivityIdRecursive("t1"));
    assertNotNull(workflowInstance.findActivityInstanceByActivityIdRecursive("t2"));
  }
    
  protected EngineFactoryImpl createCustomizedEngineFactory() {
    return new EngineFactoryImpl() {
      @Override
      protected PersistenceImpl instantiatePersistence() {
        return new PersistenceImpl(){
          @Override
          protected void addUpdate(String update) {
            super.addUpdate("Customized "+update);
          }
        };
      }
    };
  }
}
