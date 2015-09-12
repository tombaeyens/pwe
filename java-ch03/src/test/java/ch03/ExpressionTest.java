package ch03;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch03.data.DereferenceExpression;
import ch03.data.InputExpression;
import ch03.data.LessThanExpression;
import ch03.data.NameExpression;
import ch03.data.PlusExpression;
import ch03.data.TypedValueExpression;
import ch03.parser.expression.ExpressionParser;


/**
 * @author Tom Baeyens
 */
public class ExpressionTest {

  static ExpressionParser expressionParser = new ExpressionParser();

  // @Test
  public void testLessThan() {
    LessThanExpression lessThanExpression = (LessThanExpression) parse("$v < $w");
    NameExpression l = (NameExpression) lessThanExpression.getLeft();
    assertEquals("v", l.getName());
    NameExpression r = (NameExpression) lessThanExpression.getRight();
    assertEquals("w", r.getName());
  }

  // @Test
  public void testDereference() {
    DereferenceExpression dereferenceExpression = (DereferenceExpression) parse("$v.f");
    assertEquals("f", dereferenceExpression.getKey());
    NameExpression l = (NameExpression) dereferenceExpression.getExpression();
    assertEquals("v", l.getName());
  }

  @Test
  public void testPlus() {
    PlusExpression plusExpression = (PlusExpression) parse("$v + \"w\" + $x");
    NameExpression x = (NameExpression) plusExpression.getLeft();
    assertEquals("v", x.getName());

    PlusExpression rightPlus = (PlusExpression) plusExpression.getRight();

    TypedValueExpression w = (TypedValueExpression) rightPlus.getLeft();
    assertEquals("w", w.getValue());

    NameExpression v = (NameExpression) rightPlus.getRight();
    assertEquals("x", v.getName());
  }
  
//    Console console = new Console();
//    
//    MapContext externalContext = new MapContext()
//      .put(console);
//    
//    
//    InputExpression lineExpression = expressionParser.parseInputExpression("n + s + john.street");
//    
//    Workflow workflow = new Workflow();
//    PrintLn printExpression = workflow.add("println", new PrintLn()
//      .inputBinding("line", lineExpression));
//    
//    Map<String,Object> jsonMap = new HashMap<>();
//    jsonMap.put("street", "sesame");
//    jsonMap.put("ssn", 875);
//    
//    Map<String, TypedValue> startData = new HashMap<>();
//    startData.put("n", NumberType.typedValue(3l));
//    startData.put("s", StringType.typedValue("hello"));
//    startData.put("john", MapType.typedValue(jsonMap));
//    
//    WorkflowInstance workflowInstance = new Execution()
//      .startWorkflowInstance(workflow, startData);
//    
//    ActivityInstance asyncActivityInstance = workflowInstance
//      .findActivityInstanceByActivityIdRecursive("async");
//    assertNotNull(asyncActivityInstance);
//    
//    new Execution()
//      .externalContext(externalContext)
//      .handleActivityInstanceMessage(asyncActivityInstance);

  protected InputExpression parse(String expressionText) {
    return expressionParser.parseInputExpression(expressionText);
  }

}
