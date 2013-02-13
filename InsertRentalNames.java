import com.electec.*;
import com.acerenting.AceRentingDatabaseOperation;
import java.util.*;
import java.sql.*;
import java.io.*;

public class InsertRentalNames
{
  private Properties myProperties = new Properties ();
  private DatabaseOperation database;
  private boolean debug;
  private String sourceFile;
  private String content;  
  
  private String table;
  private int parentid;
  
  public InsertRentalNames () 
  {
    try {
      UtilityTools.showMessage( "***** " + UtilityTools.getCurrentTimestamp() +
                                " InsertRentalNames starts   *****\n" +
                                  "*********************************************\n");
      myProperties.load(new FileInputStream ("InsertRentalNames.properties"));
      
      sourceFile = myProperties.getProperty("SOURCE_FILE");
      if (sourceFile==null)
        throw new Exception ("Can not find the name of sourceFile in the property file");
        
      table = myProperties.getProperty("TABLE_NAME");
      if (table==null || table.equals(""))
        throw new Exception ("Can not find the table name in property file");
        
      try {
        debug = Boolean.parseBoolean(myProperties.getProperty("DEBUG_ON"));
      } catch (Exception ex) {
        System.out.println ("The value of \"DEBUG_ON\" in property file must be either true or false  (case sensitive)");
        System.exit (1);
      }
      
      try {
        parentid = Integer.parseInt(myProperties.getProperty("PARENT_ID"));
        if (parentid < 0)
          throw new Exception ("negative parentid");
      } catch (Exception ex) {
        System.out.println ("The value of PARENT_ID must be integer greater than or equal to zero");
        System.exit(1);
      }
      database = new AceRentingDatabaseOperation (debug);
      
    } catch (Exception ex) {
//      ex.printStackTrace();
      System.out.println ("Error: Error in loading the properties file.");
      System.out.println (ex.getMessage());
      System.exit (1);
    }
    try {
      content = UtilityTools.readFromFile(sourceFile);
     // String[] name = sourceFile.split("\\.");
      String[] contents = content.split ("\n");
      PreparedStatement pStatement = 
      database.getDatabaseConnection().prepareStatement(" INSERT INTO " + table + 
                                                        " (cityid, category, company_name, url) " + 
                                                        " VALUES (?, ?, ?, ?) " +
							" ON DUPLICATE KEY UPDATE cityid=?, category=?, company_name=?, url=?"
                                                       );
      int count = 0;
      
        pStatement.setInt(1, parentid); //the city_id
        pStatement.setInt(5, parentid); //duplicated id
        pStatement.setString(2, contents[0]); // the category
        pStatement.setString(6, contents[0]); // duplicated name
        pStatement.setString(3, contents[1]); // the company name
        pStatement.setString(7, contents[1]); // duplicated address
        pStatement.setString(4, contents[2]); // the url
        pStatement.setString(8, contents[2]); // duplicated address

        pStatement.executeUpdate ();
         
        count++;
      
      pStatement.close();
      System.out.println ("Success: " + count + " record has been inserted into table \"" + table + "\"");
      
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      database.closeDatabaseConnection ();
      UtilityTools.showMessage( "***** " + UtilityTools.getCurrentTimestamp() +
                                " program terminates   *****\n" +
                                  "*********************************************\n");
    }
  }
  public static void main (String [] args)
  {
System.out.println ("8*");
    new InsertRentalNames ();
  }
}