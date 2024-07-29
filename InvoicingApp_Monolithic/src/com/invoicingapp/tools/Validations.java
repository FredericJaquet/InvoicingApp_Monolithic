/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.invoicingapp.tools;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

/**
 *
 * @author frede
 */
public class Validations {
    
    public static boolean isDouble(TextField field,Label lbError,String message){
        String dl=field.getText();
        double result;
        boolean control=true;
        
        lbError.setVisible(false);
        field.getStyleClass().remove("error");
        try{
            result=Double.parseDouble(dl);
        }catch(NumberFormatException e){
            lbError.setVisible(true);
            lbError.setText(message);
            field.getStyleClass().add("error");
            control=false;
        }
        
        return control;
    }
    
    public static boolean isInteger(TextField field,Label lbError,String message){
        String dl=field.getText();
        int duedate;
        boolean control=true;
        
        lbError.setVisible(false);
        field.getStyleClass().remove("error");
        try{
            duedate=Integer.parseInt(field.getText());
        }catch(NumberFormatException ex){
            lbError.setVisible(true);
            lbError.setText(message);
            field.getStyleClass().add("error");
            control=false;
        }
        
        return control;
    }
        
    public static boolean isNotEmpty(TextField field,Label lbError,String message){
        boolean control=true;
        
        field.getStyleClass().remove("error");
        lbError.setVisible(false);
        if(field.getText().isEmpty()){
            lbError.setVisible(true);
            lbError.setText(message);
            field.getStyleClass().add("error");
            control=false;
        }
          
        return control;
    }
    
    public static boolean isNotNull(DatePicker dp,Label lbError,String message){
        boolean control=true;
        
        lbError.setVisible(false);
        if(dp.getValue()==null){
            lbError.setVisible(true);
            lbError.setText(message);
            control=false;
        }
        
        return control;
    }
}
