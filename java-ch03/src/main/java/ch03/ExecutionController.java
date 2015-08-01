package ch03;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ch03.data.Condition;
import ch03.data.Expression;
import ch03.data.TypedValue;


public class ExecutionController {
  
  ScopeInstance scopeInstance;
  boolean isAsync = false;
  LinkedList<Operation> operations = null; // null is important. @see perform(Operation)
  LinkedList<Operation> asyncOperations = null;
  ExecutionControllerContext context = new ExecutionControllerContext(this);
  Asynchronizer asynchronizer = null;
  Context externalContext; 
  
  public ExecutionController() {
  }

  public ExecutionController(Context externalContext) {
    this.externalContext = externalContext;
  }

  public void startScope() {
    // if there are nested activities
    if (!scopeInstance.scope.activities.isEmpty()) {
      // for each nested activity 
      for (Scope scope: scopeInstance.scope.activities) {
        // if the nested activity doesn't have any incoming transitions
        if (scope.inTransitions.isEmpty()) {
          // start it
          start(scope);
        }
      }
    } else {
      onwards();
    }
  }
  
  /** ends the activity instance and propagates the execution flow to the parent */
  public void end() {
    endScopeInstance();
    if (scopeInstance.parentScopeInstance!=null) {
      scopeInstance = scopeInstance.parentScopeInstance;
    }
  }

  /** takes the outgoing applicable transitions if there are any 
   * or propagates the execution flow to the parent otherwise */
  public void onwards() {
    List<Transition> transitionsToTake = new ArrayList<>();
    for (Transition outTransition: scopeInstance.scope.outTransitions) {
      if (isConditionMet(outTransition.condition, scopeInstance)) {
        transitionsToTake.add(outTransition);
      }
    }
    if (!transitionsToTake.isEmpty()) {
      takeTransitions(transitionsToTake);
    } else {
      end();
    }
  }

  protected boolean isConditionMet(Condition condition, ScopeInstance scopeInstance) {
    if (condition==null) {
      return true;
    }
    return condition.evaluate(context);
  }

  /** takes the given transitions if there are any 
   * or propagates the execution flow to the parent otherwise */
  public int takeTransitions(List<Transition> transitions) {
    endScopeInstance();
    int activitiesStarted = 0;
    if (transitions!=null) {
      for (Transition transition : transitions) {
        if (takeTransition(transition)) {
          activitiesStarted++;
        }
      }
    }
    return activitiesStarted;
  }

  public boolean takeTransition(Transition transition) {
    endScopeInstance();
    if (transition==null
        || transition.to==null
        || !isConditionMet(transition.condition, scopeInstance)) {
      return false;
    }
    return start(transition.to, scopeInstance.parentScopeInstance);
  }

  /** starts the given scope */
  public boolean start(Scope scope) {
    return start(scope, this.scopeInstance);
  }

  /** starts the given scope */
  public boolean start(Scope scope, ScopeInstance parentScopeInstance) {
    if (scope==null || !isConditionMet(scope.condition, parentScopeInstance)) {
      return false;
    }
    ScopeInstance nestedScopeInstance = scope.createInstance();
    nestedScopeInstance.initialize(scope, parentScopeInstance);
    parentScopeInstance.scopeInstances.add(nestedScopeInstance);
    perform(new StartScope(nestedScopeInstance));
    return true;
  }
  
  protected void endScopeInstance() {
    if (!scopeInstance.isEnded()) {
      scopeInstance.end();
    }
  }
  
  protected void perform(Operation operation) {
    if (operations==null) {
      operations = new LinkedList<>();
      addOperation(operation);
      executeOperations();
      if (!asyncOperations.isEmpty()) {
        operations = asyncOperations;
        asyncOperations = null;
        asynchronizer.continueAsynchrous(this);
      }
    } else {
      addOperation(operation);
    }
  }

  protected void addOperation(Operation operation) {
    if (isAsync || operation.isSynchonrous()) {
      operations.add(operation);
    } else {
      asyncOperations.add(operation);
    }
  }

  public void continueAsynchrous() {
    isAsync = true;
    executeOperations();
  }

  protected void executeOperations() {
    while (!operations.isEmpty()) {
      Operation current = operations.removeFirst();
      this.scopeInstance = current.getScopeInstance();
      current.perform(this);
    }
  }

  public TypedValue get(String key) {
    Expression expression = scopeInstance.scope.inputBindings.get(key);
    if (expression!=null) {
      return get(expression);
    }
    return context.get(key);
  }

  public TypedValue get(Expression expression) {
    return expression.get(context);
  }
}
