package org.dwm.dashboard.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.dwm.dashboard.QueryFunctions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CaseQueue {
	private  ArrayList<String> duplicateCtrlNumbers = new ArrayList<>();
	private ObservableList<Pallet> pallets = FXCollections.observableArrayList();
	
	public ObservableList<Pallet> getPallets() {
		return pallets;
	}
	
    public ArrayList<String> getDuplicateCtrlNumbers() {
        return duplicateCtrlNumbers;
    }
	
    public ObservableList<DetailItem> getAllItems() {
        ObservableList<DetailItem> allItems = FXCollections.observableArrayList();
        
        try ( ResultSet rs = QueryFunctions.getAllCases()) {            
        	
        	Map<String, Pallet> map = new HashMap<>();
        	
            while(rs.next()) {
                DetailItem item = new DetailItem();
                item.priority.set(rs.getInt(1));
                item.orderNumber.set((rs.getString(2) != null) ? rs.getString(2).trim() : "");
                item.partNumber.set((rs.getString(3) != null) ? rs.getString(3).trim() : "");
                item.orderQuantity.set(rs.getInt(4));
                item.onHandQuantity.set(rs.getInt(5));
                item.carrier.set((rs.getString(6) != null) ? rs.getString(6).trim() : "");
                item.wave.set(rs.getInt(7));
                item.status.set((rs.getString("QDSTS") != null) ? rs.getString("QDSTS").trim() : "");
                item.user.set((rs.getString(9) != null) ? rs.getString(9).trim() : "");
                item.line.set(rs.getInt(10) / 100);
                item.asle.set((rs.getString(11) == null) ? "" : rs.getString(11).trim()); //ASL
                item.bay.set((rs.getString(12) == null) ? "" : rs.getString(12).trim()); //BAY
                item.level.set((rs.getString(13) == null) ? "" : rs.getString(13).trim()); //LVL
                item.cableDiameter.set(rs.getDouble(14));
                item.casePack.set(rs.getInt(15));
                item.type.set((rs.getString(16)) == null ? "" : rs.getString(16).trim());
                
                item.weight.set(rs.getDouble(17));
                item.detailStatus.set((rs.getString(18) != null) ? rs.getString(18).trim() : "");
                item.controlNumber.set((rs.getString(19) != null) ? rs.getString(19).trim() : "");
                item.palletQuantity.set(rs.getInt(20));
                
                item.reel.set((rs.getString(21) != null) ? rs.getString(21).trim() : "");
                item.customer.set((rs.getString(22) != null) ? rs.getString(22).trim() : "");
                
                //item.reel.set(Utility.getReelSize(item, true));
                
                
                if(map.containsKey(item.getControlNumber())) {
                	map.get(item.getControlNumber()).addItemToPallet(item);
                	item.parent = map.get(item.getControlNumber());
                } else {
                	Pallet pallet = new Pallet();
            		pallet.setControlNumber(item.getControlNumber());
            		pallet.setOrderNumber(item.getOrderNumber());
            		pallet.setPriority(item.getPriority());
            		pallet.setUser(item.user.get());
            		pallet.setStatus((rs.getString(8) != null) ? rs.getString(8).trim() : "");
            		
            		pallet.addItemToPallet(item);
            		pallets.add(pallet);
            		map.put(item.getControlNumber(), pallet);
            		item.parent = pallet;
                }
            	
                
                allItems.add(item);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return allItems;
    }
}
