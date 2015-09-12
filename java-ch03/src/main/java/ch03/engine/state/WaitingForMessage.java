package ch03.engine.state;



/**
 * @author Tom Baeyens
 */
public class WaitingForMessage extends ExecutionState {

  @Override
  public boolean isEnded() {
    return false;
  }

}
