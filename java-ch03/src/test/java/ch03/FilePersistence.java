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
package ch03;

import java.util.List;

import ch03.data.TypedValue;
import ch03.engine.WorkflowInstancePersistence;
import ch03.engine.operation.Operation;
import ch03.engine.state.ExecutionState;
import ch03.model.ActivityInstance;
import ch03.model.VariableInstance;
import ch03.model.WorkflowInstance;


/**
 * @author Tom Baeyens
 */
public class FilePersistence implements WorkflowInstancePersistence {

  @Override
  public String generateWorkflowInstanceId(WorkflowInstance workflowInstance) {
    return null;
  }

  @Override
  public String generateVariableInstanceId(VariableInstance variableInstance) {
    return null;
  }

  @Override
  public String generateActivityInstanceId(ActivityInstance activityInstance) {
    return null;
  }

  @Override
  public void workflowInstanceCreated(WorkflowInstance workflowInstance) {
  }

  @Override
  public void variableInstanceCreated(VariableInstance variableInstance) {
  }

  @Override
  public void variableInstanceValueUpdated(VariableInstance variableInstance, TypedValue oldValue) {
  }

  @Override
  public void activityInstanceCreated(ActivityInstance activityInstance) {
  }

  @Override
  public void activityInstanceEnded(ActivityInstance activityInstance) {
  }

  @Override
  public void workflowInstanceEnded(WorkflowInstance workflowInstance) {
  }

  @Override
  public void activityInstanceStateUpdate(ActivityInstance activityInstance, ExecutionState oldState) {
  }

  @Override
  public void workflowInstanceStateUpdate(WorkflowInstance workflowInstance, ExecutionState oldState) {
  }

  @Override
  public void operationSynchronousAdded(Operation operation) {
  }

  @Override
  public void operationSynchronousRemoved(Operation current) {
  }

  @Override
  public void operationAsynchronousAdded(Operation operation) {
  }

  @Override
  public void savePoint(WorkflowInstance workflowInstance, List<Operation> operations, List<Operation> asyncOperations) {
  }

  @Override
  public void flush(WorkflowInstance workflowInstance) {
  }
}
