/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dwm.dashboard;

import java.io.IOException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;

/**
 *
 * @author Daniel Mikesell
 */
public class PickQueue {
    static ObservableList<QueueItem> allPallets = FXCollections.observableArrayList();
    static CustomFilteredList filteredPallets = new CustomFilteredList(allPallets);
    
    
    static IntegerProperty cases = new SimpleIntegerProperty();
    static IntegerProperty pallets = new SimpleIntegerProperty();
    static IntegerProperty ups = new SimpleIntegerProperty();
    
    static PalletQueue palletQueue = new PalletQueue();
        
    public static IntegerProperty caseProperty() { return cases; }
    
    public static IntegerProperty palletProperty() { return pallets; }
    
    public static IntegerProperty upsProperty() { return ups; }

    public static SortedList<QueueItem> getFilteredPalletPicks() { 
        return filteredPallets.sort();
    }
    
    public static void refresh() {
        allPallets.clear();
        allPallets.addAll(palletQueue.getAllItems());
        
        try {
            cases.set(QueryFunctions.getCasePickTotal());
            pallets.set(QueryFunctions.getPalletPickTotal());
            ups.set(QueryFunctions.getUPSPickTotal());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
