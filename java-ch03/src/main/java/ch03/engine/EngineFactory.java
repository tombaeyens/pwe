package ch03.engine;


/**
 * @author Tom Baeyens
 */
public class EngineFactory {
  
  protected Controller controller;

  public Engine newEngine() {
    return new Engine();
  }

}
