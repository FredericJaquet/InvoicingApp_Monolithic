/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.Address;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewNewAddressController implements Initializable {

    private Address address;
    
    @FXML private TextField fieldStreet,fieldStNumber,fieldApt,fieldCP,fieldCity,fieldState,fieldCountry;
    @FXML private VBox paneNewAddress;
    
    protected void initData(Address address){
        this.address=address;
        
        fieldStreet.setText(address.getStreet());
        fieldStNumber.setText(address.getStNumber());
        fieldApt.setText(address.getApt());
        fieldCP.setText(address.getCp());
        fieldCity.setText(address.getCity());
        fieldState.setText(address.getState());
        fieldCountry.setText(address.getCountry());
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML protected void onClicCancel(){
        Stage stage = (Stage)paneNewAddress.getScene().getWindow();
        stage.close();
    }
    
    @FXML protected void onClicSave(){
        Stage stage = (Stage)paneNewAddress.getScene().getWindow();
        saveData();
        stage.close();
    }
    
    private void saveData(){
        address.setStreet(fieldStreet.getText());
        address.setStNumber(fieldStNumber.getText());
        address.setApt(fieldApt.getText());
        address.setCp(fieldCP.getText());
        address.setCity(fieldCity.getText());
        address.setState(fieldState.getText());
        address.setCountry(fieldCountry.getText());
    }
    
}
