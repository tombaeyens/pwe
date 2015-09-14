package ch03.concurrency;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ch03.concurrency.infrastructure.ExternalSync;
import ch03.concurrency.infrastructure.InternalAsync;
import ch03.concurrency.infrastructure.TestEngineFactory;
import ch03.engine.Engine;
import ch03.model.Workflow;
import ch03.model.WorkflowInstance;


/**
 * @author Tom Baeyens
 */
public class ConcurrencyTest {
  
  @Test
  public void test() {
    TestEngineFactory testEngineFactory = new TestEngineFactory();

    Workflow workflow = new Workflow();
    workflow.setEngineFactory(testEngineFactory);
    ExternalSync externalSync = workflow.add("externalSync", new ExternalSync());
    InternalAsync internalAsync = workflow.add("internalAsync", new InternalAsync());
    externalSync.createTransitionTo(internalAsync);

    Engine engine = testEngineFactory.createEngine();
    WorkflowInstance workflowInstance = engine.startWorkfowInstanceSynchronous(workflow);
    engine.executeAsynchronousOperations();

    
    workflowInstance
      .findActivityInstanceByActivityIdRecursive("externalSync")
      .handleMessage();
    
    assertTrue(workflowInstance.isEnded());

//    List<Log> logs = testEngineFactory.getLogs();
//    assertEquals(logs.toString(), 1, logs.size());
  }
}
