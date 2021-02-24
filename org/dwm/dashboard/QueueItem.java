/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dwm.dashboard;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Daniel Mikesell
 */
public class QueueItem {
    public DoubleProperty cableDiameter = new SimpleDoubleProperty();
    public IntegerProperty priority = new SimpleIntegerProperty();
    public IntegerProperty orderQuantity = new SimpleIntegerProperty();
    public IntegerProperty onHandQuantity = new SimpleIntegerProperty();
    public IntegerProperty wave = new SimpleIntegerProperty();
    public IntegerProperty line = new SimpleIntegerProperty();
    public StringProperty orderNumber = new SimpleStringProperty();
    public StringProperty licensePlate = new SimpleStringProperty();
    public StringProperty partNumber = new SimpleStringProperty();
    public StringProperty carrier = new SimpleStringProperty();
    public StringProperty status = new SimpleStringProperty();
    public StringProperty user = new SimpleStringProperty();
    public StringProperty type = new SimpleStringProperty();
    public StringProperty customer = new SimpleStringProperty();
    public StringProperty reel = new SimpleStringProperty();
    public StringProperty shipOnDate = new SimpleStringProperty();
    public StringProperty customerId = new SimpleStringProperty();
    public StringProperty ctrlNum = new SimpleStringProperty();
    public StringProperty parcelNum = new SimpleStringProperty();
    public StringProperty asle = new SimpleStringProperty();
    public StringProperty bay = new SimpleStringProperty();
    public StringProperty level = new SimpleStringProperty();
    public StringProperty controlNumber = new SimpleStringProperty();
    public IntegerProperty casePack = new SimpleIntegerProperty();
    public StringProperty wlu = new SimpleStringProperty();
    
    
    public boolean isCradle() {
        String reel = this.reel.get().substring(0, 2);
        
        if(reel.equals("44") || reel.equals("45") || reel.equals("48") || reel.equals("50") || 
            reel.equals("54") || reel.equals("58") || reel.equals("60") || reel.equals("72")) {
            return true;
        }
        
        if((reel.equals("36") || reel.equals("40") || reel.equals("42")) && type.get().equals("BW")) {
            return true;
        }
        
        return false;
    }

    public void print() {
        for(Field field : this.getClass().getDeclaredFields()) {
            try {
                System.out.println(field.getName() + " = " + field.get(this));
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(QueueItem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public ArrayList<String> getColumnNames() {
        ArrayList<String> columns = new ArrayList<>();
    
        columns.add("Priority");
        columns.add("orderNumber");
        columns.add("licensePlate");
        columns.add("partNumber");
        columns.add("orderQuantity");
        columns.add("onHandQuantity");
        columns.add("wave");
        columns.add("carrier");
        columns.add("status");
        columns.add("user");
        columns.add("type");
        columns.add("customer");
        columns.add("reel");
        columns.add("cableDiameter");
        columns.add("line");
        columns.add("shipOnDate");
        columns.add("customerId");
        columns.add("ctrlNum");
        columns.add("parcelNum");
        columns.add("asle");
        columns.add("bay");
        columns.add("level");
        columns.add("controlNumber");
    
        return columns;
    }
    
    public ArrayList<String> getColumns() {
        ArrayList<String> columns = new ArrayList<>();
        
        columns.add(Integer.toString(priority.get()));
        columns.add(orderNumber.get());
        columns.add(licensePlate.get());
        columns.add(partNumber.get());
        columns.add(Integer.toString(orderQuantity.get()));
        columns.add(Integer.toString(onHandQuantity.get()));
        columns.add(Integer.toString(wave.get()));
        columns.add(carrier.get());
        columns.add(status.get());
        columns.add(user.get());
        columns.add(type.get());
        columns.add(customer.get());
        columns.add(reel.get());
        columns.add(Double.toString(cableDiameter.get()));
        columns.add(Integer.toString(line.get()));
        columns.add(shipOnDate.get());
        columns.add(customerId.get());
        columns.add(ctrlNum.get());
        columns.add(parcelNum.get());
        columns.add(asle.get());
        columns.add(bay.get());
        columns.add(level.get());
        columns.add(controlNumber.get());
        
        return columns;
    }
}
