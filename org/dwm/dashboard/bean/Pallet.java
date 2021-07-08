package org.dwm.dashboard.bean;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Pallet {
	private ObservableList<DetailItem> allItems = FXCollections.observableArrayList();
	private StringProperty controlNumber = new SimpleStringProperty("");
	private IntegerProperty priority = new SimpleIntegerProperty(0);
	private StringProperty orderNumber = new SimpleStringProperty("");
	private StringProperty user = new SimpleStringProperty("");
	private StringProperty status = new SimpleStringProperty("");
	
	public DetailItem getFirstItem() {
		return allItems.get(0);
	}
	
	public ObservableList<DetailItem> getPalletItems() {
		return allItems;
	}
	
	public void addItemToPallet(DetailItem item) {
		allItems.add(item);
	}
	
	public StringProperty controlNumberProperty() {
		return controlNumber;
	}
	
	public IntegerProperty priorityProperty() {
		return priority;
	}
	
	public StringProperty orderNumberProperty() {
		return orderNumber;
	}
	
	public StringProperty userProperty() {
		return user;
	}
	
	public StringProperty statusProperty() {
		return status;
	}
	
	public String getControlNumber() {
		return controlNumber.get();
	}
	
	public int getPriority() {
		return priority.get();
	}
	
	public String getOrderNumber() {
		return orderNumber.get();
	}
	
	public String getUser() {
		return user.get();
	}
	
	public String getStatus() {
		return status.get();
	}
	
	public void setControlNumber(String controlNumber) {
		this.controlNumber.set(controlNumber);
	}
	
	public void setPriority(int priority) {
		this.priority.set(priority);
	}
	
	public void setOrderNumber(String orderNumber) {
		this.orderNumber.set(orderNumber);
	}
	
	public void setUser(String user) {
		this.user.set(user);
	}
	
	public void setStatus(String status) {
		this.status.set(status);
	}
}
