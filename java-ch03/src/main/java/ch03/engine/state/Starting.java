package ch03.engine.state;


/**
 * @author Tom Baeyens
 */
public class Starting extends ExecutionState {

  public static final Starting INSTANCE = new Starting();

  @Override
  public boolean isEnded() {
    return false;
  }

}
