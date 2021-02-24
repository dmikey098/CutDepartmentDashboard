/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dwm.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Daniel Mikesell
 */
public class PalletQueue implements Queue {

    @Override
    public ObservableList<QueueItem> getAllItems() {
        ObservableList<QueueItem> allItems = FXCollections.observableArrayList();
        
        try ( ResultSet rs = QueryFunctions.getAllPallets()) {            
            while(rs.next()) {
                QueueItem item = new QueueItem();
                item.priority.set(rs.getInt(1));
                item.orderNumber.set((rs.getString(2) != null) ? rs.getString(2).trim() : "");
                item.partNumber.set((rs.getString(3) != null) ? rs.getString(3).trim() : "");
                item.orderQuantity.set(rs.getInt(4));
                item.carrier.set((rs.getString(5) != null) ? rs.getString(5).trim() : "");
                item.wave.set(rs.getInt(6));
                item.status.set((rs.getString(7) != null) ? rs.getString(7).trim() : "");
                item.user.set((rs.getString(8) != null) ? rs.getString(8).trim() : "");
                item.line.set(rs.getInt(9) / 100);
                item.asle.set((rs.getString(10) == null) ? "" : rs.getString(10).trim()); //ASL
                item.bay.set((rs.getString(11) == null) ? "" : rs.getString(11).trim()); //BAY
                item.level.set((rs.getString(12) == null) ? "" : rs.getString(12).trim()); //LVL
                item.cableDiameter.set(rs.getDouble(13));
                item.casePack.set(rs.getInt(14));
                item.type.set((rs.getString(15)) == null ? "" : rs.getString(15).trim());
                item.reel.set(Utility.getReelSize(item, true));
                allItems.add(item);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return allItems;
    }
    
}
