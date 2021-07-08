package org.dwm.dashboard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

public class LauncherUpdater {
	public static void update() {
		String strServerDir = "G:\\Clerical\\Cut Lead\\Tools\\Releases";
		String strLocalProps = getDir() + "\\local.properties";
		Properties sysProps = new Properties();
		Properties localProps = new Properties();
		
		if(Files.exists(Paths.get(strLocalProps))) {
			try {
				localProps.load(new FileInputStream(getDir() + "\\local.properties"));
			} catch (IOException ex) {
				ex.printStackTrace();
				return;
			}
		}
		
		if(Files.exists(Paths.get(strServerDir))) {
			try {
				sysProps.load(new FileInputStream(strServerDir + "\\launcher.properties"));
				
				if(sysProps.get("version") == localProps.get("launcher_version")) {
					return;
				}
				
				String strNewLauncher = strServerDir + "\\Launcher-" + sysProps.get("version") + ".jar"; 
				String strLauncher = getDir() + "\\Launcher.jar";
				
				copyFile(strNewLauncher, strLauncher);
				localProps.put("launcher_version", sysProps.get("version"));
			} catch (IOException ex) {
				ex.printStackTrace();
				return;
			}
		}		
		
		try {
			localProps.store(new FileOutputStream(strLocalProps), null);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	static void copyFile(String strFromFile, String strToFile) throws IOException {
		Files.copy(Paths.get(strFromFile), Paths.get(strToFile), StandardCopyOption.REPLACE_EXISTING);
	}
	
	static String getDir() {
        try {
            return new File(".").getCanonicalPath();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
}
