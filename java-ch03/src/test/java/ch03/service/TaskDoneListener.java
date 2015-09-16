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
package ch03.service;

import ch03.model.ActivityInstance;


/**
 * @author Tom Baeyens
 */
public class TaskDoneListener implements TaskListener {

  ActivityInstance activityInstance;
  
  public TaskDoneListener(ActivityInstance activityInstance) {
    this.activityInstance = activityInstance;
  }

  /** Sends a message to the workflow engine that it should  
   * end the activity instance and continue the workflow */
  @Override
  public void taskCompleted(Task task) {
    activityInstance.message();
  }
}
