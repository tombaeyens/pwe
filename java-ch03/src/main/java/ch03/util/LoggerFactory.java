package ch03.util;


/**
 * @author Tom Baeyens
 */
public class LoggerFactory {
  
  static Logger logger = new Logger();

  public static Logger getLogger(Class<?> clazz) {
    return logger;
  }
}
