package ch03.util;


/**
 * @author Tom Baeyens
 */
public class ApiException {

  public static void checkNotNullParameter(Object object, String parameterName) {
    checkTrue(object!=null, "Parameter %s is null", parameterName);
  }

  public static void checkNotNull(Object object, String message, String... args) {
    checkTrue(object!=null, message, args);
  }

  public static void checkTrue(boolean condition, String message, String... args) {
    if (!condition) {
      throw new java.lang.RuntimeException();
    }
  }
}
