/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dwm.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.dwm.dashboard.bean.QueueItem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 *
 * @author Daniel Mikesell
 */
public class ShuttleQueue {

    public ObservableList<QueueItem> getAllItems() {
        ObservableList<QueueItem> allItems = FXCollections.observableArrayList();
        
        try (ResultSet rs = QueryFunctions.getAllShuttles() ) {            
            while(rs.next()) {
                QueueItem item = new QueueItem();
                item.priority.set(rs.getInt(1));
                item.orderNumber.set((rs.getString(2) != null) ? rs.getString(2) : "");
                item.partNumber.set((rs.getString(3) != null) ? rs.getString(3) : "");
                item.orderQuantity.set(rs.getInt(4));
                item.wave.set(rs.getInt(5));
                item.status.set((rs.getString(6) != null) ? rs.getString(6) : "");
                item.user.set((rs.getString(7) != null) ? rs.getString(7) : "");
                item.type.set((rs.getString(8) != null) ? rs.getString(8) : "");                
                item.line.set(rs.getInt(9));                
                item.reel.set((rs.getString(10) != null) ? rs.getString(10) : "");
                item.customer.set((rs.getString(11) != null) ? rs.getString(11) : "");
                item.wlu.set((rs.getString(12) != null) ? rs.getString(12) : "");
                allItems.add(item);
            }
        } catch (SQLException ex) {}
        
        return allItems;
    }

}
