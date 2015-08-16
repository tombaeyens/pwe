package ch03.engine.state;



/**
 * @author Tom Baeyens
 */
public class Ended implements ExecutionState {

  @Override
  public boolean isEnded() {
    return true;
  }
}
