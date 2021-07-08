package org.dwm.dashboard.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;

public class CasePickPreview {
	@FXML TableView tblQueue;
	
	
	public CasePickPreview() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CasePickPreview.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		
		try {
			loader.load();
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		
		
		
		
		
	}
	
	
	
}
