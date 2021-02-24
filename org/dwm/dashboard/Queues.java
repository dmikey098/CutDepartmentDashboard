/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dwm.dashboard;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Predicate;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

/**
 *
 * @author Daniel Mikesell
 */
public class Queues {
    public static final int CUT = 0;
    public static final int SHUTTLE = 1;
    public static final int REPLEN = 2;
    public static final int ALL = 3;
    
    static ObservableList<QueueItem> allCuts = FXCollections.observableArrayList();
    static CustomFilteredList filteredCuts = new CustomFilteredList(allCuts);
    static ObservableList<QueueItem> allShuttles = FXCollections.observableArrayList();
    static CustomFilteredList filteredShuttles = new CustomFilteredList(allShuttles);
    static ObservableList<QueueItem> allReplens = FXCollections.observableArrayList();
    static CustomFilteredList filteredReplens = new CustomFilteredList(allReplens);
    
    private static CutQueue cutQueue = new CutQueue();
    private static ShuttleQueue shuttleQueue  = new ShuttleQueue();
    private static ReplenQueue replenQueue = new ReplenQueue();
    
    public static IntegerProperty cutCount = new SimpleIntegerProperty(1); 
    public static IntegerProperty spCount = new SimpleIntegerProperty(1); 
    public static IntegerProperty bwCountProperty = new SimpleIntegerProperty(1);
    public static IntegerProperty cordCountProperty = new SimpleIntegerProperty(1);
    public static IntegerProperty fiberCountProperty = new SimpleIntegerProperty(1);
    public static IntegerProperty pvCountProperty = new SimpleIntegerProperty(1);
    public static IntegerProperty cutsOver2500 = new SimpleIntegerProperty(1);
    public static IntegerProperty cutsOver5000 = new SimpleIntegerProperty(1);
    
    public static IntegerProperty totalProperty = new SimpleIntegerProperty(1);
    public static IntegerProperty reelCountProperty = new SimpleIntegerProperty();
    public static Map<Object, Long> qtyOfCutsOnOrder = null;
    
    public static Predicate<QueueItem> completedCuts = (item -> item.status.get().equals("R"));
    
    static {
        totalProperty.bind(Bindings.size(filteredCuts.list));
        cutCount.bind(Bindings.size(filteredCuts.filtered(row -> row.onHandQuantity.get() - row.orderQuantity.get() > AppManager.getStraightPullQuantity())));
        
        spCount.bind(Bindings.size(filteredCuts.filtered(row -> row.onHandQuantity.get() - row.orderQuantity.get() <= AppManager.getStraightPullQuantity())));
        bwCountProperty.bind(typeBinding("BW"));
        cordCountProperty.bind(typeBinding("CORD"));
        fiberCountProperty.bind(typeBinding("FIBER"));
        pvCountProperty.bind(typeBinding("PV"));
        cutCount.addListener((obs, oldValue, newValue) -> {
            ArrayList<String> list = new ArrayList<>();
            FilteredList<QueueItem> cutList = filteredCuts.filtered(row -> row.onHandQuantity.get() - row.orderQuantity.get() > AppManager.getStraightPullQuantity());
            
            for(QueueItem item : cutList) {
                if(!list.contains(item.licensePlate.get()))
                    list.add(item.licensePlate.get());
            }
            
            reelCountProperty.set(list.size());
        });
        
        cutsOver2500.bind(Bindings.size(filteredCuts.filtered(row -> row.orderQuantity.get() >= 2500 && row.orderQuantity.get() < 5000)));
        cutsOver5000.bind(Bindings.size(filteredCuts.filtered(row -> row.orderQuantity.get() >= 5000)));
    }
        
    public static IntegerBinding countBinding(int intVal, Predicate<QueueItem> pred) {
        IntegerBinding binding = null;
        
        switch (intVal) {
            case CUT:
                binding = Bindings.size(filteredCuts.filtered(pred));
                break;
            case SHUTTLE:
                binding = Bindings.size(filteredShuttles.filtered(pred));
                break;
            case REPLEN:
                binding = Bindings.size(filteredReplens.filtered(pred));
                break;
            default:
                break;
        }
        
        return binding;
    }
    
    public static IntegerBinding typeBinding(String type) {
        return Bindings.size(filteredCuts.filtered(row -> { return row.type.get().equals(type) && (row.onHandQuantity.get() - row.orderQuantity.get() > AppManager.getStraightPullQuantity()); }));
    }
        
    public static SortedList<QueueItem> getFilteredCuts() { 
        return filteredCuts.sort();
    }
    
    public static SortedList<QueueItem> getFilteredShuttles() { 
        return filteredShuttles.sort();
    }
    
    public static SortedList<QueueItem> getFilteredReplens() {
        return filteredReplens.sort();
    }
    
    public static QueueItem lookupItemByLP(int intVal, String licensePlate) { 
        QueueItem item = null;
        
        switch (intVal) {
            case CUT:
                item = lookupItemByLP(allCuts, licensePlate);
                break;
            case SHUTTLE:
                item = lookupItemByLP(allShuttles, licensePlate);
                break;
            case REPLEN:
                item = lookupItemByLP(allReplens, licensePlate);
                break;
            default:
                break;
        }
        
        return item;
    }
    
    private static QueueItem lookupItemByLP(ObservableList<QueueItem> allItems, String licensePlate) {
        if(allItems.filtered(item -> item.licensePlate.get().equals(licensePlate)).size() != 1) {
            return null;
        } else {
            return allItems.filtered(item -> item.licensePlate.get().equals(licensePlate)).get(0);
        }
    }
    
    public static ArrayList<String> getDuplicatePlates() {
        return cutQueue.getDuplicatePlates();
    }
    
    public static void reset() {
        filteredCuts.clear();
        filteredReplens.clear();
        filterByPriorityRange(0, 99);
        filteredShuttles.clear();
        exclude(completedCuts);
    }
    
    public static void clearFilter(int intVal) {
        switch (intVal) {
            case CUT:
                filteredCuts.clear();
                break;
            case SHUTTLE:
                filteredShuttles.clear();
                break;
            case REPLEN:
                filteredReplens.clear();
                break;
            case ALL:
                filteredCuts.clear();
                filteredShuttles.clear();
                filteredReplens.clear();
                break;
            default:
                break;
        }
    }
    
    
    public static void applyFilter(int intVal, Predicate<QueueItem> pred) {
        switch (intVal) {
            case CUT:
                filteredCuts.addFilter(pred);
                break;
            case SHUTTLE:
                filteredShuttles.addFilter(pred);
                break;
            case REPLEN:
                filteredReplens.addFilter(pred);
                break;
            case ALL:
                filteredCuts.addFilter(pred);
                filteredShuttles.addFilter(pred);
                filteredReplens.addFilter(pred);
                break;
            default:
                break;
        }
    }
    
    public static void applyPersistantFilter(int intVal, Predicate<QueueItem> pred) {
        switch (intVal) {
            case CUT:
                filteredCuts.addPersistantFilter(pred);
                break;
            case SHUTTLE:
                filteredShuttles.addPersistantFilter(pred);
                break;
            case REPLEN:
                filteredReplens.addPersistantFilter(pred);
                break;
            case ALL:
                filteredCuts.addPersistantFilter(pred);
                filteredShuttles.addPersistantFilter(pred);
                filteredReplens.addPersistantFilter(pred);
                break;
            default:
                break;
        }
    }
    
    public static void clearPersistantFilter(int intVal) {
        switch (intVal) {
            case CUT:
                filteredCuts.clearPersistantFilters();
                break;
            case SHUTTLE:
                filteredShuttles.clearPersistantFilters();
                break;
            case REPLEN:
                filteredReplens.clearPersistantFilters();
                break;
            case ALL:
                filteredCuts.clearPersistantFilters();
                filteredShuttles.clearPersistantFilters();
                filteredReplens.clearPersistantFilters();
                break;
            default:
                break;
        }
    }
    
    public static void filterByPriorityRange(int min, int max) {
        filterByPriorityRange(Queues.ALL, min, max);
    }
    
    public static void filterByPriorityRange(int intVal, int min, int max) {
        Predicate<QueueItem> pred = item -> item.priority.get() >= min && item.priority.get() <= max;
        
        switch (intVal) {
            case CUT:
                filteredCuts.addFilter(pred);
                break;
            case SHUTTLE:
                filteredShuttles.addFilter(pred);
                break;
            case REPLEN:
                filteredReplens.addFilter(pred);
                break;
            case ALL:
                filteredCuts.addFilter(pred);
                filteredShuttles.addFilter(pred);
                filteredReplens.addFilter(pred);
                break;
            default:
                break;
        }
    }
    
    public static void resetFilters() {
        
    }
    
    public static QueueItem lookupReplenByLP(String lp) {
        QueueItem replen = null;
        
        try {
            replen = (allReplens.filtered(item -> item.licensePlate.get().equals(lp))).get(0);
        } catch(IndexOutOfBoundsException ex) {}
        
        return replen;
    }
    
    public static void exclude(Predicate<QueueItem> pred) {
        filteredCuts.addPersistantFilter(pred.negate());
    }
    
    public static void refresh() {
        allCuts.clear();
        allCuts.addAll(cutQueue.getAllItems());
        exclude(completedCuts);
        allShuttles.clear();
        allShuttles.addAll(shuttleQueue.getAllItems());
        allReplens.clear();
        allReplens.addAll(replenQueue.getAllItems());
    }
     
    
}
