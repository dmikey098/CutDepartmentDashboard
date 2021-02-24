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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.dwm.dashboard.AppManager;
import org.dwm.dashboard.MainApp;
import org.dwm.dashboard.QueueItem;
import org.dwm.dashboard.Queues;
import org.dwm.dashboard.Utility;

/**
 * FXML Controller class
 *
 * @author Daniel Mikesell
 */


public class LegacyCutTableController extends TableController implements Initializable { 
    @FXML private TableColumn<QueueItem, String> colReplenStatus;
    @FXML private ContextMenu pMenu2;
    @FXML private MenuItem mitmExclude;
    @FXML private MenuItem mitmInclude;
    @FXML public ScrollPane scrollPane;
    @FXML private TextField txtSearchField;
    
    
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        
        Queues.getFilteredCuts().comparatorProperty().bind(tblQueue.comparatorProperty());
        tblQueue.setItems(Queues.getFilteredCuts());
        

        
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
                    strValue = Queues.lookupReplenByLP(cellData.getValue().licensePlate.get()).status.get();
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
        
        txtSearchField.textProperty().addListener((obs, oldValue, newValue) -> {
            if(newValue.equals("")) {
                Queues.clearFilter(Queues.CUT);
                Queues.filterByPriorityRange(0, 99);
                Queues.applyPersistantFilter(Queues.CUT, Queues.completedCuts.negate());
            } else {
                Queues.clearPersistantFilter(Queues.CUT);
                search(newValue, false);
            }
        });
        
        mitmExclude.setOnAction(e -> {
            excludeSelected();
        });
        
        mitmInclude.setOnAction(e -> {
            includeAll();
        });
        
        
        
        setFlags();
    }
    
    private void search(String strValue, boolean exactMatch) {
        Predicate<QueueItem> pred = null;
        Queues.clearFilter(Queues.ALL);
        
        if(exactMatch) {
            pred = getExactPredicate(strValue);
        } else if(strValue.contains("-")) {
            pred = item -> (Integer.toString(item.priority.get()).equals(strValue));
            try {
                String[] arr = strValue.split("-");
                
                if(arr.length == 1) {    
                    int f = Integer.parseInt(arr[0].trim());
                    pred = item -> (item.priority.get() == f);
                } else if(arr.length == 2) {
                    int f = Integer.parseInt(arr[0].trim());
                    int e = Integer.parseInt(arr[1].trim());
                    pred = item -> (item.priority.get() >= f);
                    pred = pred.and(item -> (item.priority.get() <= e));
                }
            } catch(ArrayIndexOutOfBoundsException | NumberFormatException e) {}
        } else {
            pred = getContainsPredicate(strValue);
        }
        
        Queues.applyFilter(Queues.CUT, pred);
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
    
    @FXML 
    private void excludeSelected() {
        Queues.exclude(getPredicate());
    }
    
    @FXML
    private void includeAll() {
        Queues.clearPersistantFilter(Queues.ALL);
        Queues.exclude(Queues.completedCuts);
    }
    
    @FXML
    private void print() {
        AppManager.print(tblQueue);
    }
    
    @Override
    void applyCellFilter() {
        Predicate<QueueItem> pred = getPredicate();
        System.out.println(pred);
        if(pred != null) {
            Queues.applyFilter(Queues.CUT, pred);
        }
    }
    
    
    @SuppressWarnings("unchecked")
	private void setFlags() {
        tblQueue.setRowFactory(tv -> new TableRow<QueueItem>() {
            @Override
            public void updateItem(QueueItem item, boolean empty) {
                super.updateItem(item, empty) ;
                resetRowStyle(this);
                
                if (item != null && !empty) {    
                    if (item.onHandQuantity.get() - item.orderQuantity.get() <= AppManager.getStraightPullQuantity()) {
                        getStyleClass().add("sp");
                    } else if(Queues.getDuplicatePlates().contains(item.licensePlate.get())) {
                    	int index = Queues.getDuplicatePlates().indexOf(item.licensePlate.get());
                    	index -= (index / 100) * 100;
                    	
                    	getStyleClass().add("dup" + Integer.toString(index));
                        //getStyleClass().add("dup" + Integer.toString(Queues.getDuplicatePlates().indexOf(item.licensePlate.get())));
                        //System.out.println(item.licensePlate.get() + " " + "dup" + Integer.toString(CutQueue.getDuplicatePlates().indexOf(item.licensePlate.get())));
                    } 
                    
                    if(item.customer.get().contains("GRANITE")) {
                        int reelSize = Utility.getReelValue(item);
                        if(reelSize == 44 || reelSize >= 48) {
                            getStyleClass().add("flag-reel");
                            getStyleClass().add("flag-customer");
                        }
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
