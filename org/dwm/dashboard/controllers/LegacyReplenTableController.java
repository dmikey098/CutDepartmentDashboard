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
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;

import org.dwm.dashboard.QueueManager;
import org.dwm.dashboard.bean.QueueItem;

/**
 *
 * @author Daniel Mikesell
 */
public class LegacyReplenTableController extends TableController implements Initializable {
    @FXML public TableColumn<QueueItem, String> colAsle;
    @FXML public TableColumn<QueueItem, String> colBay;
    @FXML public TableColumn<QueueItem, String> colLevel;
    @FXML private MenuItem mitmExclude;
    @FXML private MenuItem mitmInclude;
    
    @SuppressWarnings({ "rawtypes" })
	@Override
    public void initialize(URL url, ResourceBundle rb) {
        QueueManager.getFilteredReplens().comparatorProperty().bind(tblQueue.comparatorProperty());
        tblQueue.setItems(QueueManager.getFilteredReplens());
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
        
        mitmExclude.setOnAction(e -> {
            excludeSelected();
        });
        
        mitmInclude.setOnAction(e -> {
            includeAll();
        });
    }    
    
    @FXML 
    private void excludeSelected() {
        QueueManager.exclude(QueueManager.REPLEN, getPredicate());
    }
    
    @FXML
    private void includeAll() {
        QueueManager.clearPersistantFilter(QueueManager.REPLEN);
    }
    
    @Override
    void applyCellFilter() {
        Predicate<QueueItem> pred = getPredicate();
        if(pred != null) {
            QueueManager.applyFilter(QueueManager.REPLEN, pred);
        }
    }

    @Override
    void secondaryMouseFunction() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
