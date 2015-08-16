package ch03.data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ch03.engine.context.Context;


/**
 * @author Tom Baeyens
 */
public class Condition {
  
  public static Set<String> FALSE_STRINGS = new HashSet<>(Arrays.asList(new String[]{
    "false",
    "disable",
    "disabled",
    "disapprove",
    "disapproved",
    "negative",
    "no",
    "not",
    "reject",
    "rejected"
    // TODO append all other languages :)
  }));

  InputExpression expression;

  public boolean evaluate(Context context) {
    TypedValue typedValue = expression.getTypedValue(context);
    if (typedValue==null || typedValue.value==null) {
      return false;
    }
    Object value = typedValue.value;
    if (Boolean.FALSE.equals(value)) {
      return false;
    }
    if (value instanceof String) {
      String lower = ((String)value).toLowerCase();
      if (FALSE_STRINGS.contains(lower)) {
        return false;
      }
    }
    return true;
  }
}
