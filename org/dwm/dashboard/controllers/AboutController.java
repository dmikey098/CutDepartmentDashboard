package org.dwm.dashboard.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.dwm.dashboard.MainApp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class AboutController implements Initializable {
	@FXML Label lblVersion;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String str = (MainApp.IS_SNAPSHOT) ? MainApp.IN_DEV_VERSION : MainApp.VERSION;
		
		lblVersion.setText(str);
	}

}
