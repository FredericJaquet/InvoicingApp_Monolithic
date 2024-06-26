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
    
    @FXML private TextField fieldPhoneNumber,fieldPhoneKind;
    @FXML private VBox paneNewPhone;
    
    protected void initData(Phone phone){
        this.phone=phone;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    @FXML protected void onClicCancel(){
        Stage stage = (Stage) paneNewPhone.getScene().getWindow();
        stage.close();
    }
    
    @FXML protected void onClicSave(){
        phone.setPhoneNumber(fieldPhoneNumber.getText());
        phone.setKind(fieldPhoneKind.getText());
        
        Stage stage = (Stage) paneNewPhone.getScene().getWindow();
        stage.close();
    }
    
}
