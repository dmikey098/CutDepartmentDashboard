/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dwm.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.dwm.dashboard.bean.QueueItem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Daniel Mikesell
 */
public class CutQueue {
    private  ArrayList<String> duplicateLPs = new ArrayList<>();
    
    public ArrayList<String> getDuplicatePlates() {
        return duplicateLPs;
    }
    
    
    public ObservableList<QueueItem> getAllItems() {
        ObservableList<QueueItem> allItems = FXCollections.observableArrayList();
        
        try ( ResultSet rs = QueryFunctions.getAllCuts() ) {            
            Map<Object, Object> dups = new HashMap<>();
            while(rs.next()) {
                QueueItem item = new QueueItem();
                item.priority.set(rs.getInt(1));
                item.orderNumber.set((rs.getString(2) != null) ? rs.getString(2).trim() : "");
                item.licensePlate.set((rs.getString(3) != null) ? rs.getString(3).trim() : "");
                item.partNumber.set((rs.getString(4) != null) ? rs.getString(4).trim() : "");
                item.orderQuantity.set(rs.getInt(5));
                item.onHandQuantity.set(rs.getInt(6));
                item.carrier.set((rs.getString(7) != null) ? rs.getString(7).trim() : "");
                item.wave.set(rs.getInt(8));
                item.status.set((rs.getString(9) != null) ? rs.getString(9).trim() : "");
                item.user.set((rs.getString(10) != null) ? rs.getString(10).trim() : "");
                item.type.set((rs.getString(11) != null) ? rs.getString(11).trim() : "");
                item.customer.set((rs.getString(12) != null) ? rs.getString(12).trim() : "");
                item.line.set(rs.getInt(13) / 100);
                item.cableDiameter.set(rs.getDouble(14));
                item.reel.set((rs.getString(15) != null) ? rs.getString(15).trim() : "");
                item.shipOnDate.set((rs.getString(16) != null) ? rs.getString(16).trim() : "");
                item.wlu.set((rs.getString(20) != null) ? rs.getString(20).trim() : "");
                
                if(item.reel.get().equals("ERR")) {
                	item.reel.set(Utility.getReelSize(item));
                }
                
                
                item.customerId.set((rs.getString(17) != null) ? rs.getString(17).trim() : "");
                
                item.ctrlNum.set((rs.getString(18) != null) ? rs.getString(18).trim() : "");
                item.parcelNum.set((rs.getString(19) != null) ? rs.getString(19).trim() : "");
                
                if(!item.status.equals("R")) {
	                if(dups.containsKey(item.licensePlate.get())) { // && !duplicateLPs.contains(item.getLicensePlate())) {
	                    duplicateLPs.add(item.licensePlate.get());
	                } else {
	                    dups.put(item.licensePlate.get(), "");
	                }   
                }
                
                item.availStandardLength.set(rs.getInt(21));
                item.dockArea.set((rs.getString(22) != null) ? rs.getString(22).trim() : "UNK");
                                
                allItems.add(item);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return allItems;
    }

}
