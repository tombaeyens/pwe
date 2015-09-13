package ch03.concurrency.infrastructure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch03.data.TypedValue;
import ch03.engine.AsynchronizerImpl;
import ch03.engine.EngineFactoryImpl;
import ch03.engine.PersistenceImpl;
import ch03.engine.context.MapContext;
import ch03.engine.context.SubContext;
import ch03.model.WorkflowInstance;


/**
 * @author Tom Baeyens
 */
public class TestEngineFactory extends EngineFactoryImpl {
  
  String threadId = "clientThread";
  int nextThreadId = 1;
  TestAsynchronizer asynchronizer;
  TestConsole testConsole;
  Map<String,WorkflowInstance> workflowInstances = new HashMap<>();
  
  public TestEngineFactory() {
    super();
    this.asynchronizer = new TestAsynchronizer();
    this.testConsole = new TestConsole(asynchronizer);
  }

  @Override
  protected AsynchronizerImpl instantiateAsynchronizer() {
    return asynchronizer;
  }

  @Override
  protected SubContext initializeExternalContext() {
    MapContext externalContext = new MapContext("external", new HashMap<String,TypedValue>());
    externalContext.put(testConsole);
    return externalContext;
  }

  public List<Log> getLogs() {
    return testConsole.getLogs();
  }
  
  @Override
  protected PersistenceImpl instantiatePersistence() {
    return new TestPersistence(this);
  }

  public void addWorkflowInstance(WorkflowInstance workflowInstance) {
    workflowInstances.put(workflowInstance.getId(), workflowInstance);
  }
  
  public WorkflowInstance findWorkflowInstanceById(String workflowInstanceId) {
    return workflowInstances.get(workflowInstanceId);
  }
}