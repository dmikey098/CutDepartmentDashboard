/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dwm.dashboard;

import javafx.collections.ObservableList;

/**
 *
 * @author mikesda001
 */
public interface Queue {
       
    public ObservableList<QueueItem> getAllItems();
}
