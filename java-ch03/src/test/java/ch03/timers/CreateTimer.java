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
  protected boolean cancelOnScopeEnd;
  protected Map<String,InputExpression> inputParameters;

  @Override
  public void execute(ScopeInstance scopeInstance, Context context) {
    TimerService timerService = context.getExternal(TimerService.class);

    Timer timer = new Timer();
    timer.setName(name);
    timer.setCancelOnScopeEnd(cancelOnScopeEnd);
    timer.setInputs(context.readInputs(inputParameters));
    timerService.createTimer(timer);
  }

  public boolean getCancelOnScopeEnd() {
    return this.cancelOnScopeEnd;
  }
  public void setCancelOnScopeEnd(boolean cancelOnScopeEnd) {
    this.cancelOnScopeEnd = cancelOnScopeEnd;
  }
  public CreateTimer cancelOnScopeEnd(boolean cancelOnScopeEnd) {
    this.cancelOnScopeEnd = cancelOnScopeEnd;
    return this;
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
