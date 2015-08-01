package ch03;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Workflow extends Scope {
  
  Trigger trigger;
  List<WorkflowListener> listeners = new ArrayList<>();

  public WorkflowInstance start() {
    return start(null);
  }

  public WorkflowInstance start(Map<String,Object> initialData) {
    Context externalContext = initialData!=null ? new MapContext(initialData) : null;
    return start(initialData, externalContext);
  }
  
  public WorkflowInstance start(Map<String,Object> initialData, Context externalContext) {
    WorkflowInstance workflowInstance = new WorkflowInstance(this);
    ExecutionController executionController = new ExecutionController(workflowInstance, externalContext, listeners);
    executionController.startTrigger(trigger, initialData);
    return workflowInstance;
  }
}