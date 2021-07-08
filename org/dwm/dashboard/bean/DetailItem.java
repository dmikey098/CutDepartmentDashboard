package org.dwm.dashboard.bean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.dwm.dashboard.AppManager;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DetailItem {
	public DoubleProperty cableDiameter = new SimpleDoubleProperty(0.0);
    public IntegerProperty priority = new SimpleIntegerProperty(0);
    public IntegerProperty orderQuantity = new SimpleIntegerProperty(0);
    public IntegerProperty onHandQuantity = new SimpleIntegerProperty(0);
    public IntegerProperty wave = new SimpleIntegerProperty(0);
    public IntegerProperty line = new SimpleIntegerProperty(0);
    public StringProperty orderNumber = new SimpleStringProperty("");
    public StringProperty licensePlate = new SimpleStringProperty("");
    public StringProperty partNumber = new SimpleStringProperty("");
    public StringProperty carrier = new SimpleStringProperty("");
    public StringProperty status = new SimpleStringProperty("");
    public StringProperty detailStatus = new SimpleStringProperty("");
    public StringProperty user = new SimpleStringProperty("");
    public StringProperty type = new SimpleStringProperty("");
    public StringProperty customer = new SimpleStringProperty("");
    public StringProperty reel = new SimpleStringProperty("");
    public StringProperty shipOnDate = new SimpleStringProperty("");
    public StringProperty customerId = new SimpleStringProperty("");
    public StringProperty ctrlNum = new SimpleStringProperty("");
    public StringProperty parcelNum = new SimpleStringProperty("");
    public StringProperty asle = new SimpleStringProperty("");
    public StringProperty bay = new SimpleStringProperty("");
    public StringProperty level = new SimpleStringProperty("");
    public StringProperty controlNumber = new SimpleStringProperty("");
    public IntegerProperty casePack = new SimpleIntegerProperty(0);
    public StringProperty wlu = new SimpleStringProperty("");
    public IntegerProperty availStandardLength = new SimpleIntegerProperty(0);
    public StringProperty dockArea = new SimpleStringProperty("");
    public DoubleProperty weight = new SimpleDoubleProperty(0.0);
    public IntegerProperty palletQuantity = new SimpleIntegerProperty(0);
    public Pallet parent = null;
    
    public final int PRIORITY = 0;
    public final int ORDER_NUMBER = 1;
    public final int LICENSE_PLATE = 2;
    
    
    
    public boolean isStraightPull() {
    	int qty = this.onHandQuantity.get() - this.orderQuantity.get();
    	
    	
    	
    	if(qty <= AppManager.getStraightPullQuantity()) {
    		return true;
    	}
    	
    	return false;
    }
    
    public boolean isTruckload() {
    	try {
    		int door = Integer.parseInt(dockArea.get().substring(1, 3));
    		
	    	if(door <= 3 || door >= 16) {
	    		return true;
	    	}
    	} catch (NumberFormatException ex) {
    	}
    	
    	return false;
    }
    
    public boolean isUPS() {
    	if(carrier.get().contains("UPS")) {
    		return true;
    	}
    	
    	return false;
    }
    
    public boolean isDoubleStack() {
    	if(orderQuantity.get() == 2 && level.get().equals("004")) {
    		return true;
    	}
    	
    	return false;
    }
    
    public boolean isPartialPallet() {
    	if(orderQuantity.get() < onHandQuantity.get() && orderQuantity.get() != palletQuantity.get()) {
    		return true;
    	}
    	
    	return false;
    }
    
    public boolean isCradle() {
    	/*
        String reel = this.reel.get().substring(0, 2);
        
        if(reel.equals("44") || reel.equals("45") || reel.equals("48") || reel.equals("50") || 
            reel.equals("54") || reel.equals("58") || reel.equals("60") || reel.equals("72")) {
            return true;
        }
        
        if((reel.equals("36") || reel.equals("40") || reel.equals("42")) && type.get().equals("BW")) {
            return true;
        }
        
        */
    	
    	if(weight.get() >= AppManager.CRADLE_WEIGHT && level.get().equals("001") && orderQuantity.get() == 1) {    		
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
    
    
    public int getPriority() {
    	return priority.get();
    }
    
    public int getOrderQuantity() {
    	return orderQuantity.get();
    }
    
    public String getOrderNumber() {
    	return orderNumber.get();
    }
    
    public String getType() {
    	return type.get();
    }
    
    public String getControlNumber() {
    	return controlNumber.get();
    }
    
    public String getPropertyAsString(int intVal) {
    	String str = null;
    	
    	switch(intVal) {
    		case PRIORITY:
    			str = this.priority.toString();
    			break;
    		case 1:
    			str = this.orderNumber.toString();
    			break;
    		case LICENSE_PLATE:
    			str = this.licensePlate.toString();
    			break;
    		default:
    	}
    	
    	return str;
    }
}
