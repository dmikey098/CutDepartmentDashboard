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

public class MainTesting extends Application {
	public static final String VERSION = "1.0.12"; 
	public static final String IN_DEV_VERSION = "1.0.12-A005";
	public static final boolean IS_SNAPSHOT = false;
	
	public static void main(String[] args) {
		
		launch(args);
	}
	
	

	@Override
	public void start(Stage mainStage) throws Exception {
		/*
		AppManager.setMainStage(mainStage);
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UtilityTab.fxml"));
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
	        //mainStage.getIcons().add(AppManager.getIcon());
	        
	        mainStage.setMaximized(true);
	        mainStage.setMinHeight(600);
	        mainStage.setMinWidth(860);
	        
	        
	        
	        mainStage.show();
        } catch (Throwable t) {
        	t.printStackTrace();
        }
        */
		
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


}
