package ch03.concurrency.infrastructure;

import java.util.HashMap;
import java.util.List;

import ch03.data.TypedValue;
import ch03.engine.AsynchronizerImpl;
import ch03.engine.EngineFactoryImpl;
import ch03.engine.context.MapContext;
import ch03.engine.context.SubContext;


/**
 * @author Tom Baeyens
 */
public class TestEngineFactory extends EngineFactoryImpl {
  
  String threadId = "clientThread";
  int nextThreadId = 1;
  TestAsynchronizer asynchronizer;
  TestConsole testConsole;
  
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
}