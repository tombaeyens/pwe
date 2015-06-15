package ch01;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ch01.Activity;
import ch01.ActivityInstance;


public class ExecutionBasicsTest {

  @Test
  public void testSequentialAutomaticExecution() {
    Activity workflow = new Activity("workflow")
      .activity(new Automatic("a"))
      .activity(new Automatic("b"))
      .transition("a", "b");
    
    ActivityInstance workflowInstance = workflow.start();
    
    assertExecuting(workflowInstance);
    assertCompleted(workflowInstance, "a", "b");
  }

  @Test
  public void testParallelAutomaticExecution() {
    Activity workflow = new Activity("workflow")
      .activity(new Automatic("a"))
      .activity(new Automatic("b"));
    
    ActivityInstance workflowInstance = workflow.start();

    assertExecuting(workflowInstance);
    assertCompleted(workflowInstance, "a", "b");
  }

  @Test
  public void testSequentialWaitExecution() {
    Activity workflow = new Activity("workflow")
      .activity(new Wait("a"))
      .activity(new Wait("b"))
      .transition("a", "b");
    
    ActivityInstance workflowInstance = workflow.start();

    assertExecuting(workflowInstance, "a");
    assertCompleted(workflowInstance);
    
    // here you can store workflowInstance for as long as you want
    // whenever you receive a signal that activityInstanceA is complete, 
    // load the workflow instance from the persistence and 
    // make sure it is properly connected to the workflow (Activity) structure
    
    ActivityInstance activityInstanceA = workflowInstance.activityInstances.get(0);
    
    activityInstanceA.end();

    assertExecuting(workflowInstance, "b");
    assertCompleted(workflowInstance, "a");

    // here you can store workflowInstance for as long as you want
    // whenever you receive a signal that activityInstanceA is complete, 
    // load the workflow instance from the persistence and 
    // make sure it is properly connected to the workflow (Activity) structure

    ActivityInstance activityInstanceB = workflowInstance.activityInstances.get(1);
    
    activityInstanceB.end();

    assertExecuting(workflowInstance);
    assertCompleted(workflowInstance, "a", "b");

  }

  @Test
  public void testParallelWaitExecution() {
    Activity workflow = new Activity("workflow")
      .activity(new Wait("a"))
      .activity(new Wait("b"));
    
    ActivityInstance workflowInstance = workflow.start();

    assertExecuting(workflowInstance, "a", "b");
    assertCompleted(workflowInstance);

    ActivityInstance activityInstanceA = workflowInstance.activityInstances.get(0);
    activityInstanceA.end();
    ActivityInstance activityInstanceB = workflowInstance.activityInstances.get(1);
    activityInstanceB.end();

    assertExecuting(workflowInstance);
    assertCompleted(workflowInstance, "a", "b");
  }

  public void assertExecuting(ActivityInstance activityInstance, String... expectedActivityIds) {
    // only counts open, activity instances.  the ones that have end==null.
    assertActivityIds(activityInstance, expectedActivityIds, false);
  }

  public void assertCompleted(ActivityInstance activityInstance, String... expectedActivityIds) {
    // only counts completed instances.  the ones that have end!=null.
    assertActivityIds(activityInstance, expectedActivityIds, true);
  }

  public void assertStarted(ActivityInstance activityInstance, String... expectedActivityIds) {
    // count all (both executing and completed) activity instances
    assertActivityIds(activityInstance, expectedActivityIds, null);
  }

  private void assertActivityIds(ActivityInstance activityInstance, String[] expectedActivityIds, Boolean completionFilter) {
    List<String> collectedActivityIds = new ArrayList<>();
    collectOpenActivityIds(activityInstance, collectedActivityIds, completionFilter);
    assertEquals(new ArrayList<String>(Arrays.asList(expectedActivityIds)), collectedActivityIds);
  }

  private void collectOpenActivityIds(ActivityInstance activityInstance, List<String> collectedActivityIds, Boolean completionFilter) {
    for (ActivityInstance nestedActivityInstance: activityInstance.activityInstances) {
      if (completionFilter==null
          || completionFilter.equals(nestedActivityInstance.end!=null)) {
        collectedActivityIds.add(nestedActivityInstance.activity.id);
      }
      collectOpenActivityIds(nestedActivityInstance, collectedActivityIds, completionFilter);
    }
  }
}
