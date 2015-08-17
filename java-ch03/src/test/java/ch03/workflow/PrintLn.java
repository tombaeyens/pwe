package ch03.workflow;

import ch03.data.InputExpression;
import ch03.data.OutputExpression;
import ch03.engine.Execution;
import ch03.model.Activity;


/**
 * @author Tom Baeyens
 */
public class PrintLn extends Activity {

  @Override
  public void start(Execution execution) {
    execution.getAllContexts().get("external");
    super.start(execution);
  }

  @Override
  public PrintLn inputBinding(String key, InputExpression inputExpression) {
    super.inputBinding(key, inputExpression);
    return this;
  }

  @Override
  public PrintLn outputBinding(String key, OutputExpression outputExpression) {
    super.outputBinding(key, outputExpression);
    return this;
  }
}
