/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dwm.dashboard.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import org.dwm.dashboard.AppManager;
import org.dwm.dashboard.QueueCounts;
import org.dwm.dashboard.bean.QueueItemCount;

/**
 *
 * @author Daniel Mikesell
 */
public class QueueCountTableController implements Initializable {
    @FXML TableView<QueueItemCount> tblQueue;
    @FXML TableColumn<QueueItemCount, Integer> colPriority;
    @FXML TableColumn<QueueItemCount, String> colOrderNumber;
    @FXML TableColumn<QueueItemCount, Boolean> colHasCompletedCuts;
    @FXML TableColumn<QueueItemCount, Integer> colCount;
    
	@Override
    public void initialize(URL url, ResourceBundle rb) {
        QueueCounts.getFilteredOrderCounts().comparatorProperty().bind(tblQueue.comparatorProperty());
        tblQueue.setItems(QueueCounts.getFilteredOrderCounts());
        
        colPriority.setCellValueFactory(cellData -> cellData.getValue().priorityProperty().asObject());
        colOrderNumber.setCellValueFactory(cellData -> cellData.getValue().orderNumberProperty());
        colHasCompletedCuts.setCellValueFactory(cellData -> cellData.getValue().completedCutsProperty().asObject());
        colCount.setCellValueFactory(cellData -> cellData.getValue().countProperty().asObject());
        
        
    }    
    
    @FXML private void showQueue() {
    	AppManager.showQueue.set(true);
    }
}
