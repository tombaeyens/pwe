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
package ch03.engine;

import java.util.List;
import java.util.Map;

import ch03.data.TypedValue;
import ch03.model.Activity;
import ch03.model.ScopeInstance;
import ch03.model.Trigger;
import ch03.model.Workflow;
import ch03.model.WorkflowInstance;


/**
 * @author Tom Baeyens
 */
public class StartWorkflowInstance extends Operation {

  Map<String, TypedValue> startData;
  List<Activity> startActivities;
  WorkflowInstance workflowInstance;
  
  public StartWorkflowInstance(WorkflowInstance workflowInstance, Map<String, TypedValue> startData, List<Activity> startActivities) {
    super(null);
    this.workflowInstance = workflowInstance;
    this.startData = startData;
    this.startActivities = startActivities;
  }
  
  @Override
  public ScopeInstance getScopeInstance() {
    return workflowInstance;
  }

  @Override
  public void perform(Execution execution) {
    Workflow workflow = workflowInstance.workflow;
    Trigger trigger = workflow.trigger;
    if (trigger!=null) {
      trigger.start(execution, startData);
    } else if (startData!=null) {
      for (String key: startData.keySet()) {
        TypedValue typedValue = startData.get(key);
        execution.setVariableInstanceValue(key, typedValue);
      }
    }
    if (startActivities==null) {
      startActivities = workflow.getStartActivities();
    }
    execution.startActivities(startActivities);
  }

}
