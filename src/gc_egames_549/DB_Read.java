/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gc_egames_549;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
        
public class DB_Read {
    //private data fileds
    //database URL (networked location of database server)
    private String dbURL;
    //user id to access database
    private String usrID;
    //user password to access database
    private String usrPWD;
    
    //private data fields for database connection and SQL operations
    private Connection conn;
    private Statement stmt;
    private ResultSet rs;
    private int recordCount; //number of records that result from the SQL execution
    private String errorMessage; //error messages relating to SQL
    private Object [][] objDataSet; //Object [][] 2-D array for populating JTable
    private String [] stringCSVData; //String [] array to contain team and event details
    private int maxCompID; //highest value of the competitionID
                           //used to set up a competitionID for a new competition  
    


public DB_Read (String sql, String qryType)
{
    dbURL = "";
    usrID = "";
    usrPWD = "";
    recordCount = 0;
    errorMessage = "";
    objDataSet = null;
    stringCSVData = null;
    maxCompID = 0;
    
    try
    {
        BufferedReader br = new BufferedReader (new FileReader ("app.config"));
        String line = br.readLine();
        int lineCounter = 1;
        while (line != null)
        {
            switch(lineCounter)
            {
                case 1:
                    dbURL = line.substring(6,line.length());
                    break;
                case 2:
                    usrID = line.substring(6,line.length());
                    break;
                case 3:
                    usrPWD = line.substring(7,line.length());
                    break;
                default:
                    break;
            }
            
            line = br.readLine();
            lineCounter++;
        }
        
        System.out.println(this.toString());
        br.close();
    }
    
    catch (IOException ioe)
    {
        System.out.println("ERROR: External config file not found!");
        errorMessage = ioe.getMessage() + "\n";
    } 
    
    catch (Exception e)
    {
        System.out.println("ERROR: Problem with reading the config file!");
        errorMessage = e.getMessage() + "\n";
    }
    
    try
    {
        conn = DriverManager.getConnection(dbURL, usrID, usrPWD);
        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        rs = stmt.executeQuery(sql);
        
        if (rs != null)
        {
            rs.beforeFirst();
            rs.last();
            recordCount = rs.getRow();
        }
        
        if (recordCount > 0 )
        {
           int counter = 0;
           objDataSet = new Object [recordCount][];
           stringCSVData = new String [recordCount];
           maxCompID = 0;
           
           rs.beforeFirst();
           
           while (rs.next())
           {
               if (qryType.equals("competition"))
               {
                   Object [] obj = new Object [5];
                   obj[0] = rs.getString("gameName");
                   obj[1] = rs.getString("team1");
                   obj[2] = rs.getInt("team1Points");
                   obj[3] = rs.getString("team2");
                   obj[4] = rs.getInt("team2Points");
                   
                   objDataSet[counter] = obj;
                   counter++;
               }
               
               else if (qryType.equals("event"))
               {
                   stringCSVData[counter] = rs.getString("name")
                           + "," + formatDateToString (rs.getString("date"))
                           + "," + rs.getString("location");
                   counter++;
               }
               
               else if (qryType.equals("team"))
               {
                   stringCSVData[counter] = rs.getString("name")
                           + "," + rs.getString("contact")
                           + "," + rs.getString("phone")
                           + "," + rs.getString("email");
                    counter++;       
               }
               
               else if (qryType.equals("game"))
               {
                   stringCSVData[counter] = rs.getString("name");
                   counter++;
               }
               
               else if (qryType.equals("leaderBoard"))
               {
                   Object [] obj = new Object [2];
                   obj[0] = rs.getString("name");
                   obj[1] = rs.getInt("points");
                   objDataSet[counter] = obj;
                   counter++;
               }
               
               else if (qryType.equals("maxCompID"))
               {
                   maxCompID = rs.getInt("maxID");
               }
           }
           
           conn.close();
        }
    }
    
    catch(SQLException sqlE)
    {
        errorMessage = sqlE.getMessage();
    }
}

public int getRecordCount()
{
    return recordCount;
}

public String getErrorMessage()
{
    return errorMessage;
}

public int getMaxCompID()
{    
    return maxCompID;
}

public String[] getStringCSVData()
{
    return stringCSVData;
}

public Object[][] getObjDataSet()
{
    return objDataSet;
}

public static String formatDateToString(String inputDateString)
{
    String formattedDateStr = "";
    String day = inputDateString.substring(8,10);
    String year = inputDateString.substring(0,4);
    String month = "Jan";
    String monthNbr = inputDateString.substring(5,7);
    switch (monthNbr)
    {
        case "02":
            month = "Feb";
            break;
        case "03":
            month = "Mar";
            break;
        case "04":
            month = "Apr";
            break;
        case "05":
            month = "May";
            break;
        case "06":
            month = "Jun";
            break;
        case "07":
            month = "Jul";
            break;
        case "08":
            month = "Aug";
            break;
        case "09":
            month = "Sep";
            break;
        case "10":
            month = "Oct";
            break;
        case "11":
            month = "Nov";
            break;
        case "12":
            month = "Dec";
            break;
    }
    
    formattedDateStr = day + "-" + month + "-" + year;
      
    return formattedDateStr;
            
}
}