package ch03.util;


/**
 * @author Tom Baeyens
 */
public class Logger {

  public void debug(String msg) {
    System.out.println(msg);
  }

  public void debug(String format, Object... arguments) {
    System.out.println(String.format(format, arguments));
  }
}
