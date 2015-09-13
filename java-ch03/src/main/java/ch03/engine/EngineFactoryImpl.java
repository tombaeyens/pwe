/* Copyright (c) 2014, Effektif GmbH.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */
package ch03.engine;

import ch03.engine.context.SubContext;


/**
 * @author Tom Baeyens
 */
public class EngineFactoryImpl implements EngineFactory {

  @Override
  public Engine createEngine() {
    Engine engine = instantiateEngine();
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

  protected Engine instantiateEngine() {
    return new Engine();
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
