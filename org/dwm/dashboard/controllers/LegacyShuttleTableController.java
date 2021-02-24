/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dwm.dashboard.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import org.dwm.dashboard.QueueItem;
import org.dwm.dashboard.Queues;

/**
 * FXML Controller class
 *
 * @author Daniel Mikesell
 */
public class LegacyShuttleTableController extends TableController implements Initializable {
	@FXML private TableColumn<QueueItem, String> colWLU;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        Queues.getFilteredShuttles().comparatorProperty().bind(tblQueue.comparatorProperty());
        tblQueue.setItems(Queues.getFilteredShuttles());
        
        
        colPriority.setCellValueFactory(cellData -> cellData.getValue().priority.asObject());
        colOrderNumber.setCellValueFactory(cellData -> cellData.getValue().orderNumber);
        colWLU.setCellValueFactory(cellData -> cellData.getValue().wlu);
        colPartNumber.setCellValueFactory(cellData -> cellData.getValue().partNumber);
        colOrderQuantity.setCellValueFactory(cellData -> cellData.getValue().orderQuantity.asObject());
        colWave.setCellValueFactory(cellData -> cellData.getValue().wave.asObject());
        colStatus.setCellValueFactory(cellData -> cellData.getValue().status);
        colUser.setCellValueFactory(cellData -> cellData.getValue().user);
        colCustomer.setCellValueFactory(cellData -> cellData.getValue().customer);
        colType.setCellValueFactory(cellData -> cellData.getValue().type);
        colLine.setCellValueFactory(cellData -> cellData.getValue().line.asObject());
        colReel.setCellValueFactory(cellData -> cellData.getValue().reel);
        
        
        tblQueue.getColumns().forEach(column -> ((TableColumn) column).prefWidthProperty().bind(tblQueue.widthProperty().divide(12)));
    }    
    
    @Override
    void applyCellFilter() {
        Predicate<QueueItem> pred = getPredicate();
        if(pred != null) {
            Queues.applyFilter(Queues.SHUTTLE, pred);
        }
    }

    @Override
    void secondaryMouseFunction() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
