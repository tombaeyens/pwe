package ch03.concurrency.infrastructure;

import ch03.engine.PersistenceImpl;
import ch03.model.WorkflowInstance;


/**
 * @author Tom Baeyens
 */
public class TestPersistence extends PersistenceImpl {

  TestEngineFactory testEngineFactory;
  
  public TestPersistence(TestEngineFactory testEngineFactory) {
    this.testEngineFactory = testEngineFactory;
  }

  @Override
  public void transactionStartWorkflowInstance(WorkflowInstance workflowInstance) {
    super.transactionStartWorkflowInstance(workflowInstance);
    testEngineFactory.addWorkflowInstance(workflowInstance);
  }
}
