package ch03;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import ch03.data.InputExpression;
import ch03.data.TypedValue;
import ch03.data.types.MapType;
import ch03.data.types.NumberType;
import ch03.data.types.StringType;
import ch03.engine.Execution;
import ch03.engine.context.MapContext;
import ch03.model.ActivityInstance;
import ch03.model.Workflow;
import ch03.model.WorkflowInstance;
import ch03.workflow.Console;
import ch03.workflow.PrintLn;


/**
 * @author Tom Baeyens
 */
public class ExpressionTest {

  @Test
  public void test() {
    Console console = new Console();
    
    MapContext externalContext = new MapContext()
      .put(console);
    
    ExpressionParser expressionParser = new ExpressionParser();
    InputExpression lineExpression = expressionParser.parseInputExpression("n + s + john.street");
    
    Workflow workflow = new Workflow();
    PrintLn printExpression = workflow.add("println", new PrintLn()
      .inputBinding("line", lineExpression));
    
    Map<String,Object> jsonMap = new HashMap<>();
    jsonMap.put("street", "sesame");
    jsonMap.put("ssn", 875);
    
    Map<String, TypedValue> startData = new HashMap<>();
    startData.put("n", NumberType.typedValue(3l));
    startData.put("s", StringType.typedValue("hello"));
    startData.put("john", MapType.typedValue(jsonMap));
    
    WorkflowInstance workflowInstance = new Execution()
      .startWorkflowInstance(workflow, startData);
    
    ActivityInstance asyncActivityInstance = workflowInstance
      .findActivityInstanceByActivityIdRecursive("async");
    assertNotNull(asyncActivityInstance);
    
    new Execution()
      .externalContext(externalContext)
      .handleActivityInstanceMessage(asyncActivityInstance);
  }

}
