package ch03.taskservice;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import ch03.data.TypedValue;
import ch03.engine.EngineFactoryImpl;
import ch03.engine.context.MapContext;
import ch03.model.Workflow;
import ch03.model.WorkflowInstance;


/**
 * @author Tom Baeyens
 */
public class TaskTest {

  @Test
  public void testTask() {
    TaskService taskService = new TaskService();
    MapContext externalContext = new MapContext("external", new HashMap<String,TypedValue>());
    externalContext.put(taskService);
    
    EngineFactoryImpl engineFactory = new EngineFactoryImpl();
    engineFactory.setExternalContext(externalContext);

    Workflow workflow = new Workflow();
    workflow.setEngineFactory(engineFactory);
    workflow.add("task", new TaskActivity()
      .configurationValue("title", "Fix bug 287346"));

    WorkflowInstance workflowInstance = workflow.start();

    assertFalse(workflowInstance.isEnded());

    Task task = taskService.getTasks().get(0);
    assertEquals("Fix bug 287346", task.getTitle());
    task.complete();
    
    assertTrue(workflowInstance.isEnded());
  }
}
