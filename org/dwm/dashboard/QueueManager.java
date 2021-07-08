/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dwm.dashboard;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.dwm.dashboard.bean.QueueItem;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

/**
 *
 * @author Daniel Mikesell
 */
public class QueueManager {
    public static final int CUT = 0;
    public static final int SHUTTLE = 1;
    public static final int REPLEN = 2;
    public static final int ALL = 3;
    private static final int DEFAULT_MIN_PRIORITY = 0;
    private static final int DEFAULT_MAX_PRIORITY = 99;
    
    static ObservableList<QueueItem> allCuts = FXCollections.observableArrayList();
    static CustomFilteredList filteredCuts = new CustomFilteredList(allCuts);
    static ObservableList<QueueItem> allShuttles = FXCollections.observableArrayList();
    static CustomFilteredList filteredShuttles = new CustomFilteredList(allShuttles);
    static ObservableList<QueueItem> allReplens = FXCollections.observableArrayList();
    static CustomFilteredList filteredReplens = new CustomFilteredList(allReplens);
    
    private static CutQueue cutQueue = new CutQueue();
    private static ShuttleQueue shuttleQueue  = new ShuttleQueue();
    private static ReplenQueue replenQueue = new ReplenQueue();
    
    private static int minPriority = DEFAULT_MIN_PRIORITY;
    private static int maxPriority = DEFAULT_MAX_PRIORITY;
    private static int currentMinPriority = DEFAULT_MIN_PRIORITY;
    private static int currentMaxPriority = DEFAULT_MAX_PRIORITY;
    
    public static IntegerProperty cutCount = new SimpleIntegerProperty(1); 
    public static IntegerProperty spCount = new SimpleIntegerProperty(1); 
    public static IntegerProperty bwCountProperty = new SimpleIntegerProperty(1);
    public static IntegerProperty cordCountProperty = new SimpleIntegerProperty(1);
    public static IntegerProperty fiberCountProperty = new SimpleIntegerProperty(1);
    public static IntegerProperty pvCountProperty = new SimpleIntegerProperty(1);
    public static IntegerProperty cutsOver2500 = new SimpleIntegerProperty(1);
    public static IntegerProperty cutsOver5000 = new SimpleIntegerProperty(1);
    public static IntegerProperty truckloadCuts = new SimpleIntegerProperty(1);
    
    public static IntegerProperty totalProperty = new SimpleIntegerProperty(1);
    public static IntegerProperty reelCountProperty = new SimpleIntegerProperty();
    public static Map<Object, Long> qtyOfCutsOnOrder = null;
    
    public static Predicate<QueueItem> completedCuts = (item -> item.status.get().equals("R"));
    
    
    
    
    
    public static ScheduledService<Void> service = new ScheduledService<Void>() {
    	@Override
		protected QueueRefreshTask createTask() {
    		return new QueueRefreshTask();
		}
	};
	
	
    
    /*
     static ScheduledService<ObservableList<QueueItem>> service = new ScheduledService<ObservableList<QueueItem>>() {
    	@Override
		protected Task<ObservableList<QueueItem>> createTask() {
    		return new QueueRefreshTask();
		}
	}; 
     
	 static Service<ObservableList<QueueItem>> service = new Service<ObservableList<QueueItem>>() {
    	@Override
		protected Task<ObservableList<QueueItem>> createTask() {
    		return new QueueRefreshTask();
		}
	};
	  
	  */
    
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
        //AppManager.lastRefreshedProperty.bind(service.messageProperty());
        
        service.setPeriod(Duration.minutes(30));
        service.start();
        
        
        
        
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
    
    public static void setMinPriority(int min) {
    	minPriority = min;
    }
    
    public static void setMaxPriority(int max) {
    	maxPriority = max;
    }
    
    public static void setPersistantPriorities() {
    	minPriority = currentMinPriority;
    	maxPriority = currentMaxPriority;
    }
    
    public static ArrayList<String> getDuplicatePlates() {
        return cutQueue.getDuplicatePlates();
    }
    
    public static void reset() {
        filteredCuts.clear();
        filteredReplens.clear();
        filterByPriorityRange(minPriority, maxPriority);
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
    
    public static void applyTodaysCutsFilter() {
    	Predicate<QueueItem> pred = row -> row.isForToday();
    	clearFilter(CUT);
    	applyFilter(CUT, pred);
     }
    
    public static void applyTenOrMoreFilter() {
    	Map<String, Long> map = filteredCuts.sort().stream().collect(Collectors.groupingBy(QueueItem::getOrderNumber, Collectors.counting()));
   	
        
        Predicate<QueueItem> pred = item -> {
        	if(map.containsKey(item.getOrderNumber())) {
        		if(map.get(item.getOrderNumber()) >= 5) {
        			return true;
        		}
        	}
        	
        	return false;
        };
        
    	filteredCuts.addFilter(pred);
    }
    
    
    public static void applyMultiCutFilter() {
    	Map<String, Long> map = filteredCuts.sort().stream().collect(Collectors.groupingBy(QueueItem::getLicensePlate, Collectors.counting()));
   	
        
        Predicate<QueueItem> pred = item -> {
        	if(map.containsKey(item.getLicensePlate())) {
        		if(map.get(item.getLicensePlate()) >= 5) {
        			return true;
        		}
        	}
        	
        	return false;
        };
        
    	filteredCuts.addFilter(pred);
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
    
    public static void applyFilter(int intVal, Predicate<QueueItem> pred, boolean clearFirst) {
	    if(clearFirst) {
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
    	
        applyFilter(intVal, pred);
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
        filterByPriorityRange(QueueManager.ALL, min, max);
        currentMinPriority = min;
        currentMaxPriority = max;
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
    
    public static void exclude(int intVal, Predicate<QueueItem> pred) {
    	switch (intVal) {
	        case CUT:
	            filteredCuts.addPersistantFilter(pred.negate());
	            break;
	        case SHUTTLE:
	            filteredShuttles.addPersistantFilter(pred.negate());
	            break;
	        case REPLEN:
	            filteredReplens.addPersistantFilter(pred.negate());
	            break;
	        case ALL:
	            filteredCuts.addPersistantFilter(pred.negate());
	            filteredShuttles.addPersistantFilter(pred.negate());
	            filteredReplens.addPersistantFilter(pred.negate());
	            break;
	        default:
	            break;
	    }
    }
     
    
    
    
    public static void refresh() {
    	service.restart();
    }
     
    
    
    static class QueueRefreshTask extends Task<Void> {
    	public ObservableList<QueueItem> cuts = FXCollections.observableArrayList();
    	ObservableList<QueueItem> shuttles = FXCollections.observableArrayList();
    	ObservableList<QueueItem> replens = FXCollections.observableArrayList();
    	
    	protected QueueRefreshTask() {
    		//System.out.println(LocalDateTime.now() + " - Queues");
    		AppManager.lastRefreshedProperty.set("Refreshing...");
    	}
    	
		@Override
		protected Void call() throws Exception {
			cuts.clear();
			shuttles.clear();
			replens.clear();
			
			if(AppManager.isAccessRequired() && AppManager.accessProperty().get() <= 0) {
				return null;
			}
			
			cuts.addAll(cutQueue.getAllItems());
			shuttles.addAll(shuttleQueue.getAllItems());
			replens.addAll(replenQueue.getAllItems());
			
			return null;
		}
    	
		@Override 
		protected void succeeded() {
			Platform.runLater(() -> {
				allCuts.clear(); 
	    		allCuts.addAll(this.cuts);
	    		exclude(completedCuts);
	            allShuttles.clear();
	            allShuttles.addAll(this.shuttles);
	            allReplens.clear();
	            allReplens.addAll(this.replens);
	            QueueManager.reset();
	            
	            AppManager.lastRefreshedProperty.set("Last refreshed on " + LocalDate.now() + " at " + LocalTime.now());
			});
            
            
		}
    }
}
