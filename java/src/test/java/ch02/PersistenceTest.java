package ch02;

import org.junit.Test;

import ch01.AbstractTest;
import ch01.Wait;


public class PersistenceTest extends AbstractTest {

  @Test
  public void testSequentialWaitExecution() {
    WorkflowEngine workflowEngine = new WorkflowEngine();

    // +-------------+   +-------------+
    // |     a       |-->|     b       |
    // |   (Wait)    |   |   (Wait)    |
    // +-------------+   +-------------+
    DbWorkflow workflow = new DbWorkflow()
      .activity(new Wait("a"))
      .activity(new Wait("b"))
      .transition("a", "b");
    
    String workflowId = workflowEngine.deployWorkflow(workflow);
    
//    DbWorkflowInstance workflowInstance = workflowEngine.startWorkflowInstance(workflowId);
//
//    workflowInstance.start();
//
//    // We check that there is an open activity instance waiting in 'a'
//    assertWaiting(workflowInstance, "a");
//    // We check that there are no ended activity instances
//    assertEnded(workflowInstance);
//    
//    // We now take the single activity instance in the workflowInstance
//    ActivityInstance activityInstance = workflowInstance.getActivityInstances().get(0);
//    // just to be safe, we double check it is the one positioned in 'a'
//    assertEquals("a", activityInstance.getActivity().getId());
//    
//    db.deployWorkflow(workflowInstance);
//
//    // long time...
//    
//    workflowInstance = db.loadWorkflowInstance(workflowInstanceId, activityInstanceId);
//    
//    activityInstance = workflowInstance.getActivityInstances().get(0);
//    assertEquals("a", activityInstance.getActivity().getId());
//
//    activityInstance.end();
//
//    assertWaiting(workflowInstance, "b");
//    assertEnded(workflowInstance, "a");
//
//    // here you can store workflowInstance for as long as you want
//    // whenever you receive a signal that activityInstanceA is complete, 
//    // load the workflow instance from the persistence and 
//    // make sure it is properly connected to the workflow (Activity) structure
//
//    ActivityInstance activityInstanceB = workflowInstance.getActivityInstances().get(1);
//    
//    activityInstanceB.end();
//
//    assertWaiting(workflowInstance);
//    assertEnded(workflowInstance, "a", "b");

  }
}
