package ch03.data.types;

import ch03.data.Type;


/**
 * @author Tom Baeyens
 */
public abstract class AbstractType implements Type {

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    return true;
  }
}
