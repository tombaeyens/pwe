package ch03.engine;

import java.util.List;

import ch03.data.Converter;
import ch03.engine.context.ConfigurationsContext;
import ch03.engine.context.SubContext;
import ch03.engine.context.VariablesContext;


/**
 * @author Tom Baeyens
 */
public class EngineFactoryImpl implements EngineFactory {
  
  protected SubContext externalContext = null;
  protected List<Converter> converters = null;

  @Override
  public EngineImpl createEngine() {
    EngineImpl engine = instantiateEngine();
    ControllerImpl controller = instantiateController();
    ContextImpl context = instantiateContext();
    PersistenceImpl persistence = instantiatePersistence();

    engine.setController(controller);
    engine.setContext(context);
    engine.setPersistence(persistence);
    
    controller.setEngine(engine);
    controller.setPersistence(persistence);
    controller.setContext(context);
    
    context.setEngine(engine);
    context.setExternalContext(externalContext);
    context.addSubContext(new VariablesContext(engine));
    context.addSubContext(new ConfigurationsContext(engine));
    context.setConverters(converters);

    return engine;
  }

  protected EngineImpl instantiateEngine() {
    return new EngineImpl();
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

  
  public SubContext getExternalContext() {
    return externalContext;
  }

  
  public void setExternalContext(SubContext externalContext) {
    this.externalContext = externalContext;
  }

  
  public List<Converter> getConverters() {
    return converters;
  }

  
  public void setConverters(List<Converter> converters) {
    this.converters = converters;
  }
}
