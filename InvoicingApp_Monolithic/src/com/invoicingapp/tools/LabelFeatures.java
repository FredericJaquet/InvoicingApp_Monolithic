/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.invoicingapp.tools;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author frede
 */
public class LabelFeatures {
    
    private ArrayList<String> query=new ArrayList();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public void makeLabelEditable(Label label, TextField textField,String table,String field, int id) {
        textField.setVisible(false);

        label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getClickCount() == 2) {
                switchToTextField(label, textField);
            }
        });

        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                switchToLabel(label, textField,table,field,id);
            }
        });
    }
    
    public void makeLabelEditable(Label label,TextField textField,String table,String field, String id) {
        textField.setVisible(false);

        label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getClickCount() == 2) {
                switchToTextField(label, textField);
            }
        });

        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                switchToLabel(label, textField,table,field,id);
            }
        });
    }
    
    public void makeLabelEditable(Label label, CheckBox checkBox,String table,String field, int id) {
        checkBox.setVisible(false);

        label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getClickCount() == 2) {
                switchToCheckBox(label, checkBox);
            }
        });

        checkBox.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                switchToLabel(label, checkBox,table,field,id);
            }
        });
    }
    
    public void makeLabelEditable(Label label,ChoiceBox choiceBox,String table,String field, int id) {
        choiceBox.setVisible(false);
        label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getClickCount() == 2) {
                switchToChoiceBox(label, choiceBox);
            }
        });
        
        choiceBox.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                switchToLabel(label, choiceBox,table,field,id);
            }
        });
    }
    
    public void makeLabelEditable(Label label, DatePicker datePicker, String tableDB, String fieldDB, int id) {
        datePicker.setVisible(false);
        label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getClickCount() == 2) {
                switchToDatePicker(label, datePicker);
            }
        });

        datePicker.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                switchToLabel(label, datePicker, tableDB, fieldDB,id);
            }
        });
    }
    
    private void switchToTextField(Label label, TextField textField) {
        textField.setText(label.getText());
        textField.setVisible(true);
        textField.requestFocus();
        label.setVisible(false);
    }
    
    private void switchToCheckBox(Label label, CheckBox checkBox) {
        if(label.getText().equals("Sí")){
            checkBox.setSelected(true);
        }else{
            checkBox.setSelected(false);
        }
        checkBox.requestFocus();
        checkBox.setVisible(true);
        label.setVisible(false);
    }
    
    private void switchToChoiceBox(Label label, ChoiceBox choiceBox){
        choiceBox.getSelectionModel().select(label.getText());
        choiceBox.setVisible(true);
        label.setVisible(false);
    }
    
    private void switchToDatePicker(Label label, DatePicker datePicker) {
        datePicker.setVisible(true);
        label.setVisible(false);
    }
    
    private void switchToLabel(Label label, TextField textField,String table,String field, int id) {
        label.setText(textField.getText());
        label.setVisible(true);
        textField.setVisible(false);
        
        updateQuery(label,table,field,id);
    }
    
    private void switchToLabel(Label label, TextField textField,String table,String field, String id) {
        label.setText(textField.getText());
        label.setVisible(true);
        textField.setVisible(false);
        
        updateQuery(label,table,field,id);
    }
    
    private void switchToLabel(Label label, CheckBox checkBox,String table,String field, int id) {
        if(checkBox.isSelected()){
            label.setText("Sí");
        }else{
            label.setText("No");
        }
        
        label.setVisible(true);
        checkBox.setVisible(false);
        
        updateQuery(label,table,field,id);
    }
    
    private void switchToLabel(Label label, ChoiceBox choiceBox,String table,String field, int id) {
        label.setText(choiceBox.getValue().toString());
        label.setVisible(true);
        choiceBox.setVisible(false);
        
        updateQuery(label,table,field,id);
    }
    
    private void switchToLabel(Label label, DatePicker datePicker, String tableDB, String fieldDB, int id) {
        label.setText(datePicker.getValue().toString());
        label.setVisible(true);
        datePicker.setVisible(false);
        updateQuery(label,tableDB,fieldDB,id);
        label.setText(datePicker.getValue().format(formatter));
    }
    
    private void updateQuery(Label label,String table,String field,int id){
        String newValue=label.getText();
        
        if(newValue.equals("Sí")){
            newValue="1";
        }else if(newValue.equals("No")){
            newValue="0";
        }
        query.add("UPDATE "+table+" SET "+field+"='"+newValue+"' WHERE id"+table+"="+id+";");
    }
    
    private void updateQuery(Label label,String table,String field,String id){
        String newValue=label.getText();
        
        if(newValue.equals("Sí")){
            newValue="1";
        }else if(newValue.equals("No")){
            newValue="0";
        }
        query.add("UPDATE "+table+" SET "+field+"='"+newValue+"' WHERE id"+table+"='"+id+"';");
    }
    
    public ArrayList<String> getQuery(){
        return query;
    }
    
    public void resetQuery(){
        query.clear();
    }
    
}
