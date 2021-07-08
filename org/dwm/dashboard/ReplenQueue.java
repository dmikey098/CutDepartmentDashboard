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
public class ReplenQueue {

    public ObservableList<QueueItem> getAllItems() {
        ObservableList<QueueItem> allItems = FXCollections.observableArrayList();
        
        try (ResultSet rs = QueryFunctions.getAllReplens()) {
            while(rs.next()) {		
                QueueItem item = new QueueItem();
                item.priority.set(rs.getInt(1));
                item.orderNumber.set((rs.getString(2) == null) ? "" : rs.getString(2).trim());
                item.licensePlate.set((rs.getString(3) == null) ? "" : rs.getString(3).trim());
                item.partNumber.set((rs.getString(4) == null) ? "" : rs.getString(4).trim());
                item.asle.set((rs.getString(5) == null) ? "" : rs.getString(5).trim()); //ASL
                item.bay.set((rs.getString(6) == null) ? "" : rs.getString(6).trim()); //BAY
                item.level.set((rs.getString(7) == null) ? "" : rs.getString(7).trim()); //LVL
                item.status.set((rs.getString(8) == null) ? "" : rs.getString(8).trim()); //Status
                item.user.set((rs.getString(9) == null) ? "" : rs.getString(9).trim()); //User
                item.controlNumber.set((rs.getString(10) == null) ? "" : rs.getString(10).trim()); //User
                allItems.add(item);
            }
        } catch (SQLException ex) {
            
        }
        return allItems;
    }

}
