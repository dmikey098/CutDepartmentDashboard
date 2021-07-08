/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dwm.dashboard;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.dwm.dashboard.bean.CaseQueue;
import org.dwm.dashboard.bean.DetailItem;
import org.dwm.dashboard.bean.Pallet;
import org.dwm.dashboard.bean.QueueItem;
import org.dwm.dashboard.alt.CustomFilteredList;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
public class PickQueues {
	public static final int ALL = 0;
	public static final int CASE = 1;
    public static final int PALLET = 2;
    
    private static final int DEFAULT_MIN_PRIORITY = 0;
    private static final int DEFAULT_MAX_PRIORITY = 999;
    
    //static ObservableList<QueueItem> allPalletPicks = FXCollections.observableArrayList();
    //static CustomFilteredList filteredPalletPicks = new CustomFilteredList(allPalletPicks);
    
    static ObservableList<Pallet> allCasePallets = FXCollections.observableArrayList();
    
    
    static ObservableList<DetailItem> allCasePicks = FXCollections.observableArrayList();
    static CustomFilteredList filteredCasePicks = new CustomFilteredList(allCasePicks);
    
    private static PalletQueue palletQueue = new PalletQueue();
    private static CaseQueue caseQueue = new CaseQueue();
    
    @SuppressWarnings("unused")
	private static int minPriority = DEFAULT_MIN_PRIORITY;
    @SuppressWarnings("unused")
	private static int maxPriority = DEFAULT_MAX_PRIORITY;
    private static int currentMinPriority = DEFAULT_MIN_PRIORITY;
    private static int currentMaxPriority = DEFAULT_MAX_PRIORITY;
    
    public static IntegerProperty caseCount = new SimpleIntegerProperty();
    public static IntegerProperty heldCaseCount = new SimpleIntegerProperty();
    public static IntegerProperty bwCaseCount = new SimpleIntegerProperty();
    public static IntegerProperty bwPalletCount = new SimpleIntegerProperty();
    
    public static IntegerProperty cordCaseCount = new SimpleIntegerProperty();
    public static IntegerProperty cordPalletCount = new SimpleIntegerProperty();
    
    public static IntegerProperty cordsetCaseCount = new SimpleIntegerProperty();
    public static IntegerProperty cordsetPalletCount = new SimpleIntegerProperty();
    
    public static IntegerProperty dcmslrCaseCount = new SimpleIntegerProperty();
    public static IntegerProperty dcmslrPalletCount = new SimpleIntegerProperty();
    
    public static IntegerProperty elecCaseCount = new SimpleIntegerProperty();
    public static IntegerProperty elecPalletCount = new SimpleIntegerProperty();
    
    public static IntegerProperty eleslrCaseCount = new SimpleIntegerProperty();
    public static IntegerProperty eleslrPalletCount = new SimpleIntegerProperty();
    
    public static IntegerProperty ovdslrCaseCount = new SimpleIntegerProperty();
    public static IntegerProperty ovdslrPalletCount = new SimpleIntegerProperty();
    
    public static IntegerProperty dcomCaseCount = new SimpleIntegerProperty();
    public static IntegerProperty dcomPalletCount = new SimpleIntegerProperty();
    
    public static IntegerProperty unkCaseCount = new SimpleIntegerProperty();
    public static IntegerProperty unkPalletCount = new SimpleIntegerProperty();
    
    public static IntegerProperty heldPalletCount = new SimpleIntegerProperty();
    
    public static IntegerProperty palletCount = new SimpleIntegerProperty();
    
    private static BooleanProperty heldCases = new SimpleBooleanProperty(false);
    private static BooleanProperty heldPallets = new SimpleBooleanProperty(false);
    
    public static Map<String, String> palletsToCases = new HashMap<>();
    
    
    private static final Predicate<DetailItem> HELD_PRED = item -> !item.status.get().equals("H");
    
    public static ScheduledService<Void> service = new ScheduledService<Void>() {
    	@Override
		protected QueueRefreshTask createTask() {
    		return new QueueRefreshTask();
		}
	};
	
	public static ObservableList<Pallet> getCasePallets() {
		return allCasePallets;
	}
	
	public static ArrayList<String> getCaseCtrlNumbers() {
		return caseQueue.getDuplicateCtrlNumbers();
	}
    
	public static BooleanProperty showHeldCasesProperty() {
		return heldCases;
	}
	
	public static BooleanProperty showHeldPalletsProperty() {
		return heldPallets;
	}
	
	public static FilteredList<Pallet> getActiveCasePallets() {
		return allCasePallets.filtered(pred -> {
			if(pred.getStatus().equals("A") || pred.getStatus().equals("X") || pred.getStatus().equals("W")) {
				return true;
			}
			
			return false;
		});
	}
	
    static {
    	filteredCasePicks.list.addListener(new ListChangeListener<DetailItem>() {
			@SuppressWarnings("rawtypes")
			@Override
			public void onChanged(Change c) {
				setTotals();
				
			}
				
		});
    	
    	/*
    	AppManager.includeUPSInTotals.addListener((obs, oldValue, newValue) -> {
    		System.out.println(newValue);
    		setTotals();
    	});
    	*/
    	/*
    	filteredPalletPicks.list.addListener(new ListChangeListener<QueueItem>() {
			@SuppressWarnings("rawtypes")
			@Override
			public void onChanged(Change c) {
				
				
			}
		});
    	*/
    	
    	//heldPalletCount.bind(Bindings.size(filteredPalletPicks.filtered(item -> item.status.get().equals("H"))));
    	//palletCount.bind(Bindings.size(filteredPalletPicks.list));
    	
    	
    	
        applyPersistantFilter(ALL, HELD_PRED);
        
        
        /*
        heldPallets.addListener((obs, oldValue, newValue) -> {
        	if(newValue.booleanValue()) {
        		filteredPalletPicks.removePersistantFilter(HELD_PRED);
        	} else {
        		filteredPalletPicks.addPersistantFilter(HELD_PRED);
        	}
        });
        */
        
        heldCases.addListener((obs, oldValue, newValue) -> {
        	if(newValue.booleanValue()) {
        		filteredCasePicks.removePersistantFilter(HELD_PRED);
        	} else {
        		filteredCasePicks.addPersistantFilter(HELD_PRED);
        	}
        });
    	
    	
    	service.setPeriod(Duration.minutes(30));
        service.start();
    }
    
    private static void setTotals() {
    	caseCount.set((int) filteredCasePicks.list.stream().filter(item -> !item.isUPS() || AppManager.includeUPSInTotals.get()).collect(Collectors.summarizingInt(DetailItem::getOrderQuantity)).getSum());
		heldCaseCount.set((int) filteredCasePicks.list.stream().filter(item -> item.status.get().equals("H")).collect(Collectors.summarizingInt(DetailItem::getOrderQuantity)).getSum());
		bwCaseCount.set((int) filteredCasePicks.filtered(item -> item.type.get().equals("BW") && (!item.isUPS() || AppManager.includeUPSInTotals.get())).stream().collect(Collectors.summarizingInt(DetailItem::getOrderQuantity)).getSum());
		
		cordCaseCount.set((int) filteredCasePicks.filtered(item -> item.type.get().equals("CORD") && (!item.isUPS() || AppManager.includeUPSInTotals.get())).stream().collect(Collectors.summarizingInt(DetailItem::getOrderQuantity)).getSum());
		cordsetCaseCount.set((int) filteredCasePicks.filtered(item -> item.type.get().equals("CORDSET") && (!item.isUPS() || AppManager.includeUPSInTotals.get())).stream().collect(Collectors.summarizingInt(DetailItem::getOrderQuantity)).getSum());
		dcmslrCaseCount.set((int) filteredCasePicks.filtered(item -> item.type.get().equals("DCMSLR") && (!item.isUPS() || AppManager.includeUPSInTotals.get())).stream().collect(Collectors.summarizingInt(DetailItem::getOrderQuantity)).getSum());
		elecCaseCount.set((int) filteredCasePicks.filtered(item -> item.type.get().equals("ELEC") && (!item.isUPS() || AppManager.includeUPSInTotals.get())).stream().collect(Collectors.summarizingInt(DetailItem::getOrderQuantity)).getSum());
		ovdslrCaseCount.set((int) filteredCasePicks.filtered(item -> item.type.get().equals("OVDSLR") && (!item.isUPS() || AppManager.includeUPSInTotals.get())).stream().collect(Collectors.summarizingInt(DetailItem::getOrderQuantity)).getSum());
		dcomCaseCount.set((int) filteredCasePicks.filtered(item -> item.type.get().equals("DCOM") && (!item.isUPS() || AppManager.includeUPSInTotals.get())).stream().collect(Collectors.summarizingInt(DetailItem::getOrderQuantity)).getSum());
		eleslrCaseCount.set((int) filteredCasePicks.filtered(item -> item.type.get().equals("ELESLR") && (!item.isUPS() || AppManager.includeUPSInTotals.get())).stream().collect(Collectors.summarizingInt(DetailItem::getOrderQuantity)).getSum());
		unkCaseCount.set((int) filteredCasePicks.filtered(item -> {
			if((item.type.get().equals("") && (!item.isUPS() || AppManager.includeUPSInTotals.get()))) {
				return true;
			}
			
			if((item.type.get().contains("UNK") && (!item.isUPS() || AppManager.includeUPSInTotals.get()))) {
				return true;
			}
			
			if((item.type.get().contains("BULK") && (!item.isUPS() || AppManager.includeUPSInTotals.get()))) {
				return true;	
			} 
			
			return false;
		}).stream().collect(Collectors.summarizingInt(DetailItem::getOrderQuantity)).getSum());
		
		//Map<String, Map<String, List<DetailItem>>> map = filteredCasePicks.list.stream().collect(Collectors.groupingBy(DetailItem::getType, Collectors.groupingBy(DetailItem::getControlNumber)));
		
		int bw = 0;
		int cord = 0;
		int cordset = 0;
		int dcmslr = 0;
		int elec = 0;
		int ovdslr = 0;
		int dcom = 0;
		int eleslr = 0;
		
		
				
		for(Pallet pallet : getActiveCasePallets()) {
			
			
			if(pallet.getFirstItem().getType().equals("BW")) {
				bw++;
			} else if(pallet.getFirstItem().getType().equals("CORD")) {
				cord++;
			} else if(pallet.getFirstItem().getType().equals("CORDSET")) {
				cordset++;
			} else if(pallet.getFirstItem().getType().equals("DCMSLR")) {
				dcmslr++;
			} else if(pallet.getFirstItem().getType().equals("ELEC")) {
				elec++;
			} else if(pallet.getFirstItem().getType().equals("OVDSLR")) {
				ovdslr++;
			} else if(pallet.getFirstItem().getType().equals("DCOM")) {
				dcom++;
			} else if(pallet.getFirstItem().getType().equals("ELESLR")) {
				eleslr++;
			}
		}
		
		bwPalletCount.set(bw);
		cordPalletCount.set(cord);
		cordsetPalletCount.set(cordset);
		dcmslrPalletCount.set(dcmslr);
		elecPalletCount.set(elec);
		ovdslrPalletCount.set(ovdslr);
		dcomPalletCount.set(dcom);
		eleslrPalletCount.set(eleslr);
		
		
		unkPalletCount.set(0);
    }
    
    public static void showHeldCases(boolean b) {
    	heldCases.set(b);
    }
    
    public static void showHeldCases() {
    	showHeldCases(!heldCases.get());
    }
    
    public static void showHeldPallets(boolean b) {
    	heldPallets.set(b);
    }
    
    public static void showHeldPallets() {
    	showHeldPallets(!heldPallets.get());
    }
        
    public static IntegerBinding countBinding(int intVal, Predicate<DetailItem> pred) {
        IntegerBinding binding = null;
        
        switch (intVal) {
            case CASE:
                binding = Bindings.size(filteredCasePicks.filtered(pred));
                break;
            case PALLET:
                //binding = Bindings.size(filteredPalletPicks.filtered(pred));
                break;
            default:
                break;
        }
        
        return binding;
    }
    
    public static DetailItem getCasePickByLocation(String asl, String bay, String lvl) {
    	FilteredList<DetailItem> list = allCasePicks.filtered(item -> {
    		if(item.asle.get().equals(asl) && item.bay.get().equals(bay) && item.level.get().equals(lvl)) {
    			return true;
    		}
    		
    		return false;
    	}); 
    	
    	if(!list.isEmpty()) {
    		return list.get(0);
    	}
    	
    	return null; 
    }
    
    public static void filterCases() {
    	Map<String, Long> map = allCasePicks.stream().collect(Collectors.groupingBy(DetailItem::getControlNumber, Collectors.counting()));
    	
    	Predicate<DetailItem> pred = (item -> {
    		if(map.containsKey(item.controlNumber.get())) {
    			if(map.get(item.controlNumber.get()) < 4) {
    				return true;
    			}
    		} 
    		return false;
    	});
    	
    	filteredCasePicks.addFilter(pred);
    	
    	Map<String, Integer> map2 = allCasePicks.stream().collect(Collectors.groupingBy(DetailItem::getControlNumber, Collectors.summingInt(DetailItem::getOrderQuantity)));
    	
    	
    	
    	pred = (item -> {
    		if(map2.containsKey(item.controlNumber.get())) {
    			if(map2.get(item.controlNumber.get()) >= 20) {
    				return true;
    			}
    		} 
    		return false;
    	});
    	
    	filteredCasePicks.addFilter(pred);
    }
    
    public static IntegerBinding caseTypeBinding(String type) {
        return Bindings.size(filteredCasePicks.filtered(row -> { return row.type.get().equals(type); }));
    }

    /*
    public static SortedList<DetailItem> getFilteredPalletPicks() { 
        //return filteredPalletPicks.sort();
    }
    */
    
    public static SortedList<DetailItem> getFilteredCasePicks() { 
        return filteredCasePicks.sort();
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
    
    
    public static void reset() {
        //filteredPalletPicks.clear();
        filteredCasePicks.clear();
        //filterByPriorityRange(minPriority, maxPriority);
        
        //Predicate<QueueItem> pred = item -> !item.status.get().equals("H");
        //applyPersistantFilter(CASE, pred);
    }
    
    
    
    public static void clearFilter(int intVal) {
    	switch (intVal) {
            case CASE:
                filteredCasePicks.clear();
                break;
            case PALLET:
                //filteredPalletPicks.clear();
                break;
            case ALL:
            	filteredCasePicks.clear();
            	//filteredPalletPicks.clear();
                break;
            default:
                break;
        }
    }
    
    
    public static void applyFilter(int intVal, Predicate<DetailItem> pred) {
        switch (intVal) {
            case CASE:
            	filteredCasePicks.addFilter(pred);
                break;
            case PALLET:
            	//filteredPalletPicks.addFilter(pred);
                break;
            case ALL:
            	filteredCasePicks.addFilter(pred);
            	//filteredPalletPicks.addFilter(pred);
                break;
            default:
                break;
        }
    }
    
    public static void applyFilter(int intVal, Predicate<DetailItem> pred, boolean clearFirst) {
    	clearFilter(intVal);
        applyFilter(intVal, pred);
    }
    
    public static void applyPersistantFilter(int intVal, Predicate<DetailItem> pred) {
    	switch (intVal) {
            case CASE:
            	filteredCasePicks.addPersistantFilter(pred);
                break;
            case PALLET:
            	//filteredPalletPicks.addPersistantFilter(pred);
                break;
            case ALL:
            	filteredCasePicks.addPersistantFilter(pred);
            	//filteredPalletPicks.addPersistantFilter(pred);
                break;
            default:
                break;
        }
    }
    
    public static void clearPersistantFilter(int intVal, Predicate<DetailItem> pred) {
        switch (intVal) {
            case CASE:
            	filteredCasePicks.removePersistantFilter(pred);
                break;
            case PALLET:
            	//filteredPalletPicks.removePersistantFilter(pred);
                break;
            case ALL:
            	filteredCasePicks.removePersistantFilter(pred);
            	//filteredPalletPicks.removePersistantFilter(pred);
                break;
            default:
                break;
        }
    }
    
    public static void clearPersistantFilter(int intVal) {
        switch (intVal) {
            case CASE:
            	filteredCasePicks.clearPersistantFilters();
                break;
            case PALLET:
            	//filteredPalletPicks.clearPersistantFilters();
                break;
            case ALL:
            	filteredCasePicks.clearPersistantFilters();
            	//filteredPalletPicks.clearPersistantFilters();
                break;
            default:
                break;
        }
    }
    
    public static void filterByPriorityRange(int min, int max) {
        filterByPriorityRange(PickQueues.ALL, min, max);
        currentMinPriority = min;
        currentMaxPriority = max;
    }
    
    public static void filterByPriorityRange(int intVal, int min, int max) {
        Predicate<DetailItem> pred = item -> item.priority.get() >= min && item.priority.get() <= max;
        
        switch (intVal) {
            case CASE:
            	filteredCasePicks.addFilter(pred);
                break;
            case PALLET:
            	//filteredPalletPicks.addFilter(pred);
                break;
            case ALL:
            	filteredCasePicks.addFilter(pred);
            	//filteredPalletPicks.addFilter(pred);
                break;
            default:
                break;
        }
    }
    
    public static void resetFilters() {
        
    }
        
    
    public static void exclude(Predicate<QueueItem> pred) {
        //filteredCase.addPersistantFilter(pred.negate());
    }
    
    public static void exclude(int intVal, Predicate<QueueItem> pred) {
    	/*
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
	    */
    }
     
    
    
    
    public static void refresh() {
    	service.restart();
    }
     
    
    
    static class QueueRefreshTask extends Task<Void> {
    	//ObservableList<DetailItem> pallets = FXCollections.observableArrayList();
    	ObservableList<DetailItem> cases = FXCollections.observableArrayList();
    	ObservableList<Pallet> casePallets = FXCollections.observableArrayList();
    	
    	protected QueueRefreshTask() {
    		AppManager.lastRefreshedProperty.set("Refreshing...");
    	}
    	
		@Override
		protected Void call() throws Exception {
			//pallets.clear();
			//pallets.addAll(palletQueue.getAllItems());
			
			cases.clear();
			cases.addAll(caseQueue.getAllItems());
			
			casePallets.clear();
			casePallets.addAll(caseQueue.getPallets());
			return null;
		}
    	
		@Override 
		protected void succeeded() {
			Platform.runLater(() -> {
				//allPalletPicks.clear(); 
	    		//allPalletPicks.addAll(this.pallets);
	    		
	    		allCasePicks.clear();
	    		allCasePicks.addAll(this.cases);
	    		allCasePallets.clear();
	    		allCasePallets.addAll(this.casePallets);
	    		
	    		System.out.println(this.casePallets.size());
	    		PickQueues.reset();
	            
	            AppManager.lastRefreshedProperty.set("Last refreshed on " + LocalDate.now() + " at " + LocalTime.now());
			});
            
            
		}
    }
}
