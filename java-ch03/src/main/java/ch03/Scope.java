package ch03;

import java.util.LinkedHashMap;
import java.util.Map;

import ch03.data.InputExpression;
import ch03.data.OutputExpression;


public abstract class Scope {
  
  Map<String,Object> configuration = new LinkedHashMap<>();
  Map<String,InputExpression> inputs = new LinkedHashMap<>();
  Map<String,OutputExpression> outputs = new LinkedHashMap<>();
  Map<String,Activity> activities = new LinkedHashMap<>();
  Map<String,Variable> variables = new LinkedHashMap<>();

}
