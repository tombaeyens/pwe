package ch03.engine.state;


/**
 * @author Tom Baeyens
 */
public class Created implements ExecutionState {

  @Override
  public boolean isEnded() {
    return false;
  }
}
