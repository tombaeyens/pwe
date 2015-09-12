package ch03.engine;

import ch03.engine.context.SubContext;


/**
 * @author Tom Baeyens
 */
public class EngineFactory {
  
  protected SubContext externalContext;

  public Engine newEngine() {
    Engine engine = new Engine();
    ControllerImpl controller = new ControllerImpl();
    ContextImpl context = new ContextImpl();
    EngineListenerImpl engineListener = new EngineListenerImpl();
    AsynchronizerImpl asynchronizer = new AsynchronizerImpl();

    engine.setController(controller);
    engine.setContext(context);
    engine.setEngineListener(engineListener);
    engine.setAsynchronizer(asynchronizer);
    
    controller.setEngine(engine);
    controller.setEngineListener(engineListener);
    controller.setContext(context);
    
    context.setEngine(engine);
    context.setExternalContext(externalContext);
    
    return engine;
  }
  
  public SubContext getExternalContext() {
    return externalContext;
  }
  
  public void setExternalContext(SubContext externalContext) {
    this.externalContext = externalContext;
  }
}
