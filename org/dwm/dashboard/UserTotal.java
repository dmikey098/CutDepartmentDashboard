/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dwm.dashboard;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Daniel Mikesell
 */
public class UserTotal {
    StringProperty team = new SimpleStringProperty();
    StringProperty name = new SimpleStringProperty();
    IntegerProperty total = new SimpleIntegerProperty();
    
    public StringProperty teamProperty() {
        return team;
    }
    
    public StringProperty nameProperty() {
        return name;
    }
    
    public IntegerProperty totalProperty() {
        return total;
    }
}
