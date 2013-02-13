import com.electec.*;
import com.acerenting.AceRentingDatabaseOperation;
import java.util.*;
import java.sql.*;
import java.io.*;

public class InsertDealerNames
{
  private Properties myProperties = new Properties ();
  private DatabaseOperation database;
  private boolean debug;
  private String sourceFile;
  private String content;  
  
  private String table;
  private int parentid;
  
  public InsertDealerNames () 
  {
    try {
      UtilityTools.showMessage( "***** " + UtilityTools.getCurrentTimestamp() +
                                " InsertDealerNames starts   *****\n" +
                                  "*********************************************\n");
      myProperties.load(new FileInputStream ("InsertDealerNames.properties"));
      
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
                                                        " (cityid, company, company_address, manufacturer, sales_manager, sales_manager_number, company_number, sales_manager_email, url) " + 
                                                        " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
							" ON DUPLICATE KEY UPDATE cityid=?, company=?, company_address=?, manufacturer=?, sales_manager=?, sales_manager_number=?, company_number=?, sales_manager_email=?, url=?"
                                                       );
      int count = 0;
      
        pStatement.setInt(1, parentid); //the city_id
        pStatement.setInt(10, parentid); //duplicated id
        pStatement.setString(2, contents[0]); // the name
        pStatement.setString(11, contents[0]); // duplicated name
        pStatement.setString(3, contents[1]); // the address
        pStatement.setString(12, contents[1]); // duplicated address
        
        String[] cars = new String[]{"Ford","Mitsubishi","Chrysler","Dodge","Honda","Jeep","Kia"
        ,"Hyundai","Lexus","Toyota","Mazda","Saturn","Saab","Subaru","Suzuki","Volkswagen","Chevrolet","Acura"
        ,"Infiniti","Cadillac","Oldsmobile","Lincoln","Mercury","Hummer","LandRover","Isuzu","Audi","Mercedes",
        "Pontiac","GMC","Buick","Nissan","Porsche","Volvo","Mini","BMW","AlfaRomeo","AstonMartin","Ferrari",
        "Lamborghini","Maserati","Bentley","RollsRoyce","Jaguar","Lotus"};
        String manu = "";
        for (int i = 0; i < cars.length; i++) {
            if (contents[0].indexOf(cars[i]) >= 0) {
                manu += cars[i] + " ";
            }
        }
        pStatement.setString(4, manu); // the manu
        pStatement.setString(13, manu); // duplicated manu
        
        if (contents.length > 4) {
        
        pStatement.setString(5, contents[2]); // the manager
        pStatement.setString(14, contents[2]); // duplicated manager
        pStatement.setString(6, contents[3]); // the manager number
        pStatement.setString(15, contents[3]); // duplicated manager number
        pStatement.setString(7, contents[4]); // the number
        pStatement.setString(16, contents[4]); // duplicated number
        pStatement.setString(8, contents[5]); // the email
        pStatement.setString(17, contents[5]); // duplicated email
        pStatement.setString(9, contents[6]); // the url
        pStatement.setString(18, contents[6]); // duplicated url
        
        } else {

        pStatement.setString(5, ""); // the manager
        pStatement.setString(14, ""); // duplicated manager
        pStatement.setString(6, contents[3]); // the manager number
        pStatement.setString(15, contents[3]); // duplicated manager number
        pStatement.setString(7, contents[3]); // the number
        pStatement.setString(16, contents[3]); // duplicated number
        pStatement.setString(8, ""); // the email
        pStatement.setString(17, ""); // duplicated email
        pStatement.setString(9, ""); // the url
        pStatement.setString(18, ""); // duplicated url
           
        }

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
    new InsertDealerNames ();
  }
}