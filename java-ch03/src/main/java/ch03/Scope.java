package ch03;

import java.util.LinkedHashMap;
import java.util.Map;

import ch03.data.Expression;


public abstract class Scope {
  
  Map<String,Object> configuration = new LinkedHashMap<>();
  Map<String,Expression> inputs = new LinkedHashMap<>();
  Map<String,Expression> outputs = new LinkedHashMap<>();
  Map<String,Activity> activities = new LinkedHashMap<>();
  Map<String,Variable> variables = new LinkedHashMap<>();

}
