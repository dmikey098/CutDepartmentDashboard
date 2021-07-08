/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dwm.dashboard;

import java.awt.Color;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.dwm.dashboard.bean.QueueItem;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author mikesda001
 */
public class Utility {
    
    public static boolean isNumeric(String strValue) {
        boolean b = true;
        
        try {
            Integer.parseInt(strValue);
        } catch(NumberFormatException ignore) {
            b = false;
        }
        return b;
    }
    
    public static int getReelValue(QueueItem item) {
        int n = 0;
        String strReel = item.reel.get().substring(0, 2);
        if(isNumeric(strReel)) 
           n = Integer.parseInt(strReel);
        return n;
    }
    
    public static String getReelSize(QueueItem item, boolean standard) {
        return CableCalculator.getReelSize(item.cableDiameter.get(), item.casePack.get(), item.type.get(), item.partNumber.get());
    }
    
    public static String getReelSize(QueueItem item) {
        return CableCalculator.getReelSize(item.cableDiameter.get(), item.orderQuantity.get(), item.type.get(), item.partNumber.get());
    }
    
    public static LocalDate getDate(String strDte) {
        int intYear = Integer.parseInt("20" + strDte.substring(1, 3));
        int intMonth = Integer.parseInt(strDte.substring(3, 5));
        int intDay = Integer.parseInt(strDte.substring(5, 7));

        return LocalDate.of(intYear, intMonth, intDay);
    }
    
    static public String generateRandomColor() {
        Random rand = new Random();
        float r = (float) (rand.nextFloat() / 2f + 0.5);
        float g = (float) (rand.nextFloat() / 2f + 0.5);
        float b = (float) (rand.nextFloat() / 2f + 0.5);
        Color color = new Color(r, g, b);
        
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());  
    }
    
    static public void generateColorList() {
        for(int i = 0; i <= 100; i++) {
            System.out.println(
                ".dup" + i + " { -fx-background-color: " +    
                generateRandomColor() +
                "; }"
            );
        }
    }
    
    public static  void setStringProperty(StringProperty prop, String str) {
        prop.unbind();
        prop.set(str);
    }
    
    public static void bindStringProperty(StringProperty prop, ObservableValue<? extends String> obs) {
        prop.unbind();
        prop.bind(obs);
    }
    
    public static String getRFFromSerialNo(String serialNumber) {
    	Map<String, String> rfs = new HashMap<>();
    	rfs.put("NOLBSYM077", "P01");
    	rfs.put("NOLBSYM076", "P02");
    	rfs.put("NOLBSYM074", "P03");
    	rfs.put("NOLBSYM036", "P04");
    	rfs.put("NOLBSYM084", "P05");
    	rfs.put("NOLBSYM019", "P06");
    	rfs.put("NOLBSYM009", "P07");
    	rfs.put("NOLBSYM078", "P08");
    	rfs.put("NOLBSYM073", "P09");
    	rfs.put("NOLBSYM033", "P10");
    	rfs.put("NOLBSYM081", "P11");
    	
    	rfs.put("NOLBSYM087", "R01");
    	rfs.put("NOLBSYM057", "R02");
    	rfs.put("NOLBSYM071", "R03");
    	rfs.put("NOLBSYM003", "R04"); //rfs.put("NOLBSYM075", "R04"); 
    	rfs.put("NOLBSYM043", "R05");
    	rfs.put("NOLBSYM063", "R06");
    	rfs.put("NOLBSYM080", "R07");
    	rfs.put("NOLBSYM064", "R08");
    	rfs.put("NOLBSYM023", "R09");
    	rfs.put("NOLBSYM061", "C04");
    	
    	rfs.put("NOLBSYM085", "S01");
    	rfs.put("NOLBSYM018", "S02");
    	rfs.put("NOLBSYM022", "S03");
    	rfs.put("NOLBSYM056", "S04");
    	
    	rfs.put("NOLBSYM066", "M01");
    	rfs.put("NOLBSYM034", "M02");
    	rfs.put("NOLBSYM086", "M03");
    	rfs.put("NOLBSYM059", "M04");
    	rfs.put("NOLBSYM070", "M05");
    	rfs.put("NOLBSYM079", "M06");
    	
    	rfs.put("NOLBSYM029", "UPS");
    	
    	
    	rfs.put("NOLBSYM0", "");
    	
    	
    	if(!rfs.containsKey(serialNumber)) {
    		return "";
    	}
    	
    	return rfs.get(serialNumber);
    }
}
