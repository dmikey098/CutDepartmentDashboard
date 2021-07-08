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
import org.dwm.dashboard.PickQueueTotals;
import org.dwm.dashboard.bean.QueueItem;

/**
 * FXML Controller class
 *
 * @author Daniel Mikesell
 */
public class PalletTableController extends TableController implements Initializable { 
    @FXML public TableColumn<QueueItem, String> colAsle;
    @FXML public TableColumn<QueueItem, String> colBay;
    @FXML public TableColumn<QueueItem, String> colLevel;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        
        //Queues.getFilteredCuts().comparatorProperty().bind(tblQueue.comparatorProperty());
        
        
        tblQueue.setItems(PickQueueTotals.getFilteredPalletPicks());
        
        
        colPriority.setCellValueFactory(cellData -> cellData.getValue().priority.asObject());
        colOrderNumber.setCellValueFactory(cellData -> cellData.getValue().orderNumber);
        colPartNumber.setCellValueFactory(cellData -> cellData.getValue().partNumber);
        colOrderQuantity.setCellValueFactory(cellData -> cellData.getValue().orderQuantity.asObject());
        colCarrier.setCellValueFactory(cellData -> cellData.getValue().carrier);
        
        
        colWave.setCellValueFactory(cellData -> cellData.getValue().wave.asObject());
        colStatus.setCellValueFactory(cellData -> cellData.getValue().status);
        colUser.setCellValueFactory(cellData -> cellData.getValue().user);
        colType.setCellValueFactory(cellData -> cellData.getValue().type);
        colLine.setCellValueFactory(cellData -> cellData.getValue().line.asObject());
        colAsle.setCellValueFactory(cellData -> cellData.getValue().asle);
        colBay.setCellValueFactory(cellData -> cellData.getValue().bay);
        colLevel.setCellValueFactory(cellData -> cellData.getValue().level);
        colReel.setCellValueFactory(cellData -> cellData.getValue().reel);
        
    }
    
    
    
    @Override
    void applyCellFilter() {
        Predicate<QueueItem> pred = getPredicate();
        if(pred != null) {
            //Queues.applyFilter(Queues.CUT, pred);
        }
    }

    @Override
    void secondaryMouseFunction() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
   
}
