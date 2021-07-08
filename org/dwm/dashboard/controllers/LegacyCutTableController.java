/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dwm.dashboard.controllers;


import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.dwm.dashboard.AppManager;
import org.dwm.dashboard.MainApp;
import org.dwm.dashboard.QueueManager;
import org.dwm.dashboard.Utility;
import org.dwm.dashboard.bean.QueueItem;

/**
 * FXML Controller class
 *
 * @author Daniel Mikesell
 */


public class LegacyCutTableController extends TableController implements Initializable { 
    @FXML private TableColumn<QueueItem, String> colReplenStatus;
    @FXML public ScrollPane scrollPane;
    @FXML private TextField txtSearchField;
    
    
    
    @SuppressWarnings({ "rawtypes" })
	@Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        
        tblQueue.getSelectionModel().setCellSelectionEnabled(true);
        tblQueue.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        QueueManager.getFilteredCuts().comparatorProperty().bind(tblQueue.comparatorProperty());
        tblQueue.setItems(QueueManager.getFilteredCuts());
        

        
        colPriority.setCellValueFactory(cellData -> cellData.getValue().priority.asObject());
        colOrderNumber.setCellValueFactory(cellData -> cellData.getValue().orderNumber);
        colLicensePlate.setCellValueFactory(cellData -> cellData.getValue().licensePlate);
        colPartNumber.setCellValueFactory(cellData -> cellData.getValue().partNumber);
        colWave.setCellValueFactory(cellData -> cellData.getValue().wave.asObject());
        colStatus.setCellValueFactory(cellData -> cellData.getValue().status);
        colUser.setCellValueFactory(cellData -> cellData.getValue().user);
        colType.setCellValueFactory(cellData -> cellData.getValue().type);
        colLine.setCellValueFactory(cellData -> cellData.getValue().line.asObject());
        colReel.setCellValueFactory(cellData -> cellData.getValue().reel);
        colOnHandQuantity.setCellValueFactory(cellData -> cellData.getValue().onHandQuantity.asObject());
        colCustomer.setCellValueFactory(cellData -> cellData.getValue().customer);
        colOrderQuantity.setCellValueFactory(cellData -> cellData.getValue().orderQuantity.asObject());
        colCarrier.setCellValueFactory(cellData -> cellData.getValue().carrier);
        colCableOD.setCellValueFactory(cellData -> cellData.getValue().cableDiameter.asObject());
        colShipOnDate.setCellValueFactory(cellData -> cellData.getValue().shipOnDate);
        colReplenStatus.setCellValueFactory(
            cellData -> {
                String strValue = "";
                try {
                    strValue = QueueManager.lookupReplenByLP(cellData.getValue().licensePlate.get()).status.get();
                } catch (NullPointerException e) {}

                if(cellData.getValue().status.get().equals("R")) {
                	strValue = cellData.getValue().wlu.get();
                }
                
                return new SimpleStringProperty(strValue);
            }
        );
        
        
        tblQueue.widthProperty().addListener((obs, oldValue, newValue) -> {
            tblQueue.getColumns().forEach(column -> {
                if(((TableColumn) column).getPrefWidth() == 75) {      
                    ((TableColumn) column).setMinWidth(75);
                    ((TableColumn) column).prefWidthProperty().bind((tblQueue.widthProperty().subtract(360)).divide(14));

                }
            });
        });
        
        AppManager.searchBoxProperty().addListener((obs, oldValue, newValue) -> {
        	txtSearchField.setText(newValue);
        });
        
        txtSearchField.textProperty().addListener((obs, oldValue, newValue) -> {
            if(newValue.equals("")) {
                QueueManager.clearFilter(QueueManager.CUT);
                QueueManager.filterByPriorityRange(0, 99);
                QueueManager.applyPersistantFilter(QueueManager.CUT, QueueManager.completedCuts.negate());
            } else {
                QueueManager.clearPersistantFilter(QueueManager.CUT);
                search(newValue);
            }
            
            AppManager.searchBoxProperty().set(newValue);
        });
        
        setFlags();
        
        
    }
    
    private void search(String strValue) {
        Predicate<QueueItem> pred = getContainsPredicate(strValue);
        QueueManager.clearFilter(QueueManager.ALL);
                
        QueueManager.applyFilter(QueueManager.CUT, pred);
    }
    
    private Predicate<QueueItem> getContainsPredicate(String strValue) {
        Predicate<QueueItem> pred = null;
        
        pred = ((Predicate<QueueItem>) item -> (item.orderNumber.get().contains(strValue)))
            .or(item -> (item.licensePlate.get().contains(strValue)))
            .or(item -> (item.partNumber.get().toUpperCase().contains(strValue.toUpperCase())))
            .or(item -> (Integer.toString(item.orderQuantity.get()).contains(strValue)))
            .or(item -> (Integer.toString(item.onHandQuantity.get()).contains(strValue)))
            .or(item -> (item.carrier.get().toUpperCase().contains(strValue.toUpperCase())))
            .or(item -> (Integer.toString(item.wave.get()).contains(strValue)))
            .or(item -> (item.status.get().toUpperCase().contains(strValue.toUpperCase())))
            .or(item -> (item.user.get().toUpperCase().contains(strValue.toUpperCase())))
            .or(item -> (item.type.get().toUpperCase().contains(strValue.toUpperCase())))
            .or(item -> (item.customer.get().toUpperCase().contains(strValue.toUpperCase())))
            .or(item -> (item.reel.get().toUpperCase().contains(strValue.toUpperCase())))
            .or(item -> (item.shipOnDate.get().contains(strValue)))
        	.or(item -> (item.wlu.get().contains(strValue)));
        
        return pred;
    }
    
    /*
    private Predicate<QueueItem> getExactPredicate(String strValue) {
        Predicate<QueueItem> pred = null;
        
        pred =((Predicate<QueueItem>) item -> (item.orderNumber.get().equalsIgnoreCase(strValue)))
            .or(item -> (item.licensePlate.get().equalsIgnoreCase(strValue)))
            .or(item -> (item.partNumber.get().equalsIgnoreCase(strValue)))  
            .or(item -> (Integer.toString(item.orderQuantity.get()).equalsIgnoreCase(strValue)))
            .or(item -> (Integer.toString(item.onHandQuantity.get()).equalsIgnoreCase(strValue)))
            .or(item -> (item.carrier.get().equalsIgnoreCase(strValue)))
            .or(item -> (Integer.toString(item.wave.get()).equalsIgnoreCase(strValue)))
            .or(item -> (item.status.get().equalsIgnoreCase(strValue)))
            .or(item -> (item.user.get().equalsIgnoreCase(strValue)))
            .or(item -> (item.type.get().equalsIgnoreCase(strValue)))
            .or(item -> (item.customer.get().equalsIgnoreCase(strValue)))
            .or(item -> (item.reel.get().equalsIgnoreCase(strValue)))
            .or(item -> (item.shipOnDate.get().equalsIgnoreCase(strValue)));
        
        return pred;
    }
    */
    
    @FXML
    private void reelsWith5OrMore() {
    	QueueManager.applyMultiCutFilter();
    }
    
    @FXML
    private void top5() {
    	QueueManager.applyTenOrMoreFilter();
    }
    
    @FXML
    private void todaysCuts() {
    	QueueManager.applyTodaysCutsFilter();
    }
    
    @FXML
    private void showCounts() {
    	AppManager.showQueue.set(false);
    }
    
    @FXML 
    private void excludeSelected() {
    	ObservableList<QueueItem> tempList = FXCollections.observableArrayList();
    	tempList.addAll(tblQueue.getSelectionModel().getSelectedItems());
    	
    	if(tblQueue.getSelectionModel().isCellSelectionEnabled()) {
    		if(tempList.size() > 1) {
    			@SuppressWarnings("rawtypes")
				TableColumn col = ((TablePosition) tblQueue.getSelectionModel().getSelectedCells().get(0)).getTableColumn();
    	        ObservableList<Integer> rows = FXCollections.observableArrayList();
    	        rows.addAll(tblQueue.getSelectionModel().getSelectedIndices());
    			
    			rows.forEach(row -> {
    				String colName = col.getText();
    				Object obj = col.getCellData(row.intValue());
    				try {
    					QueueManager.exclude(getPredicate(colName, obj));
    				} catch (NullPointerException ex) {}
    			});
    			    			
        	} else {
        		QueueManager.exclude(getPredicate());
        	}
    	} else {
    		for(QueueItem qItem : tempList) {
    			Predicate<QueueItem> pred = (item -> item.equals(qItem));
    			QueueManager.exclude(pred);
    		}
    	}
    	
    	
    }
    
    @FXML
    private void includeAll() {
        QueueManager.clearPersistantFilter(QueueManager.CUT);
        QueueManager.exclude(QueueManager.completedCuts);
    }
    
    @FXML
    private void setPriorities() {
    	QueueManager.setPersistantPriorities();
    	
    }
    
    @FXML
    private void print() {
        AppManager.print(tblQueue);
    }
    
    @FXML 
    private void clearAndFilter() {
    	Predicate<QueueItem> pred = getPredicate();
        
        if(pred != null) {
            QueueManager.applyFilter(QueueManager.CUT, pred, true);
        }
    }
    
    @Override
    void applyCellFilter() {
        Predicate<QueueItem> pred = getPredicate();
        
        if(pred != null) {
            QueueManager.applyFilter(QueueManager.CUT, pred);
        }
    }
    
    private void setFlags() {
        tblQueue.setRowFactory(tv -> new TableRow<QueueItem>() {
            @Override
            public void updateItem(QueueItem item, boolean empty) {
                super.updateItem(item, empty) ;
                resetRowStyle(this);
                
                if (item != null && !empty) {    
                    if (item.isStraightPull()) {
                        getStyleClass().add("sp");
                        
                        if(item.onHandQuantity.get() - item.orderQuantity.get() > 0) {
                        	getStyleClass().add("flag-quantity");
                        }
                        
                    } else if(QueueManager.getDuplicatePlates().contains(item.licensePlate.get())) {
                    	int index = QueueManager.getDuplicatePlates().indexOf(item.licensePlate.get());
                    	index -= (index / 100) * 100;
                    	
                    	getStyleClass().add("dup" + Integer.toString(index));
                        //getStyleClass().add("dup" + Integer.toString(Queues.getDuplicatePlates().indexOf(item.licensePlate.get())));
                        //System.out.println(item.licensePlate.get() + " " + "dup" + Integer.toString(CutQueue.getDuplicatePlates().indexOf(item.licensePlate.get())));
                    } 
                    
                    if(item.customer.get().contains("GRANITE")) {
                    	if(item.isCradle()) {
                            getStyleClass().add("flag-reel");
                            getStyleClass().add("flag-customer");
                        }
                    }
                    
                    if(item.carrier.get().contains("CLLQ CAN")) {
                    	if(!item.isStraightPull()) {
                    		getStyleClass().add("flag-carrier");
                    	}
                    }

                    if(item.status.get().equals("K") && item.user.get().isEmpty()) {
                    	getStyleClass().add("flag-k-status");
                    }
                    
                    if(item.reel.get().equals("60") || item.reel.get().equals("72")) {
                        getStyleClass().add("flag-reel");
                    }
                    
                    
                    String shipDate = item.shipOnDate.get();
                    LocalDate date = LocalDate.now();
                    int dtComp = 0;

                    if(LocalDateTime.now().getHour() < 14) 
                            date = LocalDate.now().minusDays(1);

                    if(!shipDate.equals("0"))
                            dtComp = date.compareTo(Utility.getDate(item.shipOnDate.get()));

                    if(dtComp != -1 && dtComp != 0 && item.priority.get() < 100) { 
                    	getStyleClass().add("flag-date");
                    }
                    
                    if(item.availStandardLength.get() != 0) {
                    	//getStyleClass().add("flag-standard");
                    }
                }
            }
        });
    }

    @Override
    void secondaryMouseFunction() {
        String orderNumber = ((QueueItem) tblQueue.getSelectionModel().getSelectedItem()).orderNumber.get();
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/dwm/app/fxml/NotesTable.fxml"));
        Parent root = null;
        try {
            root = loader.load();
            NotesTableController ntc = loader.getController();
            ntc.setOrderNumber(orderNumber);
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }
}
