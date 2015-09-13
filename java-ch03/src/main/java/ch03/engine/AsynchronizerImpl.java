package ch03.engine;

import ch03.concurrency.infrastructure.Log;
import ch03.util.Logger;


/** default asynchronizer implementation performs 
 * the asynchronous work synchronous.
 * 
 * @author Tom Baeyens
 */
public class AsynchronizerImpl implements Asynchronizer {

  private static final Logger log = Engine.log;
  
  protected String threadId = "clientThread";
  protected int nextThreadId = 1;

  @Override
  public void continueAsynchrous(Engine engine) {
    engine.getContext().getExternal(Log.class);
    String pausedThreadId = threadId;
    threadId = "AsyncThread"+Integer.toString(nextThreadId++);
    log.debug("~~~ Pretend switching to thread  "+threadId);
    try {
      engine.continueAsynchrous();
    } finally {
      log.debug("~~~ Pretend switching back to thread "+pausedThreadId);
      threadId = pausedThreadId;
    }
  }

  public String getThreadId() {
    return threadId;
  }
}
