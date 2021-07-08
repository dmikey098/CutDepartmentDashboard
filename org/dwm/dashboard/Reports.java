/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dwm.dashboard;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;


/**
 *
 * @author Daniel Mikesell
 */
public class Reports {
    public static void exportCutQueue() {
        String[] columnNames = {
    		"Priority",
            "Order",
            "License Plate",
            "Part Number",
            "Order Qty",
            "On-Hand Qty",
            "Wave",
            "Carrier",
            "Status",
            "User",
            "Type",
            "Customer",
            "Reel",
            "OD",
            "Line",
            "Ship Date",
            "Ctrl Number",
            "Parcel Number",
        };
    
        ArrayList<ArrayList<String>> rows = new ArrayList<>();
        
        QueueManager.getFilteredCuts().forEach(item -> {
            ArrayList<String> row = new ArrayList<>();
        
            row.add(Integer.toString(item.priority.get()));
            row.add(item.orderNumber.get());
            row.add(item.licensePlate.get());
            row.add(item.partNumber.get());
            row.add(Integer.toString(item.orderQuantity.get()));
            row.add(Integer.toString(item.onHandQuantity.get()));
            row.add(Integer.toString(item.wave.get()));
            row.add(item.carrier.get());
            row.add(item.status.get());
            row.add(item.user.get());
            row.add(item.type.get());
            row.add(item.customer.get());
            row.add(item.reel.get());
            row.add(Double.toString(item.cableDiameter.get()));
            row.add(Integer.toString(item.line.get()));
            row.add(item.shipOnDate.get());
            row.add(item.ctrlNum.get());
            row.add(item.parcelNum.get());
            rows.add(row);
        });
                
         
        String dirName = System.getProperty("user.home") + "\\Documents";
        File tempFile = null;
        try {
            tempFile = File.createTempFile("temp", ".xlsx", new File(dirName));
            tempFile.deleteOnExit();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        
        ExcelWriter.WriteTable(tempFile, rows, columnNames, true);
    }
    
    public static void exportReplenQueue() {
        String[] columnNames = {
        	"Cut Priority",
        	"Priority",
            "Order",
            "License Plate",
            "Part Number",
            "Item Desc",
            "Asle",
            "Bay",
            "Level",
            "On-Hand Qty",
            "Cuts on LP",
            "SP"
        };
    
        ArrayList<ArrayList<String>> rows = QueryFunctions.getReplenExport();        
        
        String dirName = System.getProperty("user.home") + "\\Documents";
        File tempFile = null;
        try {
            tempFile = File.createTempFile("temp", ".xlsx", new File(dirName));
            tempFile.deleteOnExit();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        
        if(rows.size() != 0) {
        	ExcelWriter.WriteTable(tempFile, rows, columnNames, true);
        }
    }
    
    public static void exportPNCs() {
        String[] columnNames = {
        		"YR",
                "MMDD",
                "Time",
                "Part Number",
                "Qty",
                "Lot",
                "Src Type",
                "PMCMPN",
                "PMALPH"
        };
        
    	ArrayList<ArrayList<String>> rows = QueryFunctions.getPNCOutput();        
        
        String dirName = System.getProperty("user.home") + "\\Documents";
        File tempFile = null;
        try {
            tempFile = File.createTempFile("temp", ".xlsx", new File(dirName));
            tempFile.deleteOnExit();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        
        if(rows.size() != 0) {
        	ExcelWriter.WriteTable(tempFile, rows, columnNames, true);
        }
    }
    
    public static void exportTotals() {
    	String[] columnNames = {
    		"Key",
            "Value"
        };
    	        
    	ArrayList<ArrayList<String>> rows = new ArrayList<>();
    	
    	ArrayList<String> row = new ArrayList<>();
    	row.add("Cuts");
    	row.add(Integer.toString(QueueManager.getFilteredCuts().size()));
    	rows.add(row);
    	
    	row = new ArrayList<>();
    	row.add("SP");
    	row.add(Integer.toString(QueueManager.spCount.get()));
    	rows.add(row);
    	
    	row = new ArrayList<>();
    	row.add("Reels");
    	row.add(Integer.toString(QueueManager.reelCountProperty.get()));
    	rows.add(row);
    	
    	row = new ArrayList<>();
    	row.add("BW");
    	row.add(Integer.toString(QueueManager.bwCountProperty.get()));
    	rows.add(row);
    	
    	row = new ArrayList<>();
    	row.add("Cord");
    	row.add(Integer.toString(QueueManager.cordCountProperty.get()));
    	rows.add(row);
    	
    	row = new ArrayList<>();
    	row.add("Fiber");
    	row.add(Integer.toString(QueueManager.fiberCountProperty.get()));
    	rows.add(row);
    	
    	row = new ArrayList<>();
    	row.add("PV");
    	row.add(Integer.toString(QueueManager.pvCountProperty.get()));
    	rows.add(row);
    	
    	row = new ArrayList<>();
    	row.add("Shuttles");
    	row.add(Integer.toString(QueueManager.getFilteredShuttles().size()));
    	rows.add(row);
    	
    	row = new ArrayList<>();
    	row.add("Replens");
    	row.add(Integer.toString(QueueManager.getFilteredReplens().size()));
    	rows.add(row);
    	
    	row = new ArrayList<>();
    	row.add("Cases");
    	row.add(Integer.toString(PickQueueTotals.caseProperty().get()));
    	rows.add(row);
    	
    	row = new ArrayList<>();
    	row.add("Pallets");
    	row.add(Integer.toString(PickQueueTotals.palletProperty().get()));
    	rows.add(row);
    	
    	row = new ArrayList<>();
    	row.add("UPS");
    	row.add(Integer.toString(PickQueueTotals.upsProperty().get()));
    	rows.add(row);
    	
    	row = new ArrayList<>();
    	row.add("Grainger");
    	row.add(Integer.toString(PickQueueTotals.graingerProperty().get()));
    	rows.add(row);
    	
        
        String dirName = System.getProperty("user.home") + "\\Documents";
        File tempFile = null;
        try {
            tempFile = File.createTempFile("temp", ".xlsx", new File(dirName));
            tempFile.deleteOnExit();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        
        if(rows.size() != 0) {
        	ExcelWriter.WriteTable(tempFile, rows, columnNames, true);
        }
    }
    
    public static void totalsLogExport() {
    	String output = "";
    	
    	output += LocalDateTime.now() + "\t";
    	output += AppManager.userProperty().get() + "\t";
    	output += Integer.toString(QueueManager.getFilteredCuts().size()) + "\t";
    	output += Integer.toString(QueueManager.getFilteredReplens().size()) + "\t";
    	output += Integer.toString(QueueManager.allShuttles.size()) + "\t";
    	output += QueueManager.countBinding(QueueManager.SHUTTLE, item -> { return item.isCradle(); }).get();
    	
    	try {
    		new File("G:\\Clerical\\Cut Lead\\Tools\\Dashboard\\config\\test.txt").getParentFile().mkdirs();
    		Files.write(Paths.get("G:\\Clerical\\Cut Lead\\Tools\\Dashboard\\config\\test.txt"), output.getBytes(), StandardOpenOption.APPEND);
    		
		} catch (IOException e) {			
			e.printStackTrace();
		}
    }
}
