/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gc_egames_549;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

/**
 *
 * @author yolanda
 */
public class DB_Write {
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
    //number of records that result from the SQL execution
    private String errorMessage; 
       

public DB_Write (String sql)
{
    dbURL = "";
    usrID = "";
    usrPWD = "";
    errorMessage = "";
    
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
        conn = java.sql.DriverManager.getConnection(dbURL, usrID, usrPWD);
        stmt = conn.createStatement();
        stmt.executeUpdate(sql);
        conn.close();
    }
    catch (SQLException sqle)
    {
        errorMessage += sqle.getMessage();
    } 
    catch (Exception e)
    {
        errorMessage += e.getMessage();
    }
}

public String getErrorMessage()
{
    return errorMessage;
}

/*@Override
public String toString()
{
    return "Database URL = " + dbURL + " USR ID = " + usrID + " USR PWD = " + usrPWD;
}*/

}