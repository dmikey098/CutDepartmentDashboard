/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dwm.dashboard.controllers;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import org.dwm.dashboard.AppManager;
import org.dwm.dashboard.QueueManager;
import org.dwm.dashboard.bean.QueueItem;

/**
 *
 * @author Daniel Mikesell
 */
public abstract class TableController  {
    @FXML public TableView<QueueItem> tblQueue;
    @FXML public TableColumn<QueueItem, Integer> colPriority;
    @FXML public TableColumn<QueueItem, String> colOrderNumber;
    @FXML public TableColumn<QueueItem, String> colLicensePlate;
    @FXML public TableColumn<QueueItem, String> colPartNumber;
    @FXML public TableColumn<QueueItem, Integer> colWave;
    @FXML public TableColumn<QueueItem, String> colStatus;
    @FXML public TableColumn<QueueItem, String> colUser;
    @FXML public TableColumn<QueueItem, String> colType;
    @FXML public TableColumn<QueueItem, Integer> colLine;
    @FXML public TableColumn<QueueItem, String> colReel;
    @FXML public TableColumn<QueueItem, Double> colWeight;
    @FXML public TableColumn<QueueItem, Integer> colOrderQuantity;
    @FXML public TableColumn<QueueItem, String> colCarrier;
    @FXML public TableColumn<QueueItem, String> colCustomer;
    @FXML public TableColumn<QueueItem, String> colShipOnDate;
    @FXML public TableColumn<QueueItem, Double> colCableOD;
    @FXML public TableColumn<QueueItem, Integer> colOnHandQuantity;
    
    @SuppressWarnings({ "rawtypes" })
	void initialize(URL url, ResourceBundle rb) {
        tblQueue.getSelectionModel().setCellSelectionEnabled(true);
        tblQueue.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //If ALT is pressed change from cell selection to row selection
        tblQueue.setOnMouseClicked(e -> {
        	/*
        	if(e.getButton() == MouseButton.PRIMARY) {
        		TableColumn tc = tblQueue.getFocusModel().getFocusedCell().getTableColumn();
        		int row = tblQueue.getFocusModel().getFocusedIndex();
        		boolean b = tblQueue.getSelectionModel().isCellSelectionEnabled();
        		
        	    if(e.isAltDown()) {
	        		if(b) {
	        			tblQueue.getSelectionModel().setCellSelectionEnabled(false);
	        			tblQueue.getSelectionModel().clearAndSelect(row);
	        		}
        	    } else {
        	    	if(!b) {
        	    		tblQueue.getSelectionModel().setCellSelectionEnabled(true);
        	    		tblQueue.getSelectionModel().clearAndSelect(row, tc);
        	    	}
        	    }
        	} 
        	
        	
        	*/ 
        	//Apply cell filters on double click
            if(e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                applyCellFilter();
            }
        });
        
        
        tblQueue.setOnKeyPressed(event -> {
            //QueueItem item = (QueueItem) tblQueue.getFocusModel().getFocusedItem();
        	
        	//Copy selected cells on CTRL-C press
            if(event.getCode().equals(KeyCode.C) && event.isControlDown()) {
                ObservableList<TablePosition> list = tblQueue.getSelectionModel().getSelectedCells();
                int currentRow = -1;
                String str = "";
                
                //Build string of selected cells/rows
                for(TablePosition pos : list) {
                    int row = pos.getRow();
                    int col = pos.getColumn();
                    Object obj = ((TableColumn) tblQueue.getColumns().get(col)).getCellData(row);
                    
                    if(currentRow != row && currentRow != -1) {      
                        str += "\n";
                        currentRow = row;
                    } else if(currentRow == -1) {
                        currentRow = row;
                    } else {
                        str += AppManager.getDelimiter();
                    }
                    
                    str += obj.toString();
                }
                
                AppManager.statusTextProperty.set(str + " copied to clipboard."); //Show message on status bar
                
                //Set system clipboard to copied value
                StringSelection selection = new StringSelection(str);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection); 
            } else if(event.getCode().equals(KeyCode.ESCAPE)) {
                QueueManager.reset(); //clear filters on ESC
            }
        
        });
        
        tblQueue.getSortOrder().add(colPriority);
    }
    
    ObservableList<QueueItem> getSelectedItems() {
    	return tblQueue.getSelectionModel().getSelectedItems();
    }
    
    Predicate<QueueItem> getPredicate(String strValue) {
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
    
    
    Predicate<QueueItem> getPredicate(String colName, Object obj) {
    	Predicate<QueueItem> pred = null;
    	
    	
    	if(obj == null) {
    		return null;
    	}
    	
    	
    	if(colName.equals("Pty")) {
            pred = (item -> item.priority.get() == (int) obj);
        } else if(colName.equals("Order")) {
            pred = (item -> item.orderNumber.get().equals(obj));
        } else if(colName.equals("LP")) {
            pred = (item -> item.licensePlate.get().equals(obj));
        } else if(colName.equals("Part Number")) {
            pred = (item -> item.partNumber.get().equals(obj));
        } else if(colName.equals("Order Qty")) {
            pred = (item -> item.orderQuantity.get() == (int) obj);
        } else if(colName.equals("O/H Qty")) {
            pred = (item -> item.onHandQuantity.get() == (int) obj);
        } else if(colName.equals("Carrier")) {
            pred = (item -> item.carrier.get().equals(obj));
        } else if(colName.equals("Wave")) {
            pred = (item -> item.wave.get() == (int) obj);
        } else if(colName.equals("Status")) {
            pred = (item -> item.status.get().equals(obj));
        } else if(colName.equals("Type")) {
            pred = (item -> item.type.get().equals(obj));
        } else if(colName.equals("Customer")) {
            pred = (item -> item.customer.get().equals(obj));
        } else if(colName.equals("Ship Date")) {
            pred = (item -> item.shipOnDate.get().equals(obj));
        } else if(colName.equals("WLU")) {
        	pred = (item -> item.wlu.get().equals(obj));
        } else if(colName.equals("Reel")) {
        	pred = (item -> item.reel.get().equals(obj));
        } else if(colName.equals("Replen Sts")) {
        	pred = (item -> {        		
        		QueueItem replen = QueueManager.lookupReplenByLP(item.licensePlate.get()); 
        		String status = "";
        		
        		if(replen != null) {
        			status = replen.status.get(); 
        		}
        		
        		return status.equals(obj);
        	});
        }
        
        return pred;
    }
    
    @SuppressWarnings("rawtypes")
	Predicate<QueueItem> getPredicate() {
        TableColumn col = ((TablePosition) tblQueue.getSelectionModel().getSelectedCells().get(0)).getTableColumn();
        String colName = col.getText();
        int row = tblQueue.getSelectionModel().getFocusedIndex();
        Object obj = col.getCellData(row);
        if(col.equals(colPriority)) {
        }
        
        
        //Predicate<QueueItem> pred = null;
        
        return getPredicate(colName, obj);
        /*
        if(colName.equals("Pty")) {
            pred = (item -> item.priority.get() == (int) obj);
        } else if(colName.equals("Order")) {
            pred = (item -> item.orderNumber.get().equals(obj));
        } else if(colName.equals("LP")) {
            pred = (item -> item.licensePlate.get().equals(obj));
        } else if(colName.equals("Part Number")) {
            pred = (item -> item.partNumber.get().equals(obj));
        } else if(colName.equals("Order Qty")) {
            pred = (item -> item.orderQuantity.get() == (int) obj);
        } else if(colName.equals("O/H Qty")) {
            pred = (item -> item.onHandQuantity.get() == (int) obj);
        } else if(colName.equals("Carrier")) {
            pred = (item -> item.carrier.get().equals(obj));
        } else if(colName.equals("Wave")) {
            pred = (item -> item.wave.get() == (int) obj);
        } else if(colName.equals("Status")) {
            pred = (item -> item.status.get().equals(obj));
        } else if(colName.equals("Type")) {
            pred = (item -> item.type.get().equals(obj));
        } else if(colName.equals("Customer")) {
            pred = (item -> item.customer.get().equals(obj));
        } else if(colName.equals("Ship Date")) {
            pred = (item -> item.shipOnDate.get().equals(obj));
        } else if(colName.equals("WLU")) {
        	pred = (item -> item.wlu.get().equals(obj));
        } else if(colName.equals("Reel")) {
        	pred = (item -> item.reel.get().equals(obj));
        } else if(colName.equals("Replen Sts")) {
        	pred = (item -> {        		
        		QueueItem replen = Queues.lookupReplenByLP(item.licensePlate.get()); 
        		String status = "";
        		
        		if(replen != null) {
        			status = replen.status.get(); 
        		}
        		
        		return status.equals(obj);
        	});
        }
        
        return pred;
        */
    }
    
    abstract void applyCellFilter();
    abstract void secondaryMouseFunction();
    
    @SuppressWarnings("rawtypes")
	void resetRowStyle(TableRow tr) {
        tr.getStyleClass().clear();
        tr.getStyleClass().add("cell");
        tr.getStyleClass().add("indexed-cell");
        tr.getStyleClass().add("table-row-cell");
    }  
    
    QueueItem getSelectedItem() {
        return (QueueItem) tblQueue.getSelectionModel().getSelectedItem();
    }
    
    @SuppressWarnings("rawtypes")
	Object getValueOfSelectedCell() {
        return ((TablePosition) tblQueue.getSelectionModel().getSelectedCells().get(0)).getTableColumn().getCellData(tblQueue.getSelectionModel().getFocusedIndex());
    }
    
    @SuppressWarnings("rawtypes")
	Object getValueAt(int row, int col) {
        return ((TableColumn) tblQueue.getColumns().get(col)).getCellData(row);
    }
}


