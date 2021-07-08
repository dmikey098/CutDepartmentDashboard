package org.dwm.dashboard;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
	public static final String VERSION = "1.0.13"; 
	public static final String IN_DEV_VERSION = "1.0.13-A001";
	public static final boolean IS_SNAPSHOT = true;
	
	public static void main(String[] args) {
		if(!verifyLogin()) { System.exit(0); }
		launch(args);
	}
	
	private static boolean verifyLogin() {
		String username = System.getProperty("user.name");
        int opt = QueryFunctions.getAccessForUser("OPTION");
        AppManager.loggedIn = true;
        AppManager.userProperty().set(username.toUpperCase());
        
        
        int access = QueryFunctions.getAccessForUser(username.toUpperCase());
        AppManager.accessProperty().set(access);
        
        if(opt == 0) {
        	AppManager.accessRequiredProperty().set(false);
        }
        
        /*
        System.out.println("User: " + username);
        System.out.println("Access: " + access);
        System.out.println("Opt: " + opt);
        System.out.println("isAccessRequired: " + AppManager.isAccessRequired());
        */
        
        return true;
	}

	@Override
	public void start(Stage mainStage) throws Exception {
		
		AppManager.setMainStage(mainStage);
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LegacyDashboard.fxml"));
			Parent root = null;
			try {
			    root = loader.load();
			} catch (IOException ex) {
			    ex.printStackTrace();
			}
		        
			String title = "Dashboard v" + VERSION; 
			title += (IS_SNAPSHOT) ? "-SNAPSHOT" : "";
	        mainStage.setTitle(title);
	        mainStage.setScene(new Scene(root));
	        mainStage.getIcons().add(AppManager.getIcon());
	        
	        mainStage.setMaximized(true);
	        mainStage.setMinHeight(600);
	        mainStage.setMinWidth(860);
	        
	        
	        
	        mainStage.show();
        } catch (Throwable t) {
        	t.printStackTrace();
        }
		
        //Update launcher
        //new Thread(() -> {
        //	LauncherUpdater.update();
		//}).start();
        

		/*
		Listener l = new Listener("A", "SELECT QHSTS FROM PROBASEF.WFWQH WHERE QHCTRL = 60984701");
		l.getBool().addListener((obs, oldValue, newValue) -> {
			JOptionPane.showMessageDialog(null, "The status has changed...");
		});
		l.start();
		*/
		
    }

	
	@Override
    public void stop(){
		System.exit(0);
    }

    class Listener extends Thread {
    	String oldValue = "";
    	String sqlStmt = "";
    	BooleanProperty b = new SimpleBooleanProperty(false);
    	
    	Listener(String oldValue, String sqlStmt) {
    		this.oldValue = oldValue;
    		this.sqlStmt = sqlStmt;
    	}
    	
    	public BooleanProperty getBool() {
    		return b;
    	}

    	public void run() {
    		while (true) {
    			try (ResultSet rs = QueryFunctions.query(sqlStmt)) {
    				rs.next();
    				if(!rs.getString(1).equals(oldValue)) {
    					b.set(true);
    					return;
    				}
    				
    				Thread.sleep(500);
    			} catch (SQLException sqle) {
    				sqle.printStackTrace();
    			} catch (InterruptedException ie) {
    				ie.printStackTrace();
    			}
    		}
    	}

    }
}
