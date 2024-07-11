/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.Phone;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewNewPhoneController implements Initializable {

    private Phone phone;
    private boolean control=true;
    private final String errorEmpty="Falta un dato obligatorio.";
   
    @FXML private Label labelError;    
    @FXML private TextField fieldPhoneNumber,fieldPhoneKind;
    @FXML private VBox paneNewPhone;
    
    protected void initData(Phone phone){
        this.phone=phone;
        if(phone.getPhoneNumber()!=null){
            fieldPhoneNumber.setText(phone.getPhoneNumber());
            fieldPhoneKind.setText(phone.getKind());
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        labelError.setText(errorEmpty);
    }
    
     @FXML protected void onClicCancel(){
        closeWindow();
    }
    
    @FXML protected void onClicSave(){
        saveData();
        if(control){
            closeWindow();
        }
    }
    
    private void saveData(){
        control=true;
        labelError.setVisible(false);
        fieldPhoneNumber.getStyleClass().remove("error");
        
        if(fieldPhoneNumber.getText().isEmpty()){
            labelError.setVisible(true);
            fieldPhoneNumber.getStyleClass().add("error");
            control=false;
        }
        if(control){
            phone.setPhoneNumber(fieldPhoneNumber.getText());
            phone.setKind(fieldPhoneKind.getText());
        }
    }
    
    private void closeWindow(){
        Stage stage = (Stage)paneNewPhone.getScene().getWindow();
        stage.close();
    }
    
}
