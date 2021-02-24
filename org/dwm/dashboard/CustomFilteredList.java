/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dwm.dashboard;

import java.util.function.Predicate;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;


/**
 *
 * @author Daniel Mikesell
 */
public class CustomFilteredList {
    FilteredList<QueueItem> list;
    SortedList<QueueItem> sortedList;
    ObservableList<Predicate<QueueItem>> filters = FXCollections.observableArrayList();
    ObservableList<Predicate<QueueItem>> persistantFilters = FXCollections.observableArrayList();
    
    
    public CustomFilteredList(ObservableList<QueueItem> source) {
        list = new FilteredList<QueueItem>(source);
        list.predicateProperty().bind(Bindings.createObjectBinding(() -> filters.stream().reduce(x -> true, Predicate::and), filters));
        sortedList = new SortedList<QueueItem>(list);
    }
    
    public void addFilter(Predicate<QueueItem> pred) {
        filters.add(pred);
    }
    
    public void removeFilter(Predicate<QueueItem> pred) {
        filters.remove(pred);
    }
    
    public void removePersistantFilter(Predicate<QueueItem> pred) {
        persistantFilters.remove(pred);
    }
    
    public void clear() {
        filters.clear();
        filters.addAll(persistantFilters);
    }
    
    public void addPersistantFilter(Predicate<QueueItem> pred) {
        addFilter(pred);
        persistantFilters.add(pred);
    }
    
    public void clearPersistantFilters(){
        filters.removeAll(persistantFilters);
        persistantFilters.clear();
    }
    
    public FilteredList<QueueItem> filtered(Predicate<QueueItem> pred) {
        return new FilteredList<QueueItem>(list, pred);
    }
    
    public SortedList<QueueItem> sort() {
        return sortedList;
    }
}
