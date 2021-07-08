/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dwm.dashboard;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
	public static final String ICON_FILE = "/images/icon.png";
	public static final String ICON_FILE_SNAPSHOT = "/images/icon-snapshot.png";
	public static final String PNC_DATE = "1210101";
	public static final int PNC_OFFSET = -51000;
	
	private static BooleanProperty accessRequired = new SimpleBooleanProperty(true);
	
	public static StringProperty lastRefreshedProperty = new SimpleStringProperty("");
    public static StringProperty statusTextProperty = new SimpleStringProperty("");
    private static StringProperty searchBoxTextProperty = new SimpleStringProperty();
    
    private static ObservableList<UserTotal> userTotals = FXCollections.observableArrayList();
    
    private static Stage mainStage;
    
    public static boolean adminMode = false;
    public static boolean loggedIn = true;
    private static StringProperty user = new SimpleStringProperty();
    private static IntegerProperty access = new SimpleIntegerProperty();
    
    public static BooleanProperty showQueue = new SimpleBooleanProperty(true);
    public static BooleanProperty includeUPSInTotals = new SimpleBooleanProperty(false);
    public static final double CRADLE_WEIGHT = 600.0;
    
    private static Map<String, Object> options = new HashMap<>();
    
    public static boolean isAdminMode() {
        return adminMode;
    }
    
    static {
        options.put("delim", "\t");
        options.put("live_search", true);
        options.put("sp_quantity", 20);  
    }
    
    public static BooleanProperty accessRequiredProperty() {
    	return accessRequired;
    }
    
    public static boolean isAccessRequired() {
    	return accessRequired.get();
    }
    
    public static boolean getLiveSearch() {
        return (boolean) options.get("live_search");
    }
    
    public static void setLiveSearch(boolean b) {
        options.put("live_search", b);
    }
    
    public static int getStraightPullQuantity() {
        return (int) options.get("sp_quantity");
    }
    
    public static void setStraightPullQuantity(int intValue) {
        options.put("sp_quantity", intValue);
    }
    
    public static String getDelimiter() {
    	return (String) options.get("delim");
    }
    
    
    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }
    
    public static Stage getMainStage() {
        return mainStage;
    }

    
    public static void refresh() {
    	if(!hasBasicAccess()) { return; }
        
        
        QueueManager.refresh();
        PickQueueTotals.refresh();
        UserManager.refresh();
        
        PNC.refresh();
        //QueueCounts.refresh();
        
    }
    
    public static void openCalculator() {
    	if(!hasBasicAccess()) { return; }
    	
        openCalculator(true);
    }
    
    public static boolean hasBasicAccess() {
    	if(isAccessRequired() && access.get() > 0) {
        	return true;
        }
    	
    	return false;
    }
    
    public static boolean hasAdminAccess() {
    	if(access.get() == 2) {
        	return true;
        }
    	
    	return false;
    }
    
    public static void openCRDChecker() {
    	if(!hasAdminAccess()) { return; }
        openCRDChecker(true);
    }
    
    private static void openCalculator(boolean b) {
        try {
            FXMLLoader loader = new FXMLLoader(AppManager.class.getClass().getResource("/fxml/Calculator.fxml"));
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
    
    private static void openCRDChecker(boolean b) {
        try {
            FXMLLoader loader = new FXMLLoader(AppManager.class.getClass().getResource("/fxml/CRD.fxml"));
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
    	str += "Cuts: " + QueueManager.getFilteredCuts().size() + "\n";
    	str += "SPs: " + QueueManager.spCount.get() + "\n";
    	str += "Total: " + QueueManager.spCount.get() + "\n";
    	
    	str += "\nBreakdown: \n";
    	str += "===============\n";
    	str += "Reels: " + QueueManager.reelCountProperty.get() + "\n";
    	str += "BW: " + QueueManager.bwCountProperty.get() + "\n";
    	str += "Cord: " + QueueManager.cordCountProperty.get() + "\n";
    	str += "Fiber: " + QueueManager.fiberCountProperty.get() + "\n";
    	str += "PV: " + QueueManager.pvCountProperty.get() + "\n";
    	
    	str += "\nPick: \n";
    	str += "===============\n";
    	str += "Case: " + PickQueueTotals.cases.get() + "\n";
    	str += "Pallet: " + PickQueueTotals.pallets.get() + "\n";
    	str += "UPS: " + PickQueueTotals.ups.get() + "\n";
    	str += "Grainger: " + PickQueueTotals.grainger.get() + "\n";
    	
    	
    	StringSelection selection = new StringSelection(str);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    	
    	
    }
    
    public static Image getIcon() {
    	Image image = null;
    	
    	try {
        	if(MainApp.IS_SNAPSHOT) {
        		image = new Image(MainApp.class.getResourceAsStream(ICON_FILE_SNAPSHOT));	
        	} else {
        		image = new Image(MainApp.class.getResourceAsStream(ICON_FILE));
        	}
        } catch (Throwable t) {
        	t.printStackTrace();
        }
    	
    	return image;
    }
    
    
    static public void loadTemplate(Pane parent, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(AppManager.class.getClass().getResource(fxmlFile));
            Parent root = null;
            root = loader.load();    
            Scene scene = new Scene(root);
            parent.getChildren().add(scene.getRoot());
        } catch (IOException ex) {
            ex.printStackTrace();
        } 
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
    
    public static StringProperty searchBoxProperty() {
    	return searchBoxTextProperty;
    }
    
    
    public static StringProperty userProperty() {
    	return user;
    }
    
    public static IntegerProperty accessProperty() {
    	return access;
    }
}
