package ch03.concurrency.infrastructure;


/**
 * @author Tom Baeyens
 */
public class Log {

  protected String threadId;
  protected String message;

  public Log(String threadId, String message) {
    this.threadId = threadId;
    this.message = message;
  }
  
  public String getThreadId() {
    return threadId;
  }
  public String getMessage() {
    return message;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((message == null) ? 0 : message.hashCode());
    result = prime * result + ((threadId == null) ? 0 : threadId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Log other = (Log) obj;
    if (message == null) {
      if (other.message != null)
        return false;
    } else if (!message.equals(other.message))
      return false;
    if (threadId == null) {
      if (other.threadId != null)
        return false;
    } else if (!threadId.equals(other.threadId))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return threadId + " | " + message;
  }
}
