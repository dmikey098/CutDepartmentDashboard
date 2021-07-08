package org.dwm.dashboard.controllers;

import java.awt.print.PrinterException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import org.dwm.dashboard.AppManager;
import org.dwm.dashboard.PNC;
import org.dwm.dashboard.PickQueueTotals;
import org.dwm.dashboard.QueryFunctions;
import org.dwm.dashboard.QueueManager;
import org.dwm.dashboard.Reports;
import org.dwm.dashboard.bean.QueueItem;

/**
 * FXML Controller class
 *
 * @author Daniel Mikesell
 */
public class LegacyDashboardController implements Initializable { 
    @FXML private BorderPane app;
    @FXML private TabPane tabPane;
    @FXML private AnchorPane donutChart;
    @FXML private Label lblCutCount;
    @FXML private Label lblSPCount;
    @FXML private Label lblCutTotal;
    @FXML private Label lblReelCount;
    @FXML private Label lblCutsOver2500;
    @FXML private Label lblCutsOver5000;
    @FXML private Label lblTruckload;
    @FXML private Label lblLTLCuts;
    @FXML private Label lblUPSCuts;
    @FXML private Label lblBWCuts;
    @FXML private Label lblCordCuts;
    @FXML private Label lblFiberCuts;
    @FXML private Label lblPVCuts;
    @FXML private Label lblProjCradles;
    @FXML private Label lbl60Cradles;
    @FXML private Label lbl60CradlesLabel;
    @FXML private Label lblCradles;
    @FXML private Label lblShuttles;
    @FXML private Label lblReplens;
    @FXML private Label lblPNCTotal;
    @FXML private Label lblCase;
    @FXML private Label lblPallet;
    @FXML private Label lblUPS;
    @FXML private Label lblGrainger;
    @FXML private Label lblStatusBar;
    @FXML private Label lblWrongDate;
    @FXML private Label lblWrongDateValue;
    @FXML private Label lblCRDCount;
    @FXML private Label lblPersistantFilter;
    @FXML private Label lblLabelFormatResults;
    @FXML private TextField txtFromPriority;
    @FXML private TextField txtToPriority;   
    @FXML private TextField txtPNForLabelFormat;
    @FXML private AnchorPane donut;
    @FXML private GridPane labelGrid;
    @FXML private MenuItem mitmCrdChecker;
    @FXML private MenuItem mitmExclude;
    ObservableList<Label> column1 = FXCollections.observableArrayList();
    ObservableList<Label> column2 = FXCollections.observableArrayList();
    ObservableList<Label> column3 = FXCollections.observableArrayList();
    ObservableList<Label> column4 = FXCollections.observableArrayList();
    @FXML private Tab cutTab;
    @FXML private Node cutTable;
    @FXML private Node countTable;
    @FXML private UtilityTab utilityTab;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        VBox.setVgrow(tabPane, Priority.ALWAYS);
        
        //Key handlers
        app.setOnKeyPressed((KeyEvent e) -> {
            if(e.getCode().equals(KeyCode.ENTER) && app.getScene().getFocusOwner() instanceof Button) {
                ((Button) app.getScene().getFocusOwner()).fire();
            } else if((e.getCode().equals(KeyCode.C) && !e.isControlDown()) || e.getCode().equals(KeyCode.DIGIT1)) { //&& e.isAltDown()) {
                tabPane.getSelectionModel().select(0);
            } else if(e.getCode().equals(KeyCode.S) || e.getCode().equals(KeyCode.DIGIT2)) { // && e.isAltDown()) {
                tabPane.getSelectionModel().select(1);
            } else if(e.getCode().equals(KeyCode.R) || e.getCode().equals(KeyCode.DIGIT3)) { // && e.isAltDown()) {
                tabPane.getSelectionModel().select(2);
            } else if(e.getCode().equals(KeyCode.F5)) {
                refresh();
            } else if(e.getCode().equals(KeyCode.ESCAPE)) {
                clearFilter();
            } else if(e.getCode().equals(KeyCode.E) && e.isControlDown()) {
            	AppManager.sendTotalsToClipboard();
            }
        });
        
        
        //Focus on txtAsle field on Utility Tab when tab is selected
        tabPane.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
        	if((int)newValue == 4) {
        		Platform.runLater(() -> {
        			utilityTab.requestFocus();
            		utilityTab.txtAsle.requestFocus();
        		});	
        	}
        	
        });
        
        //=============== Labels ===============
        //Column 1
        lblCutCount.textProperty().bind(QueueManager.cutCount.asString());
        lblSPCount.textProperty().bind(QueueManager.spCount.asString());
        lblCutTotal.textProperty().bind(QueueManager.totalProperty.asString());
        lblReelCount.textProperty().bind(QueueManager.reelCountProperty.asString());
        lblTruckload.textProperty().bind(QueueManager.countBinding(QueueManager.CUT, row -> { return row.isTruckload(); }).asString());
        lblLTLCuts.textProperty().bind(QueueManager.countBinding(QueueManager.CUT, row -> { return !row.isTruckload() && !row.isUPS(); }).asString());
        lblUPSCuts.textProperty().bind(QueueManager.countBinding(QueueManager.CUT, row -> { return row.isUPS(); }).asString());
        
        //Column 2
        lblBWCuts.textProperty().bind(QueueManager.bwCountProperty.asString());
        lblCordCuts.textProperty().bind(QueueManager.cordCountProperty.asString());
        lblFiberCuts.textProperty().bind(QueueManager.fiberCountProperty.asString());
        lblPVCuts.textProperty().bind(QueueManager.pvCountProperty.asString());
        lblCutsOver2500.textProperty().bind(QueueManager.cutsOver2500.asString());
        lblCutsOver5000.textProperty().bind(QueueManager.cutsOver5000.asString());
        
        //Column 3
        lblPNCTotal.textProperty().bind(PNC.totalProperty());
        lblShuttles.textProperty().bind(QueueManager.countBinding(QueueManager.SHUTTLE, row -> { return true; }).asString());
        lblReplens.textProperty().bind(QueueManager.countBinding(QueueManager.REPLEN, row -> { return true; }).asString());
        lblCradles.textProperty().bind(QueueManager.countBinding(QueueManager.SHUTTLE, row -> { return row.isCradle(); }).asString());
        lblProjCradles.textProperty().bind(QueueManager.countBinding(QueueManager.CUT, row -> { return row.isCradle(); }).asString());
        lbl60Cradles.textProperty().bind(QueueManager.countBinding(QueueManager.CUT, row -> { 
        	boolean b = false;
        	
        	if(row.reel.get().equals("60") && row.getType().equals("BW")) {
        		b = true;
        	} else if(row.reel.get().equals("72")) {
        		b = true;
        	}
        	
        	return b; 
        }).asString());
        //lblTruckloadCuts.textProperty().bind(Queues.countBinding(Queues.CUT, row -> { return row.isTruckload(); }).asString());
        
        lbl60Cradles.textProperty().addListener((obs, oldValue, newValue) -> {
            boolean b = !newValue.equals("0");
            lbl60Cradles.setVisible(b);
            lbl60CradlesLabel.setVisible(b);
        });
        
        
        
        
        //lblCRDCount.textProperty().bind(AppManager.crdCount.asString());
        lblStatusBar.textProperty().bind(AppManager.lastRefreshedProperty);
        
        
        
        //Hide donut chart when window below 975px
        AppManager.getMainStage().widthProperty().addListener((obs, oldValue, newValue) -> {
        	if(newValue.intValue() < 975)
        		donutChart.setVisible(false);
        	else
        		donutChart.setVisible(true);
        });
        
        
        
        //Filter on enter in from priority text field
        txtFromPriority.setOnKeyPressed(e -> {
        	if(e.getCode() == KeyCode.ENTER) {
        		filterByPriorityRange();
        	}
        	
        });
        
        AppManager.showQueue.addListener((obs, oldValue, newValue) -> {
        	if(newValue) {
        		cutTab.setContent(cutTable);
        	} else {
        		cutTab.setContent(countTable);
        	}
        });
                
        /*
        lblWrongDateValue.textProperty().bind(Queues.countBinding(Queues.CUT, row -> {
            String shipDate = row.shipOnDate.get();
            LocalDate date = LocalDate.now();
            int dtComp = 0;
            
            if(LocalDateTime.now().getHour() < 14) 
                    date = LocalDate.now().minusDays(1);

            if(!shipDate.equals("0"))
                    dtComp = date.compareTo(Utility.getDate(shipDate));
            
            return dtComp != -1 && dtComp != 0 && row.priority.get() < 100;
        }).asString());
        
        
        
        lblWrongDateValue.textProperty().addListener((obs, oldValue, newValue) -> {
            boolean b = !newValue.equals("0");
            lblWrongDateValue.setVisible(b);
            lblWrongDate.setVisible(b);
        });
        
        lblCRDCount.textProperty().addListener((obs, oldValue, newValue) -> {
            lblCRDCount.setVisible(false);
            lblCRDCount.setTextFill(Color.BLACK);
            if(!newValue.equals("0")) {
                lblCRDCount.setTextFill(Color.RED);
                lblCRDCount.setVisible(true);
            }
        });
        */
        
        lblCase.textProperty().bind(PickQueueTotals.caseProperty().asString());
        lblPallet.textProperty().bind(PickQueueTotals.palletProperty().asString());
        lblUPS.textProperty().bind(PickQueueTotals.upsProperty().asString());
        lblGrainger.textProperty().bind(PickQueueTotals.graingerProperty().asString());
        
        if(AppManager.isAdminMode()) {
            mitmCrdChecker.setVisible(true);
        }
        
        lblPNCTotal.textProperty().addListener((obs, oldValue, newValue) -> {
            lblPNCTotal.setTextFill(Color.BLACK);
            if(!newValue.equals("0")) 
                lblPNCTotal.setTextFill(Color.RED);
        });
    }    
    
        
    @FXML
    private void exportCuts() {
    	Reports.exportCutQueue();
    }
    
    @FXML
    private void exportReplens() {
    	Reports.exportReplenQueue();
    }
    
    @FXML 
    private void exportPNCs() {
    	Reports.exportPNCs();
    }
    
    @FXML
    private void exportTotals() {
    	Reports.exportTotals();
    }
    
    @FXML
    private void printCutTable() {
        JTable table = new JTable();
        table.setModel(new AbstractTableModel() {
            /**
			 * 
			 */
			private static final long serialVersionUID = -2980818670632657356L;
			private String[] colNames = {"", "", ""};
            SortedList<QueueItem> data = QueueManager.getFilteredCuts();
            
            
            @Override
            public int getRowCount() {
                return data.size();
            }

            @Override
            public int getColumnCount() {
                return 3;
            }
            
            @Override
            public String getColumnName(int col) {
                return colNames[col];
            }

            @Override
            public Object getValueAt(int i, int i1) {
                QueueItem item = data.get(i);
                
                switch (i1) {
                    case 0:
                        return item.priority.get();
                    case 1:
                        return item.orderNumber.get();
                    case 2:
                        return item.licensePlate.get();
                    default:
                        break;
                }
                
                return "";
            }
        });
        
        JFrame frame = new JFrame();
        frame.add(table);
        frame.setVisible(true);
        
            Platform.runLater(() -> {
                try {
                    table.print();
                } catch (PrinterException ex) {
                    ex.printStackTrace();
                }
            });
            
        
        
    }
    
    @FXML
    private void filterByPriorityRange(){
        try {
        	
            int minPty = Integer.parseInt(txtFromPriority.getText());
            int maxPty = txtToPriority.getText().isEmpty() ? Integer.parseInt(txtFromPriority.getText()) : Integer.parseInt(txtToPriority.getText());
            QueueManager.clearFilter(QueueManager.ALL);
            QueueManager.filterByPriorityRange(minPty, maxPty);
        } catch (NumberFormatException ignore) {}
    }
    
    /*
    @FXML
    private void filterByPriorityRange() {
        int start = Integer.parseInt(txtFromPriority.getText());
        int end = Integer.parseInt(txtToPriority.getText());
        
        Queues.clearFilter(Queues.ALL);
        Queues.filterByPriorityRange(start, end);
    }
    */
    
    @FXML
    private void clearFilter() {
    	AppManager.searchBoxProperty().set("");
        txtFromPriority.setText("");
        txtToPriority.setText("");
        QueueManager.reset();
    }
    
    @FXML
    public void refresh() {
        AppManager.refresh();
    }
    
    
    @FXML
    public void getLabelFormat() {
        String partNumber = txtPNForLabelFormat.getText();
        lblLabelFormatResults.setText("");
        if(partNumber != null && !partNumber.isEmpty()) {
            String sqlStmt = ""
                + "SELECT\n" +
                "    CAFORM\n" +
                "FROM\n" +
                "    HFADTAGC.UCCARL1\n" +
                "WHERE\n" +
                "    CAPART = '" + partNumber.toUpperCase() + "'\n" +
                "GROUP BY\n" +
                "    CAFORM\n";
            
            ArrayList<ArrayList<Object>> rows = QueryFunctions.selectAsList(sqlStmt);
            if(rows != null) {
                String results = "";
                results = rows.stream().map((row) -> (String) row.get(0) + " ").reduce(results, String::concat);
                lblLabelFormatResults.setText(results);
            } else {
                lblLabelFormatResults.setText("Format not found.");
            }
        }
    }
    
    
    @FXML
    private void filterLTLCuts() {
    	QueueManager.applyFilter(QueueManager.CUT, item -> {
    		return (!item.isTruckload() && !item.isUPS());
    	});
    }
    
    @FXML
    private void filterUPSCuts() {
    	QueueManager.applyFilter(QueueManager.CUT, item -> item.isUPS());
    }
    
    @FXML
    private void filterTruckloadCuts() {
    	QueueManager.applyFilter(QueueManager.CUT, item -> item.isTruckload());
    }
    
    @FXML
    private void filterCradles() {
        QueueManager.applyFilter(QueueManager.SHUTTLE, item -> item.isCradle());
    }
    
    @FXML
    private void filterProjCradles() {
    	QueueManager.applyFilter(QueueManager.CUT, item -> item.isCradle());
    }
        
    @FXML
    private void filterSPs() {
        QueueManager.applyFilter(QueueManager.CUT, item -> (item.onHandQuantity.get() - item.orderQuantity.get() <= AppManager.getStraightPullQuantity()));
    }
    
    @FXML
    private void filterBW() {
        filterCuts();
        QueueManager.applyFilter(QueueManager.CUT, item -> (item.type.get().equals("BW")));
    }
    
    @FXML
    private void filterFiber() {
        filterCuts();
        QueueManager.applyFilter(QueueManager.CUT, item -> (item.type.get().equals("FIBER")));
    }
    
    @FXML
    private void filterCord() {
        filterCuts();
        QueueManager.applyFilter(QueueManager.CUT, item -> (item.type.get().equals("CORD")));
    }
    
    @FXML
    private void filterPV() {
        filterCuts();
        QueueManager.applyFilter(QueueManager.CUT, item -> (item.type.get().equals("PV")));
    }
    
    @FXML
    private void filterCuts() {
        QueueManager.applyFilter(QueueManager.CUT, item -> (item.onHandQuantity.get() - item.orderQuantity.get() > AppManager.getStraightPullQuantity()));
    }

    @FXML 
    private void filterMultiCuts() {
        QueueManager.applyFilter(QueueManager.CUT, item -> (QueueManager.getDuplicatePlates().contains(item.licensePlate.get())));
    }
    
    @FXML
    private void filterCutsOver2500() {
        QueueManager.applyFilter(QueueManager.CUT, item -> item.orderQuantity.get() >= 2500 && item.orderQuantity.get() < 5000);
    }
    
    @FXML
    private void filterCutsOver5000() {
        QueueManager.applyFilter(QueueManager.CUT, item -> item.orderQuantity.get() >= 5000);
    }
    
    @FXML
    private void openCalculator() {
        AppManager.openCalculator();
    }
    
    @FXML
    private void openCRDChecker() {
        AppManager.openCRDChecker();
    }
    
    @FXML
    private void openAboutDialog() {
    	AppManager.openAboutDialog();
    }
    
    
    @FXML
    private void close() {
        Platform.exit();
    }
      

    @SuppressWarnings("unused")
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
            .or(item -> (item.shipOnDate.get().contains(strValue)));
        
        return pred;
    }
    
    @SuppressWarnings("unused")
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
    
    public DoubleBinding getBinding(IntegerProperty intProp) {
        return Bindings.createDoubleBinding(() -> {
            if(QueueManager.cutCount.get() != 0) {
                return intProp.doubleValue() / (QueueManager.cutCount.doubleValue() + QueueManager.spCount.doubleValue()) * 360.00;
            }            
            return 0.0;
        }, QueueManager.cutCount);
    }    
    
    public void setTab() {
        tabPane.getSelectionModel().select(0);
    }
    
}

/*
    //Find reels with the most multicuts
    long first = -1;
    long second = -1;
    String strFirst = "";
    String strSecond = "";
    Map<Object, Long> dups = new HashMap<>();

//This can be added to the refresh in CutQueue to avoid multiple loops
        Map<Object, Long> map = CutQueue.duplicateLPs.stream().collect(Collectors.groupingBy(row -> { return row; }, Collectors.counting()));
        map.forEach((k, v) -> {
            if(v > first) {
                first = v;
                strFirst = k.toString();
            } else if(v > second) {
                second = v;
                strSecond = k.toString();
            }
        
        });


*/