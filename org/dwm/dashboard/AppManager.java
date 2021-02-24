/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dwm.dashboard;

import org.dwm.dashboard.controllers.CRDController;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Daniel Mikesell
 */
public class AppManager {
	private static String strIconFile = "/images/icon.png";
    public static StringProperty lastRefreshedProperty = new SimpleStringProperty("");
    public static StringProperty searchBoxTextProperty = new SimpleStringProperty();
    public static StringProperty statusTextProperty = new SimpleStringProperty("test");
    
    
    private final StringProperty statusBar = new SimpleStringProperty("");
    public static IntegerProperty pncTotal = new SimpleIntegerProperty();
    public static IntegerProperty crdCount = new SimpleIntegerProperty();
    public static IntegerProperty caseCount = new SimpleIntegerProperty();
    public static IntegerProperty palletCount = new SimpleIntegerProperty();
    public static IntegerProperty upsCount = new SimpleIntegerProperty();
    public static IntegerProperty graingerCount = new SimpleIntegerProperty();
    
    
    public static IntegerProperty cutCount = new SimpleIntegerProperty(1); 
    public static IntegerProperty spCount = new SimpleIntegerProperty(1); 
    public static IntegerProperty bwCountProperty = new SimpleIntegerProperty(1);
    public static IntegerProperty cordCountProperty = new SimpleIntegerProperty(1);
    public static IntegerProperty fiberCountProperty = new SimpleIntegerProperty(1);
    public static IntegerProperty pvCountProperty = new SimpleIntegerProperty(1);
    public static ArrayList<String> duplicateLPs = new ArrayList<>();
    public static IntegerProperty totalProperty = new SimpleIntegerProperty(1);
    public static IntegerProperty reelCountProperty = new SimpleIntegerProperty();
    public static ObjectProperty<ZonedDateTime> lastRefreshed = new SimpleObjectProperty<ZonedDateTime>(ZonedDateTime.now());
    
    private static AppManager INSTANCE;
    public HashMap<String, Object> options = new HashMap<>();
    private static ObservableList<HistoryItem> invHistory = FXCollections.observableArrayList();
    private static ObservableList<UserTotal> userTotals = FXCollections.observableArrayList();
    private static Stage mainStage;
    private static String pncFromDate = "1210101";
    public static boolean adminMode = false;
    
    public static boolean loggedIn = true;
    
    public static boolean isAdminMode() {
        return adminMode;
    }
    
    private AppManager() {
        options.put("delim", "\t");
        options.put("live_search", true);
        options.put("sp_quantity", 20);   
    }
    
    public static boolean getLiveSearch() {
        return (boolean) getInstance().options.get("live_search");
    }
    
    public static void setLiveSearch(boolean b) {
        getInstance().options.put("live_search", b);
    }
    
    public static int getStraightPullQuantity() {
        return (int) getInstance().options.get("sp_quantity");
    }
    
    public static void setStraightPullQuantity(int intValue) {
        getInstance().options.put("sp_quantity", intValue);
    }
    
    public static HashMap<String, Object> getOptions() {
        return getInstance().options;
    }
    
    private static AppManager getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new AppManager();
        }
        
        return INSTANCE;
    }
    
    public static String getPNCFromDate() {
        return pncFromDate;
    }
    
    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }
    
    public static Stage getMainStage() {
        return mainStage;
    }

    public static void setStatusBarText(String str) {
        getInstance().statusBar.set(str);
    }

    public static void updatePNCTotal() {
        pncTotal.set(QueryFunctions.getPNCTotal());
    }
    
    public static void refresh() {
        if(!loggedIn) { return; }
        
        Platform.runLater(() -> {
            Queues.refresh();
            updatePNCTotal();
            getInstance().setCRDCount();
            PickQueue.refresh();
            
            try {
				caseCount.set(QueryFunctions.getCasePickTotal());
				palletCount.set(QueryFunctions.getPalletPickTotal());
	            upsCount.set(QueryFunctions.getUPSPickTotal());
	            graingerCount.set(QueryFunctions.getGraingerPickTotal());
            } catch (IOException e) {
				e.printStackTrace();
			}
            
            
            Queues.reset();
            invHistory.clear();
            invHistory.addAll(QueryFunctions.getLast10InventoryTransactions());
            userTotals.clear();
            userTotals.addAll(QueryFunctions.getUserTotalsForShift());
            lastRefreshedProperty.set("Last refreshed on " + LocalDate.now() + " at " + LocalTime.now());
            lastRefreshed.set(ZonedDateTime.now());
        });
    }
    
    public static void openCalculator() {
        getInstance().openCalculator(true);
    }
    
    public static void openCRDChecker() {
        getInstance().openCRDChecker(true);
    }
    
    private void openCalculator(boolean b) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Calculator.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Cable Calculator");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
            
        } catch (IOException e) {
            System.out.println("IOException - " + e.getMessage() );
        }
    }
    
    private void openCRDChecker(boolean b) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CRD.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("CRD");
            stage.setScene(new Scene(root));
            //stage.setResizable(false);
            stage.show();
            
        } catch (IOException e) {
            System.out.println("IOException - " + e.getMessage() );
        }
    }
    
    public static void openAboutDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(AppManager.class.getClass().getResource("/fxml/AboutDialog.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("About - Dashboard");
            stage.setScene(new Scene(root));
            
            stage.getIcons().add(getIcon());
            
            
            //stage.setResizable(false);
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void sendTotalsToClipboard() {
    	String str = "";
    	
    	str += "Cut: \n";
    	str += "===============\n";
    	str += "Cuts: " + Queues.getFilteredCuts().size() + "\n";
    	str += "SPs: " + Queues.spCount.get() + "\n";
    	str += "Total: " + Queues.spCount.get() + "\n";
    	
    	str += "\nBreakdown: \n";
    	str += "===============\n";
    	str += "Reels: " + Queues.reelCountProperty.get() + "\n";
    	str += "BW: " + Queues.bwCountProperty.get() + "\n";
    	str += "Cord: " + Queues.cordCountProperty.get() + "\n";
    	str += "Fiber: " + Queues.fiberCountProperty.get() + "\n";
    	str += "PV: " + Queues.pvCountProperty.get() + "\n";
    	
    	str += "\nPick: \n";
    	str += "===============\n";
    	str += "Case: " + caseCount.get() + "\n";
    	str += "Pallet: " + palletCount.get() + "\n";
    	str += "UPS: " + AppManager.upsCount.get() + "\n";
    	str += "Grainger: " + AppManager.graingerCount.get() + "\n";
    	
    	
    	StringSelection selection = new StringSelection(str);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    	
    	
    }
    
    public static Image getIcon() {
    	Image image = null;
    	
    	try {
        	image = new Image(MainApp.class.getResourceAsStream(strIconFile));
        	
        } catch (Throwable t) {
        	t.printStackTrace();
        }
    	
    	return image;
    }
    
    
    String exclusions = "";
    private void setCRDCount() {
        exclusions = "";
        int count = 0;
        try {
            Files.lines(Paths.get("C:\\Macros\\seq.txt")).forEach(line -> {            
                exclusions += "," + line;
            });
        } catch (IOException ex) {
            Logger.getLogger(CRDController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        exclusions = exclusions.replaceFirst(",", "");
        try {
            String sqlStmt = (QueryFunctions.readSQLFile("/sql/CRD.sql").replace("<THHSEQ>", exclusions));
            try (ResultSet rs = QueryFunctions.query(sqlStmt)) {
                while(rs.next()) {
                    count++;
                }
                System.out.println(count);
            } catch (SQLException ex) {
                Logger.getLogger(CRDController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(CRDController.class.getName()).log(Level.SEVERE, null, ex);
        }
        crdCount.set(count);
    }
    
    static public void loadTemplate(Pane parent, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getInstance().getClass().getResource(fxmlFile));
            Parent root = null;
            root = loader.load();    
            Scene scene = new Scene(root);
            parent.getChildren().add(scene.getRoot());
        } catch (IOException ex) {
            ex.printStackTrace();
        } 
    }
    
    static public ObservableList<HistoryItem> getInventoryHistory() {
        return invHistory;
    }
    
    public static ObservableList<UserTotal> getUserTotals() {
        return userTotals;
    }
    
    public static void print(Node node) {
        PrinterJob job = PrinterJob.createPrinterJob();
        Printer printer = job.getPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, Printer.MarginType.HARDWARE_MINIMUM);
        boolean success = job.printPage(pageLayout, node);
        
        
        if(success){
            job.endJob();
        }
    }
}
