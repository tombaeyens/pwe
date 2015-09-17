package ch03.data;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import ch03.data.types.NumberType;
import ch03.data.types.StringType;
import ch03.model.Variable;
import ch03.model.VariableInstance;
import ch03.model.Workflow;
import ch03.model.WorkflowInstance;


/**
 * @author Tom Baeyens
 */
public class DataStartWorkflowTest {

  @Test 
  public void testPassingDeclaredValuesWithStart() {
    Workflow workflow = new Workflow()
      .variable(new Variable()
        .id("v1")
        .type(StringType.INSTANCE))
      .variable(new Variable()
        .id("v2")
        .type(NumberType.INSTANCE));

    Map<String,TypedValue> startData = new HashMap<>();
    startData.put("v1", new TypedValue(StringType.INSTANCE, "one"));
    startData.put("v2", new TypedValue(NumberType.INSTANCE, 6));
    WorkflowInstance workflowInstance = workflow.start(startData);

    Map<String, VariableInstance> variableInstances = workflowInstance.getVariableInstances();
    assertEquals("one", variableInstances.get("v1").getTypedValue().getValue());
    assertEquals(6, variableInstances.get("v2").getTypedValue().getValue());
  }


  @Test 
  public void testPassingUndeclaredValuesWithStart() {
    Workflow workflow = new Workflow();

    Map<String,TypedValue> startData = new HashMap<>();
    startData.put("v1", new TypedValue(StringType.INSTANCE, "one"));
    startData.put("v2", new TypedValue(NumberType.INSTANCE, 6));
    WorkflowInstance workflowInstance = workflow.start(startData);

    Map<String, VariableInstance> variableInstances = workflowInstance.getVariableInstances();
    assertEquals("one", variableInstances.get("v1").getTypedValue().getValue());
    assertEquals(6, variableInstances.get("v2").getTypedValue().getValue());
  }

  @Test 
  public void testWorkflowInputParameters() {
    Workflow workflow = new Workflow()
      .variable(new Variable()
        .id("v1")
        .type(StringType.INSTANCE))
      .variable(new Variable()
        .id("v2")
        .type(NumberType.INSTANCE))
      .inputParameter("v1", new NameExpression("p1"))
      .inputParameter("v2", new NameExpression("p2"));

    Map<String,TypedValue> startData = new HashMap<>();
    startData.put("p1", new TypedValue(StringType.INSTANCE, "one"));
    startData.put("p2", new TypedValue(NumberType.INSTANCE, 6));
    WorkflowInstance workflowInstance = workflow.start(startData);

    Map<String, VariableInstance> variableInstances = workflowInstance.getVariableInstances();
    assertEquals("one", variableInstances.get("v1").getTypedValue().getValue());
    assertEquals(6, variableInstances.get("v2").getTypedValue().getValue());
  }
}
