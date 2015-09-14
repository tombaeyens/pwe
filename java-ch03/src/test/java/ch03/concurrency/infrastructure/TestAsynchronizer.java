package ch03.concurrency.infrastructure;

import ch03.engine.AsynchronizerImpl;
import ch03.engine.EngineImpl;
import ch03.util.Logger;


/**
 * @author Tom Baeyens
 */
public class TestAsynchronizer extends AsynchronizerImpl {
  
  private static final Logger log = EngineImpl.log;
  
  protected String threadId = "clientThread";
  protected int nextThreadId = 1;

  @Override
  public void continueAsynchrous(EngineImpl engine) {
    engine.getContext().getExternal(Log.class);
    String pausedThreadId = threadId;
    threadId = "AsyncThread"+Integer.toString(nextThreadId++);
    log.debug("~~~ Switching to thread  "+threadId);
    try {
      engine.continueAsynchrous();
    } finally {
      log.debug("~~~ Switching back to thread "+pausedThreadId);
      threadId = pausedThreadId;
    }
  }

  public String getThreadId() {
    return threadId;
  }
}
