package ch03;


/**
 * @author Tom Baeyens
 */
public class Ended implements ExecutionState {

  @Override
  public boolean isEnded() {
    return true;
  }
}
