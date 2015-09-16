package ch03.activityworker;

import java.util.HashMap;
import java.util.List;

import ch03.data.TypedValue;
import ch03.engine.EngineFactoryImpl;
import ch03.engine.context.MapContext;
import ch03.engine.context.SubContext;


public class ActivityWorkerEngineFactory extends EngineFactoryImpl {

  ActivityWorker activityWorker = new ActivityWorker();
  
  @Override
  protected SubContext initializeExternalContext() {
    MapContext externalContext = new MapContext("external", new HashMap<String,TypedValue>());
    externalContext.put(activityWorker);
    return externalContext;
  }

  public List<String> getLogs() {
    return activityWorker.workDone;
  }
}