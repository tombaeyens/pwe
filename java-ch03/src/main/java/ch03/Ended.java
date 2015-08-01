package ch03;


public class Ended implements ExecutionState {

  @Override
  public boolean isEnded() {
    return true;
  }
}
