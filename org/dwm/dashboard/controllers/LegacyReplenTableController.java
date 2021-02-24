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
 *
 * @author Daniel Mikesell
 */
public class LegacyReplenTableController extends TableController implements Initializable {
    @FXML public TableColumn<QueueItem, String> colAsle;
    @FXML public TableColumn<QueueItem, String> colBay;
    @FXML public TableColumn<QueueItem, String> colLevel;
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public void initialize(URL url, ResourceBundle rb) {
        Queues.getFilteredReplens().comparatorProperty().bind(tblQueue.comparatorProperty());
        tblQueue.setItems(Queues.getFilteredReplens());
        super.initialize(url, rb);
        
        colPriority.setCellValueFactory(cellData -> cellData.getValue().priority.asObject());
        colOrderNumber.setCellValueFactory(cellData -> cellData.getValue().orderNumber);
        colLicensePlate.setCellValueFactory(cellData -> cellData.getValue().licensePlate);
        colPartNumber.setCellValueFactory(cellData -> cellData.getValue().partNumber);
        
        colStatus.setCellValueFactory(cellData -> cellData.getValue().status);
        colUser.setCellValueFactory(cellData -> cellData.getValue().user);
        

        
        tblQueue.getColumns().forEach(column -> {
            ((TableColumn) column).prefWidthProperty().bind((tblQueue.widthProperty().subtract(360)).divide(9));           
        });
        
        colAsle.setCellValueFactory(cellData -> ((QueueItem) cellData.getValue()).asle);
        colBay.setCellValueFactory(cellData -> ((QueueItem) cellData.getValue()).bay);
        colLevel.setCellValueFactory(cellData -> ((QueueItem) cellData.getValue()).level);
        
    }    
    
    @Override
    void applyCellFilter() {
        Predicate<QueueItem> pred = getPredicate();
        if(pred != null) {
            Queues.applyFilter(Queues.REPLEN, pred);
        }
    }

    @Override
    void secondaryMouseFunction() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
