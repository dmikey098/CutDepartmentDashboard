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
public class PalletQueue {

    public ObservableList<QueueItem> getAllItems() {
        ObservableList<QueueItem> allItems = FXCollections.observableArrayList();
        
        try ( ResultSet rs = QueryFunctions.getAllPallets()) {            
            while(rs.next()) {
                QueueItem item = new QueueItem();
                item.priority.set(rs.getInt(1));
                item.orderNumber.set((rs.getString(2) != null) ? rs.getString(2).trim() : "");
                item.partNumber.set((rs.getString(3) != null) ? rs.getString(3).trim() : "");
                item.orderQuantity.set(rs.getInt(4));
                item.onHandQuantity.set(rs.getInt(5));
                item.carrier.set((rs.getString(6) != null) ? rs.getString(6).trim() : "");
                item.wave.set(rs.getInt(7));
                item.status.set((rs.getString(8) != null) ? rs.getString(8).trim() : "");
                item.user.set((rs.getString(9) != null) ? rs.getString(9).trim() : "");
                item.line.set(rs.getInt(10) / 100);
                item.asle.set((rs.getString(11) == null) ? "" : rs.getString(11).trim()); //ASL
                item.bay.set((rs.getString(12) == null) ? "" : rs.getString(12).trim()); //BAY
                item.level.set((rs.getString(13) == null) ? "" : rs.getString(13).trim()); //LVL
                item.cableDiameter.set(rs.getDouble(14));
                item.casePack.set(rs.getInt(15));
                item.type.set((rs.getString(16)) == null ? "" : rs.getString(16).trim());
                item.weight.set(rs.getDouble(17));
                item.controlNumber.set((rs.getString(18)) == null ? "" : rs.getString(18).trim());
                item.palletQuantity.set(rs.getInt(19));
                item.reel.set((rs.getString(20)) == null ? "" : rs.getString(20).trim());
                item.customer.set((rs.getString(21)) == null ? "" : rs.getString(21).trim());
                //item.reel.set(Utility.getReelSize(item, true));
                
                allItems.add(item);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return allItems;
    }
    
}
