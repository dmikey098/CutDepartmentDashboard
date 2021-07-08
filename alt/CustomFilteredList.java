/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dwm.dashboard.alt;

import java.util.function.Predicate;

import org.dwm.dashboard.bean.DetailItem;

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
    public FilteredList<DetailItem> list;
    SortedList<DetailItem> sortedList;
    ObservableList<Predicate<DetailItem>> filters = FXCollections.observableArrayList();
    ObservableList<Predicate<DetailItem>> persistantFilters = FXCollections.observableArrayList();
    
    
    public CustomFilteredList(ObservableList<DetailItem> source) {
        list = new FilteredList<DetailItem>(source);
        list.predicateProperty().bind(Bindings.createObjectBinding(() -> filters.stream().reduce(x -> true, Predicate::and), filters));
        sortedList = new SortedList<DetailItem>(list);
    }
    
    public void addFilter(Predicate<DetailItem> pred) {
        filters.add(pred);
    }
    
    public void removeFilter(Predicate<DetailItem> pred) {
        filters.remove(pred);
    }
    
    public boolean removePersistantFilter(Predicate<DetailItem> pred) {
    	removeFilter(pred);
        return persistantFilters.remove(pred);
    }
    
    public void clear() {
        filters.clear();
        filters.addAll(persistantFilters);
    }
    
    public void addPersistantFilter(Predicate<DetailItem> pred) {
    	addFilter(pred);
        persistantFilters.add(pred);
    }
    
    public void clearPersistantFilters(){
        filters.removeAll(persistantFilters);
        persistantFilters.clear();
    }
    
    public FilteredList<DetailItem> filtered(Predicate<DetailItem> pred) {
        return new FilteredList<DetailItem>(list, pred);
    }
    
    public SortedList<DetailItem> sort() {
        return sortedList;
    }
}
