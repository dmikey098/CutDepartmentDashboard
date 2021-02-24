/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dwm.dashboard.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.dwm.dashboard.QueryFunctions;

/**
 * FXML Controller class
 *
 * @author mikesda001
 */
public class NotesTableController implements Initializable {
    @SuppressWarnings("rawtypes")
	@FXML private TableView table;
    @FXML private TableColumn<ArrayList<String>, String> colOrderNumber;
    @FXML private TableColumn<ArrayList<String>, String> colSuffix;
    @FXML private TableColumn<ArrayList<String>, String> colLine;
    @FXML private TableColumn<ArrayList<String>, String> colNote;
    ObservableList<ArrayList<String>> tableData = FXCollections.observableArrayList();
    
    /**
     * Initializes the controller class.
     */
    @SuppressWarnings("unchecked")
	@Override
    public void initialize(URL url, ResourceBundle rb) {
        table.setItems(tableData);
        colOrderNumber.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().get(0)));
        colSuffix.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().get(1)));
        colLine.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().get(2)));
        colNote.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().get(3)));
        
    }    
    
    public void setOrderNumber(String orderNumber) {
        tableData.clear();
        tableData.addAll(QueryFunctions.getOrderNotes(orderNumber));
        
    }
}
