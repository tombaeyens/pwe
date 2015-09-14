package ch03.concurrency.infrastructure;

import java.util.ArrayList;
import java.util.List;

import ch03.engine.AsynchronizerImpl;


/**
 * @author Tom Baeyens
 */
public class TestConsole {

  AsynchronizerImpl asynchronizer;
  List<Log> logs = new ArrayList<>();
  
  public TestConsole(AsynchronizerImpl asynchronizer) {
    this.asynchronizer = asynchronizer;
  }

  public void log(String message) {
    logs.add(new Log(asynchronizer.getThreadName(), message));
  }

  public List<Log> getLogs() {
    return logs;
  }
}
