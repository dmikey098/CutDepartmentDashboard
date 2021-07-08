/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dwm.dashboard;



import java.time.LocalDateTime;

import org.dwm.dashboard.bean.QueueItem;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

/**
 *
 * @author Daniel Mikesell
 */
public class PickQueueTotals {
    static ObservableList<QueueItem> allPallets = FXCollections.observableArrayList();
    static CustomFilteredList filteredPallets = new CustomFilteredList(allPallets);
    
    static IntegerProperty cases = new SimpleIntegerProperty();
    static IntegerProperty pallets = new SimpleIntegerProperty();
    static IntegerProperty ups = new SimpleIntegerProperty();
    static IntegerProperty grainger = new SimpleIntegerProperty();
    
    
    static ScheduledService<Void> service = new ScheduledService<Void>() {
    	@Override
		protected PickRefreshTask createTask() {
    		return new PickRefreshTask();
		}
	};
        
    public static IntegerProperty caseProperty() { return cases; }
    public static IntegerProperty palletProperty() { return pallets; }
    public static IntegerProperty upsProperty() { return ups; }
    public static IntegerProperty graingerProperty() { return grainger; }

    public static SortedList<QueueItem> getFilteredPalletPicks() { 
        return filteredPallets.sort();
    }
    
    static {
    	service.setPeriod(Duration.minutes(30));
    	service.start();
    }
    
    
    public static void refresh() {
        //allPallets.clear();
        //allPallets.addAll(palletQueue.getAllItems());
        service.restart();
    }
    
    
    
    
    static class PickRefreshTask extends Task<Void> {
    	private IntegerProperty cases = new SimpleIntegerProperty();
        private IntegerProperty pallets = new SimpleIntegerProperty();
        private IntegerProperty ups = new SimpleIntegerProperty();
        private IntegerProperty grainger = new SimpleIntegerProperty();
        
        protected PickRefreshTask() {
        	System.out.println(LocalDateTime.now() + " - PickQueue");
        }
        
		@Override
		protected Void call() throws Exception {
			if(AppManager.isAccessRequired() && AppManager.accessProperty().get() <= 0) {
				this.cases.set(0);
				this.pallets.set(0);
				this.ups.set(0);
				this.grainger.set(0);
				
				return null;
			}
			
			this.cases.set(QueryFunctions.getCasePickTotal());
            this.pallets.set(QueryFunctions.getPalletPickTotal());
            this.ups.set(QueryFunctions.getUPSPickTotal());
            this.grainger.set(QueryFunctions.getGraingerPickTotal());
			return null;
		}
		
		@Override
		protected void succeeded() {
			Platform.runLater(() -> {
				PickQueueTotals.cases.set(cases.get());
				PickQueueTotals.pallets.set(pallets.get());
				PickQueueTotals.ups.set(ups.get());
				PickQueueTotals.grainger.set(grainger.get());
			});
		}
    	
    }
}
