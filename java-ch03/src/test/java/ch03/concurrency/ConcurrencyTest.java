package ch03.concurrency;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ch03.concurrency.infrastructure.ExternalSync;
import ch03.concurrency.infrastructure.InternalAsync;
import ch03.concurrency.infrastructure.TestEngineFactory;
import ch03.model.Workflow;
import ch03.model.WorkflowInstance;


/**
 * @author Tom Baeyens
 */
public class ConcurrencyTest {
  
  @Test
  public void test() {
    TestEngineFactory engineFactory = new TestEngineFactory();

    Workflow workflow = new Workflow();
    workflow.setEngineFactory(engineFactory);
    ExternalSync externalSync = workflow.add("externalSync", new ExternalSync());
    InternalAsync internalAsync = workflow.add("internalAsync", new InternalAsync());
    externalSync.createTransitionTo(internalAsync);

    WorkflowInstance workflowInstance = workflow.start();
    
    workflowInstance
      .findActivityInstanceByActivityIdRecursive("externalSync")
      .handleMessage();
    
    assertTrue(workflowInstance.isEnded());
    
    System.out.println(engineFactory.getLogs());
  }

//  public static Log log(String threadId, String message) {
//    return new Log(threadId, message);
//  }
}
