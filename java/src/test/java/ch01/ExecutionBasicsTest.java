package ch01;
import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class ExecutionBasicsTest extends AbstractTest {

  @Test
  public void testSequentialAutomaticExecution() {
    // +-------------+   +-------------+
    // |     a       |-->|      b      |
    // | (Automatic) |   | (Automatic) |
    // +-------------+   +-------------+
    Workflow workflow = new Workflow()
      .activity(new Automatic("a"))
      .activity(new Automatic("b"))
      .transition("a", "b");
    
    WorkflowInstance workflowInstance = new WorkflowInstance(workflow);

    // When we invoke start(), only activity 'a' will be started 
    // because 'b' has an incoming transition and is therefor not 
    // considered a start activity.
    // Activity 'a' is of type Automatic, which means it will 
    // log a message and propagate the execution immediately by 
    // invoking ActivityInstance.end.  Ending activity 'a' means 
    // all outgoing transitions are taken.  Therefor activity 'b' is 
    // executed.  Activity 'b' then ends, but it has no outgoing
    // transitions so the execution flow stops there.
    // Only then the start() method returns.
    workflowInstance.start();
    
    // We now check that there are no activity instances open.
    // An activity instance is open when it is ended (ActivityInstance.end!=null)
    assertWaiting(workflowInstance);
    
    // We also check that there are 2 ended activity instances, one for 
    // activity 'a' and one for activity 'b'
    assertEnded(workflowInstance, "a", "b");
  }

  @Test
  public void testParallelAutomaticExecution() {
    // +-------------+   +-------------+
    // |     a       |   |      b      |
    // | (Automatic) |   | (Automatic) |
    // +-------------+   +-------------+
    Workflow workflow = new Workflow()
      .activity(new Automatic("a"))
      .activity(new Automatic("b"));
    
    WorkflowInstance workflowInstance = new WorkflowInstance(workflow);

    // When we invoke start(), both activities 'a' and 'b' are 
    // start activities and will be started.  Both activity 
    // instances will immediately log a message to the console 
    // and end.
    workflowInstance.start();

    // We now check that there are no activity instances open.
    // An activity instance is open when it is ended (ActivityInstance.end!=null)
    assertWaiting(workflowInstance);

    // We also check that there are 2 ended activity instances, one for 
    // activity 'a' and one for activity 'b'
    assertEnded(workflowInstance, "a", "b");
  }

  @Test
  public void testSequentialWaitExecution() {
    // +-------------+   +-------------+
    // |     a       |-->|     b       |
    // |   (Wait)    |   |   (Wait)    |
    // +-------------+   +-------------+
    Workflow workflow = new Workflow()
      .activity(new Wait("a"))
      .activity(new Wait("b"))
      .transition("a", "b");
    
    WorkflowInstance workflowInstance = new WorkflowInstance(workflow);

    // When we invoke start(), only activity 'a' will be started 
    // because 'b' has an incoming transition and is therefor not 
    // considered a start activity.
    // Activity 'a' is of type Wait, which means it will 
    // log a message and then return without ending the activity instance.  
    // This means that the start() method will return with an 
    // activity instance that is not ended (ActivityInstance.end==null)
    // No activivity instance for 'b' will be created yet.
    workflowInstance.start();

    // We check that there is an open activity instance waiting in 'a'
    assertWaiting(workflowInstance, "a");
    // We check that there are no ended activity instances
    assertEnded(workflowInstance);

    // We now take the single activity instance in the workflowInstance
    ActivityInstance activityInstanceA = workflowInstance.activityInstances.get(0);
    // just to be safe, we double check it is the one positioned in 'a'
    assertEquals("a", activityInstanceA.activity.id);

    // When we call end(), we provide an external trigger that will end the activity instance 
    // and propagate the workflow execution forward until no more work can be done.
    // In this case, propagation means that the single transition from 'a' to 
    // 'b' is taken and execution will arrive in 'a'.  'a' is started, it 
    // will print a message to the console and then return without propagating 
    // the execution forward.
    activityInstanceA.end();

    assertWaiting(workflowInstance, "b");
    assertEnded(workflowInstance, "a");

    // here you can store workflowInstance for as long as you want
    // whenever you receive a signal that activityInstanceA is complete, 
    // load the workflow instance from the persistence and 
    // make sure it is properly connected to the workflow (Activity) structure

    ActivityInstance activityInstanceB = workflowInstance.activityInstances.get(1);
    
    activityInstanceB.end();

    assertWaiting(workflowInstance);
    assertEnded(workflowInstance, "a", "b");

  }

  @Test
  public void testParallelWaitExecution() {
    // +-------------+   +-------------+
    // |     a       |   |      b      |
    // |   (Wait)    |   |   (Wait)    |
    // +-------------+   +-------------+
    Workflow workflow = new Workflow()
      .activity(new Wait("a"))
      .activity(new Wait("b"));
    
    WorkflowInstance workflowInstance = new WorkflowInstance(workflow);
    workflowInstance.start();

    assertWaiting(workflowInstance, "a", "b");
    assertEnded(workflowInstance);

    ActivityInstance activityInstanceA = workflowInstance.activityInstances.get(0);
    activityInstanceA.end();
    ActivityInstance activityInstanceB = workflowInstance.activityInstances.get(1);
    activityInstanceB.end();

    assertWaiting(workflowInstance);
    assertEnded(workflowInstance, "a", "b");
  }

}
