package ch03;

import org.junit.Test;


/**
 * @author Tom Baeyens
 */
public class WorkflowTest {

  @Test
  public void test() {
    Workflow workflow = new Workflow();
    WorkflowInstance workflowInstance = workflow.start();
  }
}
