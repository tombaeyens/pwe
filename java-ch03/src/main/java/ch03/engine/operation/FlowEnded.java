package ch03.engine.operation;

import ch03.engine.ContextImpl;
import ch03.engine.ControllerImpl;
import ch03.engine.Engine;
import ch03.model.ActivityInstance;
import ch03.model.Scope;
import ch03.model.ScopeInstance;


/**
 * @author Tom Baeyens
 */
public class FlowEnded extends Operation {

  public FlowEnded(ActivityInstance endedActivityInstance) {
    super(endedActivityInstance);
  }

  @Override
  public void perform(Engine engine, ContextImpl context, ControllerImpl controller) {
    ScopeInstance parentInstance = scopeInstance.getParent();
    Scope parentScope = parentInstance.getScope();
    parentScope.activityInstanceEnded((ActivityInstance)scopeInstance, parentInstance, engine.getContext(), engine.getController());
  }
}
