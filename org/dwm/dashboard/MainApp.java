package org.dwm.dashboard;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApp extends Application {
	public static String version = "1.0.4"; 
	
	
	public static void main(String[] args) {
		
		if(!verifyLogin()) { System.exit(0); }
		launch(args);

	}
	
	private static boolean verifyLogin() {
		boolean b = false;
		
		String username = System.getProperty("user.name");
        int opt = QueryFunctions.getAccessForUser("OPTION");
        AppManager.loggedIn = true;
        
        if(opt == 1) {
            int access = QueryFunctions.getAccessForUser(username.toUpperCase());
        
            if(access != 0) {
            	b = true;
            }
        } else {
        	b = true;
        }
		
        return b;
	}

	@Override
	public void start(Stage mainStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LegacyDashboard.fxml"));
		Parent root = null;
		try {
		    root = loader.load();
		} catch (IOException ex) {
		    ex.printStackTrace();
		}
	        
		
        mainStage.setTitle("Dashboard v" + version);
        mainStage.setMaximized(true);
        
        mainStage.setScene(new Scene(root));
        //mainStage.initStyle(StageStyle.UNDECORATED);
                
        mainStage.getIcons().add(AppManager.getIcon());
        
        //mainStage.setX(bounds.getMinX());
        //mainStage.setY(bounds.getMinY());
        //mainStage.setWidth(bounds.getWidth());
        //mainStage.setHeight(bounds.getHeight());
        //AppManager.setMainStage(mainStage);
        
        mainStage.show();
        addRefreshTimer();
	}

	
	@Override
    public void stop(){
        System.exit(0);
    }

    public void addRefreshTimer() {
        //-----------------------------------
        //Auto-refresh timer
        //-----------------------------------
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                AppManager.refresh();
                
                //Queues.filterByPriorityRange(0, 99);
            }
        }, 0, 1800000);
    }
    
    public void addTimer() {
        //-----------------------------------
        //Auto-refresh timer
        //-----------------------------------
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    System.out.println("Record check"); //debugging
                    
                    try(ResultSet rs = QueryFunctions.query("SELECT QHSTS, QHAUSR FROM PROBASEF.WFWQH WHERE QHCTRL = 60285471")) {
                        while(rs.next()) {
                            if(!rs.getString(1).equals("A")) {
                                String strMsg = "NOT ACTIVE ANYMORE \n" + rs.getString(2) + "\n" + LocalDateTime.now().toString();
                                
                                Alert alert = new Alert(Alert.AlertType.INFORMATION, strMsg, ButtonType.OK);
                                alert.showAndWait();
                            }
                        }
                    } catch (SQLException ex) {
                        
                    }
                    
                    
                });
            }
        }, 0, 60000);
    }
}
