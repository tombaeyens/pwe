package ch03.timers;

import java.util.LinkedHashMap;
import java.util.Map;

import ch03.data.InputExpression;
import ch03.engine.Context;
import ch03.engine.Listener;
import ch03.model.ScopeInstance;


/**
 * @author Tom Baeyens
 */
public class CreateTimer implements Listener {
  
  protected String name;
  protected Map<String,InputExpression> inputParameters;

  @Override
  public void execute(ScopeInstance scopeInstance, Context context) {
    TimerService timerService = context.getExternal(TimerService.class);

    TimerHandler timerHandler = instantiateTimerHandler();
    timerHandler.setInputs(context.readInputs(inputParameters));

    Timer timer = new Timer();
    timer.setName(name);
    timer.setTimerHandler(timerHandler);
    timerService.createTimer(timer);
  }

  protected TimerHandler instantiateTimerHandler() {
    return new TimerHandler();
  }

  public String getName() {
    return this.name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public CreateTimer name(String name) {
    this.name = name;
    return this;
  }

  public Map<String, InputExpression> getInputParameters() {
    return inputParameters;
  }

  public void setInputParameters(Map<String, InputExpression> inputParameters) {
    this.inputParameters = inputParameters;
  }

  public CreateTimer inputParameter(String key, InputExpression inputExpression) {
    assert inputExpression != null;
    if (inputParameters==null) {
      inputParameters = new LinkedHashMap<>();
    }
    inputParameters.put(key, inputExpression);
    return this;
  }
}
