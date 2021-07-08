package org.dwm.dashboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.dwm.dashboard.bean.QueueItemCount;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

public class QueueCounts {
	private static ObservableList<QueueItemCount> allOrders = FXCollections.observableArrayList();
	private static FilteredList<QueueItemCount> filteredOrders = new FilteredList<>(allOrders);
	private static SortedList<QueueItemCount> sortedOrders = new SortedList<>(filteredOrders);
	
	public static ScheduledService<Void> service = new ScheduledService<Void>() {
    	@Override
		protected CountRefreshTask createTask() {
    		return new CountRefreshTask();
		}
	};
	
	public static ArrayList<String> getOrdersWithMostCuts() {
		return filteredOrders.stream().sorted(Comparator.comparing(QueueItemCount::getCount).reversed()).limit(5).map(item -> item.getOrderNumber()).collect(Collectors.toCollection(ArrayList::new));
	}
	
	public static ArrayList<String> getOrdersWithTenOrMoreCuts() {
		return filteredOrders.stream().filter(count -> count.getCount() >= 10).sorted(Comparator.comparing(QueueItemCount::getCount).reversed()).map(item -> item.getOrderNumber()).collect(Collectors.toCollection(ArrayList::new));
	}
	
	static {
		allOrders.addAll(QueryFunctions.getOrderCounts());
		filteredOrders.setPredicate(item -> !item.isComplete());
		
		//service.setPeriod(Duration.minutes(30));
        //service.start();
	}
	
	public static ObservableList<QueueItemCount> getAllOrderCounts() {
		return allOrders;
	}
	
	public static SortedList<QueueItemCount> getFilteredOrderCounts() {
		return sortedOrders;
	}
	
	public static void refresh() {
    	service.restart();
    }
    
    static class CountRefreshTask extends Task<Void> {
    	public ObservableList<QueueItemCount> counts = FXCollections.observableArrayList();
    	
		@Override
		protected Void call() throws Exception {
			counts.clear();
			counts.addAll(QueryFunctions.getOrderCounts());
			return null;
		}
    	
		@Override 
		protected void succeeded() {
			Platform.runLater(() -> {
				allOrders.clear(); 
				allOrders.addAll(this.counts);
			});
            
            
		}
    }
}
