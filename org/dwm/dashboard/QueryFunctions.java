/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dwm.dashboard;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Daniel Mikesell
 */
public class QueryFunctions {
    static Connection conn = DatabaseConnection.getConnection();
    
    /**
     * @param sqlStmt 
     * @return
     * @throws SQLException 
     */
    public static ResultSet query(String sqlStmt) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sqlStmt);
        ResultSet rs = ps.executeQuery();
        return rs;
    }
    
    public static Boolean execute(String sqlStmt) throws SQLException {
        return conn.prepareStatement(sqlStmt).execute();
    }
    
    public static String getVersion() {
        String sqlStmt = "SELECT VALUE FROM BBQYXXX.DASHBOARD WHERE PROPERTY = 'version'";
        String version = "unk";
        
        try (ResultSet rs = query(sqlStmt)) {
            if(rs.isBeforeFirst()) {
                rs.next();
                version = rs.getString(1);
            }
        } catch (SQLException ex) {
            
        }
        
        return version;
    }
    
    public static void wflod() throws IOException  {
        ObservableList<String> list = FXCollections.observableArrayList();
        String sqlStmt = readSQLFile("/sql/WFLOD.sql");
        
        try( ResultSet rs = query(sqlStmt)) {
            while(rs.next()) {
                list.add(rs.getString("LDOLCP"));
            }
            
        } catch(SQLException ex) {
            
        }
        
        System.out.println(list.size());
        System.exit(0);
    }
    
    
    public static String readSQLFile(String strFile) throws FileNotFoundException, IOException {
        InputStream is = QueryFunctions.class.getResourceAsStream(strFile);
        String line = "";
        String lines = "";
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            while((line = br.readLine()) != null) {			
                lines += " " + line.trim();
            }			
        } catch (IOException ex) {
            
        }

        return lines.trim();
    }
    
    public static ResultSet getAllCuts() throws SQLException {
        String sqlStmt = "";
        try {
            sqlStmt = readSQLFile("/sql/CutQueue.sql");
        } catch (IOException ex) { 
            ex.printStackTrace();
        }
        
        return query(sqlStmt);
    }
    
    public static ResultSet getAllReplens() throws SQLException {
        String sqlStmt = "";
        try {
            sqlStmt = readSQLFile("/sql/ReplenQueue.sql");
        } catch (IOException ex) {
            
        }
        
        return query(sqlStmt);
    }
    
    public static ResultSet getAllShuttles() throws SQLException {
        String sqlStmt = "";
        try {
            sqlStmt = readSQLFile("/sql/ShuttleQueue.sql");
        } catch (IOException ex) {
            
        }
        
        return query(sqlStmt);
    }
    
    public static ResultSet getAllPallets() throws SQLException {
        String sqlStmt = "";
        try {
            sqlStmt = readSQLFile("/sql/PalletQueue.sql");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return query(sqlStmt);        
    }
    
    public static int getPNCTotal() {
        int total = 99;
        
        String sqlStmt = "SELECT SUM(THTQTY) AS TOTAL FROM PROBASEF.WFTRH WHERE THWHS = 'LB' AND THRSRT = 'PNC' AND THTTYP IN ('PCKV','PUTV') AND THDATS > ? GROUP BY THRSRT ";
        PreparedStatement ps = null;
        
        try {
            ps = conn.prepareStatement(sqlStmt);
            ps.setString(1, AppManager.getPNCFromDate());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        try(ResultSet rs = ps.executeQuery()) {
            rs.next();
            total = rs.getInt(1);
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return total;
    }
    
    public static int getCasePickTotal() throws IOException {
        int total = 0;
        String sqlStmt = QueryFunctions.readSQLFile("/sql/CaseCount.sql");

        try(ResultSet rs = query(sqlStmt)) {
            if(rs.isBeforeFirst()) {
                rs.next();
                total = rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return total;
    }

    public static int getPalletPickTotal() throws IOException {
        int total = 0;
        String sqlStmt = QueryFunctions.readSQLFile("/sql/PalletCount.sql");

        try(ResultSet rs = query(sqlStmt)) {
            if(rs.isBeforeFirst()) {
                rs.next();
                total = rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return total;
    }
    
    public static int getUPSPickTotal() throws IOException {
        int total = 0;
        String sqlStmt = QueryFunctions.readSQLFile("/sql/UPSCount.sql");

        try(ResultSet rs = query(sqlStmt)) {
            if(rs.isBeforeFirst()) {
                rs.next();
                total = rs.getInt(1);
            }
        } catch (SQLException ex) {
            
        }
        
        return total;
    }
    
    public static int getGraingerPickTotal() throws IOException {
        int total = 0;
        String sqlStmt = QueryFunctions.readSQLFile("/sql/GraingerCount.sql");

        try(ResultSet rs = query(sqlStmt)) {
            if(rs.isBeforeFirst()) {
                rs.next();
                total = rs.getInt(1);
            }
        } catch (SQLException ex) {
            
        }
        
        return total;
    }
    
    static String[] columns = null;
    
    
    public static ArrayList<ArrayList<Object>> selectAsList(String sqlStmt) {
        ArrayList<ArrayList<Object>> list = new ArrayList<>();
        boolean exists = false;

        try (ResultSet rs = query(sqlStmt)) {
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();

            while(rs.next()) {
                exists = true;
                ArrayList<Object> row = new ArrayList<>();
                columns = new String[columnCount];

                for(int i = 1; i <= columnCount; i++) {
                    columns[i - 1] = md.getColumnName(i);
                    row.add(rs.getObject(i));
                }

                list.add(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } 		

        if(exists) {
            return list; 
        } 

        return null;
    }
    
    public static ArrayList<ArrayList<String>> selectAsStringList(String sqlStmt) {
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        boolean exists = false;

        try (ResultSet rs = query(sqlStmt)) {
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();

            while(rs.next()) {
                exists = true;
                ArrayList<String> row = new ArrayList<>();
                columns = new String[columnCount];

                for(int i = 1; i <= columnCount; i++) {
                    columns[i - 1] = md.getColumnName(i);
                    row.add(rs.getString(i));
                }

                list.add(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } 		

        if(exists) {
            return list; 
        } 

        return null;
    }

    public static ObservableList<HistoryItem> getLast10InventoryTransactions() {
        ObservableList<HistoryItem> list = FXCollections.observableArrayList();
        String sqlStmt = null;
        
        try {
            sqlStmt = QueryFunctions.readSQLFile("/sql/Last10InventoryTransactions.sql");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
         
        try(ResultSet rs = query(sqlStmt)) {
            while(rs.next()) {
                HistoryItem item = new HistoryItem();
                item.setAisle(rs.getString(1));
                item.setBay(rs.getString(2));
                item.setLevel(rs.getString(3));
                item.setItem(rs.getString(4));
                item.setOlcp(rs.getString(5));
                item.setLot(rs.getString(6));
                item.setSourceType(rs.getString(7));
                item.setType(rs.getString(8));
                item.setUser(rs.getString(9));
                item.setQuantity(rs.getInt(10));
                item.setStrDate(rs.getString(11));
                item.setStrTime(rs.getString(12));
                list.add(item);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return list;
    }
    
    public static ArrayList<ArrayList<String>> getReplenExport() {
    	ArrayList<ArrayList<String>> rows = new ArrayList<>();
    	String sqlStmt = null;
        
        try {
            sqlStmt = QueryFunctions.readSQLFile("/sql/ReplenExport.sql");
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        PreparedStatement ps = null;
        
        try {
            ps = conn.prepareStatement(sqlStmt);
            ps.setInt(1, AppManager.getStraightPullQuantity());
            ps.setInt(2, AppManager.getStraightPullQuantity());
            ps.setInt(3, Queues.getFilteredReplens().get(0).priority.get());
            ps.setInt(4, Queues.getFilteredReplens().get(Queues.getFilteredReplens().size() - 1).priority.get());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        try(ResultSet rs = ps.executeQuery()) {
        	while(rs.next()) {
	        	ResultSetMetaData md = rs.getMetaData();
	        	ArrayList<String> row = new ArrayList<>();
	        	
	        	for(int i = 1; i <= md.getColumnCount(); i++) {
	        		row.add(rs.getString(i));
	        	}
	        	
	        	rows.add(row);
        	}
        } catch (SQLException ex) {
        	ex.printStackTrace();
        }
        
    	
    	return rows;
    }
    
    public static ObservableList<UserTotal> getUserTotalsForShift() {
        ObservableList<UserTotal> rows = FXCollections.observableArrayList();
        String sqlStmt = null;
        
        try {
            sqlStmt = QueryFunctions.readSQLFile("/sql/UserTotals.sql");
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        PreparedStatement ps = null;
        
        try {
            ps = conn.prepareStatement(sqlStmt);
            ps.setInt(1, 1210114);
            ps.setInt(2, 1210115);
            ps.setInt(3, 1210115);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
         
        try(ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                UserTotal userTotal = new UserTotal();
                userTotal.teamProperty().set(rs.getString(1)); 
                userTotal.nameProperty().set(rs.getString(2)); 
                userTotal.totalProperty().set(rs.getInt(3)); 
                rows.add(userTotal);
            }
        } catch (SQLException | NullPointerException ex) {
            ex.printStackTrace();
        }
        
        return rows;    
    }
    
    public static ObservableList<ArrayList<String>> getOrderNotes(String orderNumber) {
        ObservableList<ArrayList<String>> rows = FXCollections.observableArrayList();
        String sqlStmt = null;
        
        try {
            sqlStmt = QueryFunctions.readSQLFile("/sql/OrderNotes.sql");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        PreparedStatement ps = null;
        
        try {
            ps = conn.prepareStatement(sqlStmt);
            ps.setString(1, orderNumber);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
         
        try(ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                ArrayList<String> row = new ArrayList<>();
                row.add(rs.getString(1));
                row.add(rs.getString(2));
                row.add(rs.getString(3));
                row.add(rs.getString(4));
                rows.add(row);
            }
        } catch (SQLException | NullPointerException ex) {
            ex.printStackTrace();
        }
        
        return rows;    
    }
    
    public static ArrayList<ArrayList<String>> getPNCOutput() {
        ArrayList<ArrayList<String>> rows = new ArrayList<>();
        PreparedStatement ps = null;
        
        try {
            ps = conn.prepareStatement(QueryFunctions.readSQLFile("/sql/PNCExport.sql"));
            ps.setString(1, AppManager.getPNCFromDate());
        } catch (IOException | SQLException ex) {
            ex.printStackTrace();
        }
        
        try (ResultSet rs = ps.executeQuery()) {
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();

            while(rs.next()) {
                ArrayList<String> row = new ArrayList<>();
                columns = new String[columnCount];

                for(int i = 1; i <= columnCount; i++) {
                    columns[i - 1] = md.getColumnName(i);
                    row.add(rs.getString(i));
                }

                rows.add(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } 		
        
        return rows;
    }

    public static int getAccessForUser(String username) {
        PreparedStatement ps = null;
        int access = 0;
        
        try {
            ps = conn.prepareStatement(QueryFunctions.readSQLFile("/sql/Access.sql"));
            ps.setString(1, username);
        } catch (IOException | SQLException ex) {
            ex.printStackTrace();
        }
        
        try (ResultSet rs = ps.executeQuery()) {
            if(rs.isBeforeFirst()) {
                rs.next();
                
                access = rs.getInt(1);
        
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } 		
        
        
        
        return access;
    }
    
    
}

