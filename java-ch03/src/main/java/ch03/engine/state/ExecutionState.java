package ch03.engine.state;


/**
 * @author Tom Baeyens
 */
public abstract class ExecutionState {

  public abstract boolean isEnded();

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
