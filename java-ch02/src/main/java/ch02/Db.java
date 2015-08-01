package ch02;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/** represents a database, stores serialized data.
 * 
 *  The database has a set of tables.
 *  Each table maps ids to serialized representation (String) of objects.*/
public class Db {
  
  private static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss");
  
  /** maps table name to table map */
  Map<String,Map<String,String>> tables = new ConcurrentHashMap<>();
  
  public void save(String tableName, String id, String value) {
    Map<String,String> table = tables.get(tableName);
    if (table==null) {
      table = new ConcurrentHashMap<>();
      tables.put(tableName, table);
    }
    System.out.println("--"+tableName+"-"+id+"--> "+value);
    table.put(id, value);
  }
  
  public String find(String tableName, String id) {
    Map<String,String> table = tables.get(tableName);
    return table!=null ? table.get(id) : null;
  }

  public static String convertDateToString(Date date) {
    return date!=null ? DATE_FORMAT.format(date) : null;
  }
  
  public static Date convertStringToDate(String dateString) {
    try {
      return dateString!=null ? DATE_FORMAT.parse(dateString) : null;
    } catch (ParseException e) {
      throw new RuntimeException("Bad date format '"+dateString+"': "+e.getMessage(), e);
    }
  }
  
}
