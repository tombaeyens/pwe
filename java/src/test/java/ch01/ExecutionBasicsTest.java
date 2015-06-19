package ch01;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;


public class ExecutionBasicsTest {

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
    assertCompleted(workflowInstance, "a", "b");
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
    workflowInstance.start();

    assertWaiting(workflowInstance);
    assertCompleted(workflowInstance, "a", "b");
  }

  @Test
  public void testSequentialWaitExecution() {
    Workflow workflow = new Workflow()
      .activity(new Wait("a"))
      .activity(new Wait("b"))
      .transition("a", "b");
    
    WorkflowInstance workflowInstance = new WorkflowInstance(workflow);
    workflowInstance.start();

    assertWaiting(workflowInstance, "a");
    assertCompleted(workflowInstance);
    
    // here you can store workflowInstance for as long as you want
    // whenever you receive a signal that activityInstanceA is complete, 
    // load the workflow instance from the persistence and 
    // make sure it is properly connected to the workflow (Activity) structure
    
    ActivityInstance activityInstanceA = workflowInstance.activityInstances.get(0);
    
    activityInstanceA.end();

    assertWaiting(workflowInstance, "b");
    assertCompleted(workflowInstance, "a");

    // here you can store workflowInstance for as long as you want
    // whenever you receive a signal that activityInstanceA is complete, 
    // load the workflow instance from the persistence and 
    // make sure it is properly connected to the workflow (Activity) structure

    ActivityInstance activityInstanceB = workflowInstance.activityInstances.get(1);
    
    activityInstanceB.end();

    assertWaiting(workflowInstance);
    assertCompleted(workflowInstance, "a", "b");

  }

  @Test
  public void testParallelWaitExecution() {
    Workflow workflow = new Workflow()
      .activity(new Wait("a"))
      .activity(new Wait("b"));
    
    WorkflowInstance workflowInstance = new WorkflowInstance(workflow);
    workflowInstance.start();

    assertWaiting(workflowInstance, "a", "b");
    assertCompleted(workflowInstance);

    ActivityInstance activityInstanceA = workflowInstance.activityInstances.get(0);
    activityInstanceA.end();
    ActivityInstance activityInstanceB = workflowInstance.activityInstances.get(1);
    activityInstanceB.end();

    assertWaiting(workflowInstance);
    assertCompleted(workflowInstance, "a", "b");
  }

  public void assertWaiting(WorkflowInstance workflowInstance, String... expectedActivityIds) {
    // only counts open, activity instances.  the ones that have end==null.
    assertActivityIds(workflowInstance, expectedActivityIds, false);
  }

  public void assertCompleted(WorkflowInstance workflowInstance, String... expectedActivityIds) {
    // only counts completed instances.  the ones that have end!=null.
    assertActivityIds(workflowInstance, expectedActivityIds, true);
  }

  public void assertStarted(WorkflowInstance workflowInstance, String... expectedActivityIds) {
    // count all (both executing and completed) activity instances
    assertActivityIds(workflowInstance, expectedActivityIds, null);
  }

  private void assertActivityIds(WorkflowInstance workflowInstance, String[] expectedActivityIds, Boolean completionFilter) {
    List<String> collectedActivityIds = new ArrayList<>();
    collectOpenActivityIds(workflowInstance, collectedActivityIds, completionFilter);
    assertEquals(new ArrayList<String>(Arrays.asList(expectedActivityIds)), collectedActivityIds);
  }

  private void collectOpenActivityIds(WorkflowInstance workflowInstance, List<String> collectedActivityIds, Boolean completionFilter) {
    for (ActivityInstance nestedActivityInstance: workflowInstance.activityInstances) {
      if (completionFilter==null
          || completionFilter.equals(nestedActivityInstance.end!=null)) {
        collectedActivityIds.add(nestedActivityInstance.activity.id);
      }
    }
  }
}
