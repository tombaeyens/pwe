package ch03.engine;

import ch03.engine.context.SubContext;


/**
 * @author Tom Baeyens
 */
public class EngineFactoryImpl implements EngineFactory {

  @Override
  public EngineImpl createEngine() {
    EngineImpl engine = instantiateEngine();
    ControllerImpl controller = instantiateController();
    ContextImpl context = instantiateContext();
    PersistenceImpl persistence = instantiatePersistence();
    AsynchronizerImpl asynchronizer = instantiateAsynchronizer();

    engine.setController(controller);
    engine.setContext(context);
    engine.setPersistence(persistence);
    engine.setAsynchronizer(asynchronizer);
    
    controller.setEngine(engine);
    controller.setPersistence(persistence);
    controller.setContext(context);
    
    context.setEngine(engine);
    context.setExternalContext(initializeExternalContext());
    
    return engine;
  }

  protected EngineImpl instantiateEngine() {
    return new EngineImpl();
  }

  protected SubContext initializeExternalContext() {
    return null;
  }

  protected ControllerImpl instantiateController() {
    return new ControllerImpl();
  }

  protected ContextImpl instantiateContext() {
    return new ContextImpl();
  }

  protected PersistenceImpl instantiatePersistence() {
    return new PersistenceImpl();
  }

  protected AsynchronizerImpl instantiateAsynchronizer() {
    return new AsynchronizerImpl();
  }
}
