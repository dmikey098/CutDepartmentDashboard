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
 * FXML Controller class
 *
 * @author Daniel Mikesell
 */
public class LegacyShuttleTableController extends TableController implements Initializable {
	@FXML private TableColumn<QueueItem, String> colWLU;
	@FXML private MenuItem mitmExclude;
    @FXML private MenuItem mitmInclude;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @SuppressWarnings({ "rawtypes" })
	@Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        QueueManager.getFilteredShuttles().comparatorProperty().bind(tblQueue.comparatorProperty());
        tblQueue.setItems(QueueManager.getFilteredShuttles());
        
        
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
        mitmExclude.setOnAction(e -> {
            excludeSelected();
        });
        
        mitmInclude.setOnAction(e -> {
            includeAll();
        });
    }    
    
    @FXML 
    private void excludeSelected() {
        QueueManager.exclude(QueueManager.SHUTTLE, getPredicate());
    }
    
    @FXML
    private void includeAll() {
        QueueManager.clearPersistantFilter(QueueManager.SHUTTLE);
    }
    
    @Override
    void applyCellFilter() {
        Predicate<QueueItem> pred = getPredicate();
        if(pred != null) {
            QueueManager.applyFilter(QueueManager.SHUTTLE, pred);
        }
    }

    @Override
    void secondaryMouseFunction() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
