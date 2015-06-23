package ch02;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/** represents a database, stores serialized data.
 * 
 *  The database has a set of tables.
 *  Each table maps ids to serialized representation (String) of objects.*/
public class Db {

  /** maps table name to table map */
  Map<String,Map<String,String>> tables = new ConcurrentHashMap<>();
  
  public void save(String tableName, String id, String value) {
    Map<String,String> table = tables.get(tableName);
    if (table==null) {
      table = new ConcurrentHashMap<>();
      tables.put(tableName, table);
    }
    table.put(id, value);
  }
  
  public String find(String tableName, String id) {
    Map<String,String> table = tables.get(tableName);
    return table!=null ? table.get(id) : null;
  }

}
