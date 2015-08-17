package ch03.workflow;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Tom Baeyens
 */
public class Console {
  
  List<String> lines = new ArrayList<>();
  
  public void print(String line) {
    lines.add(line);
  }

}
