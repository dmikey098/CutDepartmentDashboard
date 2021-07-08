package org.dwm.dashboard;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

public class PNC {
	private static StringProperty pncTotal = new SimpleStringProperty();
	
	static ScheduledService<Void> service = new ScheduledService<Void>() {
    	@Override
		protected Task<Void> createTask() {
    		return new PNCRefreshTask();
		}
	};
	
	static {
    	service.setPeriod(Duration.minutes(30));
    	service.start();
    }
	
	public static StringProperty totalProperty() {
		return pncTotal;
	}
	
	public static void refresh() {
		service.restart();
	}
	
	static class PNCRefreshTask extends Task<Void> {
    	int pnc = 0;
    	
    	protected PNCRefreshTask() {
    		this.updateMessage("...");
    	}
    	
		@Override
		protected Void call() throws Exception {
			if(AppManager.isAccessRequired() && AppManager.accessProperty().get() <= 0) {
				return null;
			}
			pnc = QueryFunctions.getPNCTotal() + AppManager.PNC_OFFSET;
			return null;
		}
    	
		@Override 
		protected void succeeded() {
			pncTotal.set(Integer.toString(pnc));
		}
    }
}
