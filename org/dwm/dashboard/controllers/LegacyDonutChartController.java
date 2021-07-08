/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dwm.dashboard.controllers;

import com.sun.javafx.tk.Toolkit;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Arc;
import org.dwm.dashboard.AppManager;
import org.dwm.dashboard.QueueManager;


/**
 * FXML Controller class
 *
 * @author mikesda001
 */
public class LegacyDonutChartController implements Initializable {
    @FXML private Arc arcBW;
    @FXML private Arc arcCord;
    @FXML private Arc arcFiber;
    @FXML private Arc arcPV;
    @FXML private Arc arcSP;
    Label bwLabel = new Label("BW");
    Label cordLabel = new Label("");
    Label fiberLabel = new Label("");
    Label pvLabel = new Label("");
    Label spLabel = new Label("");
    @FXML private AnchorPane root;
    //@FXML private Label lblCount;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //lblCount.textProperty().bind(Queues.totalProperty.asString());
        arcBW.lengthProperty().bind(getBinding(QueueManager.bwCountProperty));
        arcCord.lengthProperty().bind(getBinding(QueueManager.cordCountProperty));
        arcFiber.lengthProperty().bind(getBinding(QueueManager.fiberCountProperty));
        arcPV.lengthProperty().bind(getBinding(QueueManager.pvCountProperty));
        arcSP.lengthProperty().bind(Bindings.createDoubleBinding(() -> {
            if(QueueManager.cutCount.get() != 0) {
                return QueueManager.spCount.doubleValue() / (QueueManager.cutCount.doubleValue() + QueueManager.spCount.doubleValue()) * 360.00;
            }            
            return 360.00;
        }, QueueManager.spCount, QueueManager.cutCount));
        
        bwLabel.textProperty().bind(QueueManager.bwCountProperty.asString());
        cordLabel.textProperty().bind(QueueManager.cordCountProperty.asString());
        fiberLabel.textProperty().bind(QueueManager.fiberCountProperty.asString());
        pvLabel.textProperty().bind(QueueManager.pvCountProperty.asString());
        //spLabel.textProperty().bind(Queues.spCount.asString());
        
        arcBW.setStartAngle(0.0);
        arcCord.startAngleProperty().bind(arcBW.lengthProperty());
        arcFiber.startAngleProperty().bind(arcBW.lengthProperty().add(arcCord.lengthProperty()));
        arcPV.startAngleProperty().bind(arcBW.lengthProperty().add(arcCord.lengthProperty()).add(arcFiber.lengthProperty()));
        arcSP.startAngleProperty().bind(arcBW.lengthProperty().add(arcCord.lengthProperty()).add(arcFiber.lengthProperty()).add(arcPV.lengthProperty()));
        
        bwLabel.setVisible(false);
        cordLabel.setVisible(false);
        pvLabel.setVisible(false);
        fiberLabel.setVisible(false);
        
        /*
        Queues.bwCountProperty.addListener((obs, oldValue, newValue) -> {
            bwLabel.setVisible(false);
            if(newValue.intValue() != 0) {
                bwLabel.setVisible(true);
                setLabelPosition(arcBW, bwLabel);
            }
        });
        
        Queues.cordCountProperty.addListener((obs, oldValue, newValue) -> {
            cordLabel.setVisible(false);
            if(newValue.intValue() != 0) {
                cordLabel.setVisible(true);
                setLabelPosition(arcCord, cordLabel);
            }
        });
        
        Queues.fiberCountProperty.addListener((obs, oldValue, newValue) -> {
            fiberLabel.setVisible(false);
            if(newValue.intValue() != 0) {
                fiberLabel.setVisible(true);
                setLabelPosition(arcFiber, fiberLabel);
            }
        });
        
        Queues.pvCountProperty.addListener((obs, oldValue, newValue) -> {
            pvLabel.setVisible(false);
            if(newValue.intValue() != 0) {
                pvLabel.setVisible(true);
                setLabelPosition(arcPV, pvLabel);
            }
        });
        */
        //Queues.spCount.addListener((obs, oldValue, newValue) -> {
          //  setLabelPosition(arcSP, spLabel);
        //});
        
        
        
        root.getChildren().add(bwLabel);
        root.getChildren().add(cordLabel);
        root.getChildren().add(fiberLabel);
        root.getChildren().add(pvLabel);
        root.getChildren().add(spLabel);
        
        redrawLabels();
    }    
    
    public DoubleBinding getBinding(IntegerProperty intProp) {
        return Bindings.createDoubleBinding(() -> {
           if(QueueManager.cutCount.get() != 0) {
                return intProp.doubleValue() / (QueueManager.getFilteredCuts().size()) * 360.00;
            }            
            return 0.0;
        }, QueueManager.cutCount);
    }     

    @FXML
    private void filterBW(MouseEvent event) {
        filterCuts();
        QueueManager.applyFilter(QueueManager.CUT, item -> (item.type.get().equals("BW")));
    }
    

    @FXML
    private void filterCord(MouseEvent event) {
        filterCuts();
        QueueManager.applyFilter(QueueManager.CUT, item -> (item.type.get().equals("CORD")));
    }

    @FXML
    private void filterFiber(MouseEvent event) {
        filterCuts();
        QueueManager.applyFilter(QueueManager.CUT, item -> (item.type.get().equals("FIBER")));
    }

    @FXML
    private void filterPV(MouseEvent event) {
        filterCuts();
        QueueManager.applyFilter(QueueManager.CUT, item -> (item.type.get().equals("PV")));
    }

    @FXML
    private void filterSPs(MouseEvent event) {
        QueueManager.applyFilter(QueueManager.CUT, item -> (item.onHandQuantity.get() - item.orderQuantity.get() <= AppManager.getStraightPullQuantity()));
    }
    
    private void filterCuts() {
        QueueManager.applyFilter(QueueManager.CUT, item -> (item.onHandQuantity.get() - item.orderQuantity.get() > AppManager.getStraightPullQuantity()));
    }
    
    public double cos(double angle) {
        return Math.round(Math.cos(Math.toRadians(angle)) * 100.0) / 100.0;
    }
    
    public double sin(double angle) {
        return Math.round(Math.sin(Math.toRadians(angle)) * 100.0) / 100.0;
    }
    
    public void redrawLabels() {
        
        
    }
    
    public void setLabelPosition(Arc arc, Label label) {
            
        
        int originX = (int) arc.getCenterX();
        int originY = (int) arc.getCenterY();
        int labelWidth = (int) Toolkit.getToolkit().getFontLoader().computeStringWidth(label.getText(), label.getFont());        
        int labelHeight = 20;
        int i = 6;
        
        double angle = Math.round(arc.getStartAngle() + 0.5 * arc.getLength());//270.0;


        if(arc.lengthProperty().get() == 0) {
            label.setVisible(false);
        } else {
            label.setVisible(true);
        }

        double calcX = (arc.getRadiusX() + i) * cos(angle);
        double calcY = (arc.getRadiusY() + i) * sin(angle);

        double x = originX + calcX;
        double y = originY - calcY;

        if(angle <= 180) {
            y -= labelHeight;
        }

        if(angle > 90 && angle < 270) {
            x -= labelWidth;
        }

        label.setLayoutX(x);
        label.setLayoutY(y);
    }
    
}

