package org.dwm.dashboard.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.dwm.dashboard.QueryFunctions;
import org.dwm.dashboard.UserManager;
import org.dwm.dashboard.bean.Total;
import org.dwm.dashboard.bean.User;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;



public class UserTableController implements Initializable  {
	@FXML TableColumn<User, String> colUser;
	@FXML TableColumn<User, String> colTeam;
	@FXML TableColumn<User, String> colJob;
	@FXML TableColumn<User, String> colRF;
	@FXML TableColumn<User, String> colStartDate;
    @FXML TableColumn<User, String> colStartTime;
    @FXML TableColumn<User, String> colTransactions;
	@FXML TableView<User> tblUsers;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		tblUsers.setItems(UserManager.getUserList());
		
		colUser.setCellValueFactory(cell -> cell.getValue().nameProperty());
		colTeam.setCellValueFactory(cell -> cell.getValue().teamProperty());
		colJob.setCellValueFactory(cell -> cell.getValue().jobProperty());
		colRF.setCellValueFactory(cell -> cell.getValue().rfProperty());
		colStartDate.setCellValueFactory(cell -> cell.getValue().startDateProperty());
		colStartTime.setCellValueFactory(cell -> cell.getValue().startTimeProperty());
		colTransactions.setCellValueFactory(cell -> {
			Total t = QueryFunctions.getTotalForUserAndTeam(cell.getValue().nameProperty().get(), cell.getValue().teamProperty().get());
			
			return new SimpleStringProperty(Integer.toString(t.getTotal()));
		});
		
	}
}
