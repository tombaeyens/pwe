package ch03.engine.operation;

import ch03.engine.Execution;
import ch03.engine.ExecutionContextImpl;
import ch03.engine.ExecutionControllerImpl;
import ch03.model.ActivityInstance;
import ch03.model.Scope;
import ch03.model.ScopeInstance;


/**
 * @author Tom Baeyens
 */
public class ScopeInstanceEnded extends Operation {

  public ScopeInstanceEnded(ActivityInstance endedActivityInstance) {
    super(endedActivityInstance);
  }

  @Override
  public void perform(Execution execution, ExecutionContextImpl context, ExecutionControllerImpl controller) {
    ScopeInstance parentInstance = activityInstance.getParent();
    Scope parentScope = parentInstance.scope;
    parentScope.activityInstanceEnded(activityInstance, parentInstance, execution.getExecutionContext(), execution.getExecutionController());
  }
}
