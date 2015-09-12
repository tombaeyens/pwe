/* Copyright (c) 2014, Effektif GmbH.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */
package ch03.concurrency.infrastructure;

import ch03.engine.AsynchronizerImpl;
import ch03.engine.Engine;


/**
 * @author Tom Baeyens
 */
public class TestAsynchronizer extends AsynchronizerImpl {
  
  protected String threadId = "clientThread";
  protected int nextThreadId = 1;

  @Override
  public void continueAsynchrous(Engine engine) {
    engine.getContext().getExternal(Log.class);
    String pausedThreadId = threadId;
    threadId = "AsyncThread"+Integer.toString(nextThreadId++);
    System.out.println("Pausing "+pausedThreadId+", continuing with "+threadId);
    try {
      engine.continueAsynchrous();
    } finally {
      System.out.println("Thread "+threadId+" ended, continuing with "+pausedThreadId);
      threadId = pausedThreadId;
    }
  }

  public String getThreadId() {
    return threadId;
  }
}
