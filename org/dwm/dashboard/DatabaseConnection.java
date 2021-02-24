/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dwm.dashboard;

import java.sql.Connection; 
import java.sql.SQLException;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDataSource; 
  
public class DatabaseConnection { 
    private static Connection conn = null; 
    
    static {
        String systemName = "HQPLANO01"; //Credentials.getSystemName(); 
        String userid = "LBQUTM1"; //Credentials.getUserid(); 
        String password = "GENERAL1"; //Credentials.getPassword();
        
        try {
            conn = new AS400JDBCDataSource(new AS400(systemName, userid, password)).getConnection();
        } 
        catch (SQLException ex) { 
            ex.printStackTrace();
        } 
    } 
        
    public static Connection getConnection() { 
        return conn; 
    }
} 
