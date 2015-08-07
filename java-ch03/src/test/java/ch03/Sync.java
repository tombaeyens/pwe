package ch03;

import ch03.engine.Execution;
import ch03.model.Activity;


/**
 * @author Tom Baeyens
 */
public class Sync extends Activity {

  @Override
  public void start(Execution execution) {
    execution.onwards();
  }
  
}
