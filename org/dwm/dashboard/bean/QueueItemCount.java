package org.dwm.dashboard.bean;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class QueueItemCount {
	IntegerProperty priority = new SimpleIntegerProperty(0);
	StringProperty orderNumber = new SimpleStringProperty("");
	StringProperty status = new SimpleStringProperty("");
	IntegerProperty count = new SimpleIntegerProperty(0);
	BooleanProperty cut = new SimpleBooleanProperty(false);
	BooleanProperty complete = new SimpleBooleanProperty(true);
	
	
	public int getPriority() {
		return priority.get();
	}
	public void setPriority(int priority) {
		this.priority.set(priority);
	}
	public String getOrderNumber() {
		return orderNumber.get();
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber.set(orderNumber);
	}
	public String getStatus() {
		return status.get();
	}
	public void setStatus(String status) {
		this.status.set(status);
	}
	public int getCount() {
		return count.get();
	}
	public void setCount(int count) {
		this.count.set(count);
	}
	
	public boolean hasCompletedCuts() {
		return cut.get();
	}
	
	public void setCompletedCuts(boolean b) {
		this.cut.set(b);
	}
	
	public boolean isComplete() {
		return complete.get();
	}
	
	public void setComplete(boolean b) {
		this.complete.set(b);
	}
	
	public String toString() {
		String str = "";
		str += "Pty: " + this.priority.get();
		str += "Order: " + this.orderNumber.get();
		str += "Count: " + this.count.get();
		
		return str;
	}
	
	public IntegerProperty priorityProperty() {
		return priority;
	}
	
	public StringProperty orderNumberProperty() {
		return orderNumber;
	}
	
	public BooleanProperty completedCutsProperty() {
		return cut;
	}
	
	public IntegerProperty countProperty() {
		return count;
	}
	
}
