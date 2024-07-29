/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.tools.Validations;
import invoicingapp_monolithic.Address;
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
public class ViewNewAddressController implements Initializable {

    private Address address;
    private final String errorEmpty="Falta un dato obligatorio.";
   
    @FXML private Label labelError;
    @FXML private TextField fieldStreet,fieldStNumber,fieldApt,fieldCP,fieldCity,fieldState,fieldCountry;
    @FXML private VBox paneNewAddress;
    
    protected void initData(Address address){
        this.address=address;
        if(address.getStreet()!=null){
            fieldStreet.setText(address.getStreet());
            fieldStNumber.setText(address.getStNumber());
            fieldApt.setText(address.getApt());
            fieldCP.setText(address.getCp());
            fieldCity.setText(address.getCity());
            fieldState.setText(address.getState());
            fieldCountry.setText(address.getCountry());
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        labelError.setText(errorEmpty);
    }

    @FXML protected void onClicCancel(){
        closeWindow();
    }
    
    @FXML protected void onClicSave(){
        
        if(Validations.isNotEmpty(fieldStreet,labelError,errorEmpty)&&Validations.isNotEmpty(fieldStNumber,labelError,errorEmpty)&&Validations.isNotEmpty(fieldCP,labelError,errorEmpty)&&Validations.isNotEmpty(fieldCity,labelError,errorEmpty)&&Validations.isNotEmpty(fieldCountry,labelError,errorEmpty)){
            address.setStreet(fieldStreet.getText());
            address.setStNumber(fieldStNumber.getText());
            address.setApt(fieldApt.getText());
            address.setCp(fieldCP.getText());
            address.setCity(fieldCity.getText());
            address.setState(fieldState.getText());
            address.setCountry(fieldCountry.getText());
            closeWindow();
        }
    }
    
    private void closeWindow(){
        Stage stage = (Stage)paneNewAddress.getScene().getWindow();
        stage.close();
    }
}
