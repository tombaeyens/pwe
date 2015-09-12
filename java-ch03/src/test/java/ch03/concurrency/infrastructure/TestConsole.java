package ch03.concurrency.infrastructure;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Tom Baeyens
 */
public class TestConsole {

  TestAsynchronizer asynchronizer;
  List<Log> logs = new ArrayList<>();
  
  public TestConsole(TestAsynchronizer asynchronizer) {
    this.asynchronizer = asynchronizer;
  }

  public void log(String message) {
    logs.add(new Log(asynchronizer.getThreadId(), message));
  }

  public List<Log> getLogs() {
    return logs;
  }
}
