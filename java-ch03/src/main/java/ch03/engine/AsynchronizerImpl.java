package ch03.engine;

import ch03.concurrency.infrastructure.Log;
import ch03.util.Logger;


/** default asynchronizer implementation performs 
 * the asynchronous work synchronous.
 * 
 * @author Tom Baeyens
 */
public class AsynchronizerImpl implements Asynchronizer {

  private static final Logger log = EngineImpl.log;

  public static final String CLIENT = "client";
  public static final String ASYNC = "async";

  protected String threadName = CLIENT;
  protected int nextThreadId = 1;

  @Override
  public void continueAsynchrous(EngineImpl engine) {
    engine.getContext().getExternal(Log.class);
    threadName = ASYNC;
    log.debug("~~~ Switching to async work in current thread");
    try {
      engine.continueAsynchrous();
    } finally {
      log.debug("~~~ Switching back to client thread");
      threadName = CLIENT;
    }
  }

  public String getThreadName() {
    return threadName;
  }
}
