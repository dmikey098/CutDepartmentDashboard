/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dwm.dashboard.controllers;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import org.dwm.dashboard.QueryFunctions;

/**
 * FXML Controller class
 *
 * @author mikesda001
 */
public class CRDController implements Initializable {
    ObservableList<String> list = FXCollections.observableArrayList();
    String exclusions = "";
    
    @FXML private TableView<RowItem> tblResults;
    @FXML private ListView<String> lstHistorySequence;
    @FXML private TableColumn<RowItem, String> colSeq;
    @FXML private TableColumn<RowItem, String> colDats;
    @FXML private TableColumn<RowItem, String> colUsrs;
    @FXML private TableColumn<RowItem, String> colLot;
    @FXML private TableColumn<RowItem, String> colOlcp;
    @FXML private TableColumn<RowItem, String> colAsle;
    @FXML private TableColumn<RowItem, String> colBay;
    @FXML private TableColumn<RowItem, String> colLvl;
    @FXML private TableColumn<RowItem, String> colOhq;
    @FXML private TableColumn<RowItem, String> colComt;
    @FXML private TableColumn<RowItem, String> colQty;
    @FXML private TableColumn<RowItem, String> colDiff;
    

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lstHistorySequence.setItems(list);
        ObservableList<RowItem> data = FXCollections.observableArrayList();
        tblResults.setItems(data);
        colSeq.setCellValueFactory(c -> c.getValue().seq);
        colDats.setCellValueFactory(c -> c.getValue().dats);
        colUsrs.setCellValueFactory(c -> c.getValue().usrs);
        colLot.setCellValueFactory(c -> c.getValue().lot);
        colOlcp.setCellValueFactory(c -> c.getValue().olcp);
        colAsle.setCellValueFactory(c -> c.getValue().asle);
        colBay.setCellValueFactory(c -> c.getValue().bay);
        colLvl.setCellValueFactory(c -> c.getValue().lvl);
        colOhq.setCellValueFactory(c -> c.getValue().ohq);
        colComt.setCellValueFactory(c -> c.getValue().comt);
        colQty.setCellValueFactory(c -> c.getValue().qty);
        colDiff.setCellValueFactory(c -> c.getValue().diff);
                
        
        try {
            Files.lines(Paths.get("C:\\Macros\\seq.txt")).forEach(line -> {
                list.add(line);
                exclusions += "," + line;
            });
        } catch (IOException ex) {
            Logger.getLogger(CRDController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        exclusions = exclusions.replaceFirst(",", "");
        try {
            String sqlStmt = (QueryFunctions.readSQLFile("/org/dwm/app/sql/CRD.sql").replace("<THHSEQ>", exclusions));
            
            
            System.out.println(sqlStmt);
            try (ResultSet rs = QueryFunctions.query(sqlStmt)) {
                while(rs.next()) {
                    RowItem row = new RowItem();
                    row.seq.set(rs.getString(1));
                    row.dats.set(rs.getString(2));
                    row.usrs.set(rs.getString(3));
                    row.lot.set(rs.getString(4));
                    row.olcp.set(rs.getString(5));
                    row.asle.set(rs.getString(6));
                    row.bay.set(rs.getString(7));
                    row.lvl.set(rs.getString(8));
                    row.ohq.set(rs.getString(9));
                    row.comt.set(rs.getString(10));
                    row.qty.set(rs.getString(11));
                    row.diff.set(rs.getString(12));
                    
                           
                    
                    data.add(row);
                }
            } catch (SQLException ex) {
                Logger.getLogger(CRDController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        } catch (IOException ex) {
            Logger.getLogger(CRDController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        tblResults.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.D) {
                RowItem selected = tblResults.getSelectionModel().getSelectedItem();
                list.add(selected.seq.getValue());
                data.remove(selected);
            }
            
            if(event.getCode() == KeyCode.S && event.isControlDown()) {
                writeFile();
            }
            
            if(event.getCode() == KeyCode.C && event.isControlDown()) {
                StringSelection selection = new StringSelection(tblResults.getSelectionModel().getSelectedItem().olcp.getValue().trim());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
            }
        });
        
        
        
        lstHistorySequence.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.D) {
                String selected = lstHistorySequence.getSelectionModel().getSelectedItem();
                list.remove(selected);
            }
        
        });
        
        
        
    }   
    
 
    
    
    private void writeFile() {
        try (FileWriter myWriter = new FileWriter("C:\\Macros\\seq.txt")) {
            for(String line : list) {
                myWriter.write(line + "\n");    
            }
        } catch (IOException ex) {
            Logger.getLogger(CRDController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Successfully wrote to the file.");
    }
    
}
class RowItem {
    public StringProperty seq = new SimpleStringProperty();
    public StringProperty dats = new SimpleStringProperty();
    public StringProperty usrs = new SimpleStringProperty();
    public StringProperty lot = new SimpleStringProperty();
    public StringProperty olcp = new SimpleStringProperty();
    public StringProperty asle = new SimpleStringProperty();
    public StringProperty bay = new SimpleStringProperty();
    public StringProperty lvl = new SimpleStringProperty();
    public StringProperty ohq = new SimpleStringProperty();
    public StringProperty comt = new SimpleStringProperty();
    public StringProperty qty = new SimpleStringProperty();
    public StringProperty diff = new SimpleStringProperty();

     public RowItem() {
    
     }
 }