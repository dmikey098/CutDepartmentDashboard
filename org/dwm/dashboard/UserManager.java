package org.dwm.dashboard;


import org.dwm.dashboard.bean.User;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

public class UserManager {
private static ObservableList<User> allUsers = FXCollections.observableArrayList();
	
	static ScheduledService<Void> service = new ScheduledService<Void>() {
    	@Override
		protected Task<Void> createTask() {
    		return new UserRefreshTask();
		}
	};
	
	static {
		service.setPeriod(Duration.minutes(30));
		service.start();
	}
	
	public static ObservableList<User> getUserList() {
		return allUsers;
	}
	
	public static void refresh() {
		service.restart();
	}
	
	
	
	static class UserRefreshTask extends Task<Void> {
		private ObservableList<User> users = FXCollections.observableArrayList();
		
		@Override
		protected Void call() throws Exception {
			users.clear();
			users.addAll(QueryFunctions.getUserTeams());
			return null;
		}
		
		@Override 
		protected void succeeded() {
			Platform.runLater(() -> {
				allUsers.clear();
				allUsers.addAll(users);
			});
		}
	}
}
