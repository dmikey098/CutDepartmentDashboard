/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dwm.dashboard.controllers;

import java.io.IOException;


import org.dwm.dashboard.QueryFunctions;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Daniel Mikesell
 */
public class UtilityTab extends VBox {
	@FXML TextField txtAsle;
    @FXML TextField txtBay;
    @FXML TextField txtLvl;
    @FXML Button btnRun;
    @FXML Label lblResult;
    
    public UtilityTab() {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UtilityTab.fxml"));
    	loader.setRoot(this);
    	loader.setController(this);
    	
    	try {
    		loader.load();
    	} catch(IOException ex) {
    		ex.printStackTrace();
    	}
    	
    	txtAsle.textProperty().addListener((obs, oldValue, newValue) -> {
    		lblResult.setText("");
    		if(newValue.length() == 3) {
    			txtBay.requestFocus();
    		}
    	});
    	
    	txtBay.textProperty().addListener((obs, oldValue, newValue) -> {
    		lblResult.setText("");
    		if(newValue.length() == 3) {
    			txtLvl.requestFocus();
    		}
    	});
    	
    	txtLvl.textProperty().addListener((obs, oldValue, newValue) -> {
    		lblResult.setText("");
    		if(newValue.length() == 3) {
    			btnRun.requestFocus();
    		}
    	});
    	
    	
    	
    	btnRun.setOnAction((e) -> run());
    	btnRun.setOnKeyPressed((e) -> {
    		run();
    		if(e.getCode().equals(KeyCode.ENTER)) {
    			txtAsle.requestFocus();
    		}
    	});
    }   
    
    private void run() {
    	String asl = txtAsle.getText();
		String bay = txtBay.getText();
		String lvl = txtLvl.getText();
		
		if(!asl.isEmpty() && !bay.isEmpty() && !lvl.isEmpty()) {
			try {
				lblResult.setText(QueryFunctions.getCheckDigit(asl, bay, lvl));
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
    }
    
    
}