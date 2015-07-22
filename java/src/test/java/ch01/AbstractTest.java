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
package ch01;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author Tom Baeyens
 */
public class AbstractTest {

  public static void assertWaiting(WorkflowInstance workflowInstance, String... expectedActivityIds) {
    // only counts open, activity instances.  the ones that have end==null.
    assertActivityIds(workflowInstance, expectedActivityIds, false);
  }

  public static void assertEnded(WorkflowInstance workflowInstance, String... expectedActivityIds) {
    // only counts completed instances.  the ones that have end!=null.
    assertActivityIds(workflowInstance, expectedActivityIds, true);
  }

  public static void assertStarted(WorkflowInstance workflowInstance, String... expectedActivityIds) {
    // count all (both executing and completed) activity instances
    assertActivityIds(workflowInstance, expectedActivityIds, null);
  }

  private static void assertActivityIds(WorkflowInstance workflowInstance, String[] expectedActivityIds, Boolean completionFilter) {
    List<String> collectedActivityIds = new ArrayList<>();
    collectOpenActivityIds(workflowInstance, collectedActivityIds, completionFilter);
    assertEquals(new ArrayList<String>(Arrays.asList(expectedActivityIds)), collectedActivityIds);
  }

  private static void collectOpenActivityIds(WorkflowInstance workflowInstance, List<String> collectedActivityIds, Boolean completionFilter) {
    for (ActivityInstance nestedActivityInstance: workflowInstance.activityInstances) {
      if (completionFilter==null
          || completionFilter.equals(nestedActivityInstance.end!=null)) {
        collectedActivityIds.add(nestedActivityInstance.activity.id);
      }
    }
  }
}
