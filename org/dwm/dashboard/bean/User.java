package org.dwm.dashboard.bean;

import org.dwm.dashboard.Utility;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
	private StringProperty name = new SimpleStringProperty();
	private StringProperty team = new SimpleStringProperty();
	private StringProperty job = new SimpleStringProperty();
	private StringProperty rf = new SimpleStringProperty();
	private StringProperty startDate = new SimpleStringProperty();
	private StringProperty startTime = new SimpleStringProperty();
	private IntegerProperty transactions = new SimpleIntegerProperty();
	
	
	public User() {}
	
	public User(String name, String team, String job, String startDate, String startTime, int transactions) {
		this.name.set(name);
		this.team.set(team);
		this.job.set(job);
		this.rf.set(Utility.getRFFromSerialNo(job));
		this.startDate.set(startDate);
		this.startTime.set(startTime);
		this.transactions.set(transactions);
	}
	
	public StringProperty nameProperty() {
		return name;
	}
	
	public StringProperty teamProperty() {
		return team;
	}
	
	public StringProperty jobProperty() {
		return job;
	}
	
	public StringProperty rfProperty() {
		return rf;
	}
	
	public StringProperty startDateProperty() {
		return startDate;
	}
	
	public StringProperty startTimeProperty() {
		return startTime;
	}
	
	public IntegerProperty transactionProperty() {
		return transactions;
	}
	
	
}
