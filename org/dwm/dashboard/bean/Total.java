package org.dwm.dashboard.bean;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Total {
	private StringProperty user = new SimpleStringProperty("");
	private StringProperty team = new SimpleStringProperty("");
	private IntegerProperty total = new SimpleIntegerProperty(0);
	
	public Total(String user, String team, int total) {
		this.user.set(user);
		this.team.set(team);
		this.total.set(total);
	}
	
	public Total() {}
	
	public void setUser(String user) {
		this.user.set(user);
	}
	
	public void setTeam(String team) {
		this.team.set(team);
	}
	
	public void setTotal(int total) {
		this.total.set(total);
	}
	
	public String getUser() {
		return user.get();
	}
	
	public String getTeam() {
		return team.get();
	}
	
	public int getTotal() {
		return total.get();
	}
	
	public StringProperty userProperty() {
		return user;
	}
	
	public StringProperty teamProperty() {
		return team;
	}
	
	public IntegerProperty totalProperty() {
		return total;
	}
	
}
