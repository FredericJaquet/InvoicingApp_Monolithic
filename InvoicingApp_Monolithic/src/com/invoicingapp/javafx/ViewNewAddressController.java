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
    private boolean control=true;
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
        saveData();
        if(control){
            closeWindow();
        }
    }
    
    private void saveData(){
        control=true;
        labelError.setVisible(false);
        fieldStreet.getStyleClass().remove("error");
        fieldStNumber.getStyleClass().remove("error");
        fieldCP.getStyleClass().remove("error");
        fieldCity.getStyleClass().remove("error");
        fieldCountry.getStyleClass().remove("error");
        
        if(fieldStreet.getText().isEmpty()){
            labelError.setVisible(true);
            fieldStreet.getStyleClass().add("error");
            control=false;
        }
        if(fieldStNumber.getText().isEmpty()){
            labelError.setVisible(true);
            fieldStNumber.getStyleClass().add("error");
            control=false;
        }
        if(fieldCP.getText().isEmpty()){
            labelError.setVisible(true);
            fieldCP.getStyleClass().add("error");
            control=false;
        }
        if(fieldCity.getText().isEmpty()){
            labelError.setVisible(true);
            fieldCity.getStyleClass().add("error");
            control=false;
        }
        if(fieldCountry.getText().isEmpty()){
            labelError.setVisible(true);
            fieldCountry.getStyleClass().add("error");
            control=false;
        }
        if(control){
            address.setStreet(fieldStreet.getText());
            address.setStNumber(fieldStNumber.getText());
            address.setApt(fieldApt.getText());
            address.setCp(fieldCP.getText());
            address.setCity(fieldCity.getText());
            address.setState(fieldState.getText());
            address.setCountry(fieldCountry.getText());
        }
    }
    
    private void closeWindow(){
        Stage stage = (Stage)paneNewAddress.getScene().getWindow();
        stage.close();
    }
}
