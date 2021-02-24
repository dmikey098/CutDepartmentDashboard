/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dwm.dashboard;

import java.awt.Color;
import java.time.LocalDate;
import java.util.Random;
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
}
