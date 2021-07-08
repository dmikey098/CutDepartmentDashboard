/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dwm.dashboard.controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.controlsfx.control.textfield.TextFields;
import org.dwm.dashboard.CableCalculator;
import org.dwm.dashboard.QueryFunctions;
import static org.dwm.dashboard.CableCalculator.calculateCapacity;
import static org.dwm.dashboard.CableCalculator.reels;

/**
 * FXML Controller class
 *
 * @author Daniel Mikesell
 */
public class CalculatorController implements Initializable {
    @FXML private TextField txtPartNumber;
    @FXML private TextField txtDiameter;
    @FXML private TextField txtQuantity;
    @FXML private Button btnCalculate;
    @FXML private Button btnCancel;
    @FXML private Button btnNext;
    @FXML private Button btnPrevious;
    @FXML private Label lblResult;
    @FXML private CheckBox chkSpecialty;
    @FXML private CheckBox chkTHHN;
    
    
    Map<String, String> pkgMap = new HashMap<>();
    HashMap<String, String> map = new HashMap<>();
    private double[][] capacities;
    private int index;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        txtPartNumber.textProperty().addListener((obs, oldValue, newValue) -> {
        	String pn = newValue.toUpperCase();
            
        	if(map.containsKey(pn)) {
        		txtDiameter.setText(map.get(pn));
        	} else {
        		txtDiameter.setText("");
        	}
        	
        	if(pkgMap.containsKey(pn)) {
	        	if(!pkgMap.get(pn).equals("1")) {
	            	txtQuantity.setText(pkgMap.get(pn));
	            }
        	} else {
        		txtDiameter.setText("");
        	}
        	
        	
        });
        
        btnCalculate.setOnKeyPressed((e) -> {
            if(e.getCode().equals(KeyCode.ENTER)) {
                calculate();
            }
        });
        
        btnNext.setOnKeyPressed((e) -> {
            if(e.getCode().equals(KeyCode.ENTER)) {
                nextReel();
            }
        });
        
        btnPrevious.setOnKeyPressed((e) -> {
            if(e.getCode().equals(KeyCode.ENTER)) {
                prevReel();
            }
        });
        
        chkTHHN.selectedProperty().addListener((obs, oldValue, newValue) -> {
            txtDiameter.setDisable(newValue);
        });
        
        chkSpecialty.setVisible(false);
        
        ArrayList<String> words = new ArrayList<>();
        
        String sqlStmt = ""
            + " SELECT "
            + "     TRIM(F1), F2, CSPKG"
            + " FROM "
            + "     BBQYXXX.CABLESIZE3"
            //+ " WHERE "
            //+ "     F1 LIKE '%X%'"
            //+ "     OR F1 LIKE '%U%'"
        	+ "";
                
        try (ResultSet rs = QueryFunctions.query(sqlStmt)) {
            while(rs.next()) {
                words.add(rs.getString(1));
                map.put(rs.getString(1), rs.getString(2));
                pkgMap.put(rs.getString(1), rs.getString(3));
            }
        } catch (SQLException ex) {}
        
        TextFields.bindAutoCompletion(txtPartNumber, words);
    }    
    
    public boolean validate() {
        boolean valid = true;
        
        if(txtPartNumber.getText().isEmpty() && chkTHHN.isSelected()) {
            lblResult.setText("Part number cannot be blank when sizing BW reel.");
            valid = false;
        } else if(txtQuantity.getText().isEmpty() || !isInteger(txtQuantity.getText())) {
            lblResult.setText("Quantity is required.");
            valid = false;
        } else if((txtDiameter.getText().isEmpty() || !isDouble(txtDiameter.getText())) && !chkTHHN.isSelected()) {
            lblResult.setText("Diameter is required for non-THHN reel sizing.");
            valid = false;
        } 
        
        return valid;
    }
    
    @FXML
    public void setDiameter() {
        String pn = txtPartNumber.getText().toUpperCase();
        txtDiameter.setText(map.get(pn));
    }
    
    
    @FXML
    private void nextReel() {
        if(capacities == null) 
            return;
        if(index < capacities.length - 1 && index != -1) {
            index++;
            setResultText();
        } 
    }
    
    @FXML 
    private void prevReel() {
        if(capacities == null) 
            return;
        if(index > 0) {
            index--;
            setResultText();
        } 
    }
    
    private void setResultText() {
        if(index >= 0 && index < capacities.length){
        
        lblResult.setText(
            Double.toString(capacities[index][0]) + " x " + 
            Double.toString(capacities[index][1]) + " x " + 
            Double.toString(capacities[index][2]) + "    Gap: " + 
            Double.toString(capacities[index][3]) + "    Capacity: " + 
            Double.toString(capacities[index][5]));
        }
    }
    
    private boolean isInteger(String strVal) {
        try {
            Integer.parseInt(strVal);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    
    private boolean isDouble(String strVal) {
        try {
            Double.parseDouble(strVal);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    
    @FXML
    private void calculate() {
        if(!validate()) { return; }
        String partNumber = "";
        String type = "";
        int qty = 0;
        double od = 0;
        
        if(chkTHHN.isSelected()) {
            partNumber = txtPartNumber.getText();
            type = "BW";
        } else {
            od = Double.parseDouble(txtDiameter.getText());
        }
        
        qty = Integer.parseInt(txtQuantity.getText());            
        setCapacityInfo(od, qty, type, partNumber);
        setResultText();
    }
    

    public void setCapacityInfo(double od, int qty, String type, String pn) {
        double gap = 0;
        double[][] arr = CableCalculator.reels;
        //double md = 0;
        index = -1;
        
        if(type.equals("Fiber")) {
            //md = Math.ceil(20 * od); 
        } 
        
        if(type.equals("BW")) {
            getBWReelSize(pn, qty);
        
        } else {
            for(int i = 0; i < reels.length; i++) {
                double[] reel = reels[i];

                if(od > 1) {
                    gap = Math.ceil(2 * od);
                } else {
                    gap = reel[3];
                }				

                int capacity = calculateCapacity(reel[0], reel[1], reel[2], gap, od);
                arr[i][5] = capacity;

                if(capacity >= qty && index == -1) {
                    index = i;
                }
            }
        
            this.capacities = arr;
        }
        
        
        if(index == -1) {
            lblResult.setText("Length will not fit on any known reel.");
        }
        
    }
    
    public void getBWReelSize(String pn, int qty) {
        HashMap<String, int[]> bwHm = new HashMap<>();

        bwHm.put("000", new int[]{0, 0, 0, 0, 	0, 	0, 	0, 	0, 	0, 	903, 	1284, 	1687});
        bwHm.put("750", new int[]{0, 0, 0, 0, 	0, 	0, 	0, 	581, 	0, 	1244, 	1692, 	2460});
        bwHm.put("600", new int[]{0, 0, 0, 0, 	0, 	437, 	630, 	782, 	1215, 	1636, 	2075, 	3053});
        bwHm.put("500", new int[]{0, 0, 0, 0, 	0, 	607, 	867, 	1008, 	1561, 	2078, 	2581, 	3706});
        bwHm.put("400", new int[]{0, 0, 0, 0, 	0, 	835, 	1101, 	1309, 	1873, 	2564, 	3133, 	4411});
        bwHm.put("350", new int[]{0, 0, 0, 0, 	0, 	872, 	1188, 	1361, 	2213, 	2714, 	3525, 	5044});
        bwHm.put("300", new int[]{0, 0, 0, 0, 	757, 	1101, 	1410, 	1649, 	2498, 	3183, 	3845, 	5861});
        bwHm.put("250", new int[]{0, 0, 0, 0, 	905, 	1182, 	1749, 	1767, 	2976, 	3778, 	4397, 	6739});
        bwHm.put("410", new int[]{0, 0, 535, 877, 	1145, 	1486, 	1909, 	2160, 	3497, 	4624, 	5741, 	8005});
        bwHm.put("310", new int[]{0, 0, 682, 1096, 	1414, 	1874, 	2356, 	2664, 	4440, 	5445, 	6694, 	9741});
        bwHm.put("210", new int[]{0, 0, 876, 1339, 	1711, 	2251, 	2851, 	3142, 	5189, 	6451, 	8171, 	12048});
        bwHm.put("110", new int[]{484, 704, 	1093, 	1607, 	2036, 	2724, 	3676, 	4032, 	6327, 	8042, 	9969, 	14376});
        bwHm.put("001", new int[]{518, 754, 	1196, 	1899, 	2389, 	3242, 	4288, 	4712, 	7215, 	9257, 	11402, 	17153});
        bwHm.put("002", new int[]{706, 1100, 	1670, 	2613, 	3252, 	4413, 	5749, 	6231, 	10157, 	12734, 	15800, 	23157});
        bwHm.put("003", new int[]{875, 1313, 	2004, 	3043, 	3770, 	5150, 	6998, 	7586, 	11905, 	15297, 	18506, 	27261});
        bwHm.put("004", new int[]{1089, 1583, 	2369, 	3506, 	4580, 	5944, 	8058, 	9071, 	13986, 	18096, 	22431, 	32346});
        bwHm.put("006", new int[]{1679, 2498, 	3937, 	5795, 	7050, 	9929, 	12865, 	14294, 	22728, 	28651, 	36192, 	52317});
        bwHm.put("008", new int[]{2321, 3311, 	5390, 	7791, 	9802, 	13239, 	18278, 	19884, 	31178, 	39291, 	48957, 	71543});

        double[][] bwReels = {
            {12, 12, 5, 1, 0, 0},
            {16, 14, 8, 1, 0, 0},
            {18, 14, 8, 1, 0, 0},
            {21, 16, 10, 1, 0, 0},
            {24, 18, 10, 1.5, 0, 0},
            {27, 18, 12, 1.5, 0, 0},
            {30, 22, 16, 1.5, 0, 0},
            {32, 24, 16, 1.5, 0, 0},
            {36, 22, 18, 2, 0, 0},
            {40, 24, 17, 2, 0, 0},
            {44, 30, 24, 2, 0, 0},
            {48, 28, 24, 2, 0, 0},
            {54, 32, 28, 2, 0, 0},
            {60, 36, 24, 2.5, 0, 0}
        };

        String awg = "";
        
        if(pn.length() == 8) {
            awg = pn.substring(2, 5);
        } else {
            awg = pn.substring(5, 8);
        }

        if(bwHm.containsKey(awg)) {
            int[] arr = bwHm.get(awg);

            for(int i = 0; i < arr.length; i++) {
                System.out.println(arr[i]);
                int capacity = arr[i];
                bwReels[i][5] = capacity;
                if(qty <= capacity && index == -1) {
                    index = i;
                }
            }

            capacities = bwReels;
        }
    }
}
