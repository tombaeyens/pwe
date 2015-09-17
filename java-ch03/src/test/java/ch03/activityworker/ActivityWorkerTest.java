package ch03.activityworker;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import ch03.data.TypedValue;
import ch03.engine.EngineFactoryImpl;
import ch03.engine.context.MapContext;
import ch03.model.Workflow;
import ch03.model.WorkflowInstance;


/**
 * @author Tom Baeyens
 */
public class ActivityWorkerTest {
  
  @Test
  public void test() {
    ActivityWorker activityWorker = new ActivityWorker();
    MapContext externalContext = new MapContext("external", new HashMap<String,TypedValue>());
    externalContext.put(activityWorker);
    
    EngineFactoryImpl engineFactory = new EngineFactoryImpl();
    engineFactory.setExternalContext(externalContext);

    Workflow workflow = new Workflow();
    workflow.setEngineFactory(engineFactory);
    workflow.add("external", new ActivityWorkerActivity());

    WorkflowInstance workflowInstance = workflow.start();

    // The .start() will execute up to the ActivityWorkerActivity
    // That's a wait state.  But it will add a notification to 
    // the ActivityWorker.  That notification is performed 
    // after the workflow instance is persisted at the end of the 
    // .start().  The activity worker will respond to the 
    // notification by sending the message to the activity instance,
    // which will complete the whole workflow execution.

    assertTrue(workflowInstance.isEnded());
    
    List<String> logs = activityWorker.getWorkDone();
    assertEquals(logs.toString(), 1, logs.size());
  }
}
