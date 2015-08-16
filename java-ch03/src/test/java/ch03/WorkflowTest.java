package ch03;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import ch03.engine.Execution;
import ch03.model.ActivityInstance;
import ch03.model.Workflow;
import ch03.model.WorkflowInstance;
import ch03.workflow.Async;
import ch03.workflow.Sync;


/**
 * @author Tom Baeyens
 */
public class WorkflowTest {

  @Test
  public void test() {
    Workflow workflow = new Workflow();
    
    Sync sync = workflow.add("sync", new Sync());
    
    Async async = workflow.add("async", new Async());

    WorkflowInstance workflowInstance = new Execution()
      .startWorkflowInstance(workflow);
    
    ActivityInstance asyncActivityInstance = workflowInstance
      .findActivityInstanceByActivityIdRecursive("async");
    assertNotNull(asyncActivityInstance);
    
    new Execution()
      .handleActivityInstanceMessage(asyncActivityInstance);
  }
}
