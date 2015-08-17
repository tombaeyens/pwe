package ch03.engine.state;



/**
 * @author Tom Baeyens
 */
public class WaitingForMessage implements ExecutionState {

  @Override
  public boolean isEnded() {
    return false;
  }

}
