/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dwm.dashboard;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumn;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns;

/**
 *
 * @author mikesda001
 */
public class ExcelWriter {
    private String strFile;
    private int intSheet;
    ArrayList<HashMap<String, Object>> storedList;
    
    public ExcelWriter(String strFile) {
        
        this.strFile = strFile;
        this.intSheet = 0;
    }
    
    public void setSheet(int sheet) {
        this.intSheet = sheet;
    }
    
    public boolean singleWrite( String strCell, Object objValue) {
        boolean retVal = false;
        
        try (InputStream inp = new FileInputStream(strFile)) {
            XSSFWorkbook wb = XSSFWorkbookFactory.createWorkbook(inp);
            XSSFSheet sheet = wb.getSheetAt(intSheet);
            
            Integer[] coords = getCoordsFromReference(strCell);
            int colNum = coords[0];
            int rowNum = coords[1];
            
            if(rowNum < 0 || colNum < 0) {
                throw new Exception();
            }
            
            //If row is doesn't exist create it
            XSSFRow row = sheet.getRow(rowNum);
            if(row == null) {
                row = sheet.createRow(rowNum);
            }
            
            //If cell is null create it
            XSSFCell cell = row.getCell(colNum);
            if(cell == null) {
                cell = row.createCell(colNum);
            }
            
            //Set cell value based on objValue class
            if(objValue instanceof String) {
                cell.setCellValue((String) objValue);
            } else if(objValue instanceof Integer) {
                cell.setCellValue((int) objValue);
            } else if(objValue instanceof Double) {
                cell.setCellValue((double) objValue);
            } else if(objValue instanceof Date) {
                cell.setCellValue((Date) objValue);
            }
            
            try (OutputStream fileOut = new FileOutputStream(strFile)) {
                wb.write(fileOut);
            } catch(java.io.FileNotFoundException ex2) {
                Logger.getLogger(ExcelWriter.class.getName()).log(Level.SEVERE, null, ex2);
                return false;
            }  
        } catch(Exception ex) {
            Logger.getLogger(ExcelWriter.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }     
        
        retVal = true;
        return retVal;
    }
    
    public void add(String strCell, Object objValue) {
        if(storedList == null) {
            storedList = new ArrayList<>();
        }
        
        HashMap<String, Object> map = new HashMap<>();
        map.put("reference", strCell);
        map.put("value", objValue);
        storedList.add(map);
    }
    
    public boolean multiWrite() {
        if(storedList != null) {
            return multiWrite(storedList);
        }
        return false;
    }
    
    public boolean multiWrite(ArrayList<HashMap<String, Object>> list) {
        boolean retVal = false;
        
        try (InputStream inp = new FileInputStream(strFile)) {
            XSSFWorkbook wb = XSSFWorkbookFactory.createWorkbook(inp);
            XSSFSheet sheet = wb.getSheetAt(intSheet);
            
            for(HashMap<String, Object> map : list) {
                String strCell = (String) map.get("reference");
                Object objValue = map.get("value");
                
                Integer[] coords = getCoordsFromReference(strCell);
                int colNum = coords[0];
                int rowNum = coords[1];
                
                if(rowNum < 0 || colNum < 0) {
                    throw new Exception();
                }

                //If row is doesn't exist create it
                XSSFRow row = sheet.getRow(rowNum);
                if(row == null) {
                    row = sheet.createRow(rowNum);
                }

                //If cell is null create it
                XSSFCell cell = row.getCell(colNum);
                if(cell == null) {
                    cell = row.createCell(colNum);
                }

                //Set cell value based on objValue class
                if(objValue instanceof String) {
                    cell.setCellValue((String) objValue);
                } else if(objValue instanceof Integer) {
                    cell.setCellValue((int) objValue);
                } else if(objValue instanceof Double) {
                    cell.setCellValue((double) objValue);
                } else if(objValue instanceof Date) {
                    cell.setCellValue((Date) objValue);
                }
            }
            
            try (OutputStream fileOut = new FileOutputStream(strFile)) {
                wb.write(fileOut);
            } catch(java.io.FileNotFoundException ex2) {
                Logger.getLogger(ExcelWriter.class.getName()).log(Level.SEVERE, null, ex2);
                return false;
            }  
        } catch(Exception ex) {
            Logger.getLogger(ExcelWriter.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }     
        
        retVal = true;
        return retVal;
        
        
    }
    
    public static Integer[] getCoordsFromReference(String str) {
        Integer[] coords = {-1, -1};
        
        //Set column 
        str = str.toUpperCase();
        char c = str.charAt(0);
        coords[0] = ((int) c) - 65;
        
        //Set row
        try {
            coords[1] = Integer.parseInt(str.substring(1, str.length())) - 1;
        } catch(NumberFormatException ex) {
            Logger.getLogger(ExcelWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return coords;
    }

    public static void WriteTable(File file, ArrayList<ArrayList<String>> rows, String[] colNames, boolean open) {
        try (
            XSSFWorkbook wb = new XSSFWorkbook();
            OutputStream fileOut = new FileOutputStream(file);
        ) {
            int rowNum = 0;
            int cellNum = 0;
            int colCount = colNames.length;
            
            int div = Math.floorDiv(colCount, 26);
            int mod = Math.floorMod(colCount, 26);
            
            String strCol0 = (div > 0) ? Character.toString((char) (div + 64)) : "";
            String strCol = Character.toString((char) (mod + 64));
                        
            strCol = strCol0 + strCol;
            
            String strRow = Integer.toString(rows.size() + 1);

            XSSFSheet sheet1 = wb.createSheet("Query Results");
            XSSFTable table = sheet1.createTable(null);
            XSSFRow xlRow = sheet1.createRow(rowNum++);

            CTTable ctTable = table.getCTTable();
            ctTable.setDisplayName("Table1");
            ctTable.setId(1);
            ctTable.setName("Test");
            ctTable.setRef("A1:" + strCol + strRow);
            ctTable.setTotalsRowShown(false);
            
            CTTableColumns columns = ctTable.addNewTableColumns();
            columns.setCount(colCount);
            
            //Create columns
            for (int i = 1; i <= colCount; i++) {
                CTTableColumn column = columns.addNewTableColumn();
                column.setId(i);
                column.setName(colNames[i - 1]);
            }
            
            //Set column names
            for(String name : colNames) {
                xlRow.createCell(cellNum++).setCellValue(name);
            }

            //Set values
            for (ArrayList<String> row : rows) {
                xlRow = sheet1.createRow(rowNum++);
                cellNum = 0;
                
                for (int i = 0; i < row.size(); i++) {
                    xlRow.createCell(cellNum++).setCellValue(row.get(i));
                	
                }
            }

            if (open) {
                Desktop.getDesktop().open(file);
            }
            
            wb.write(fileOut);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    
    
    
}
