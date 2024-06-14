/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.Users;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewCreateUserController implements Initializable {

    Users user=new Users();
    String introUser="Aquí, puedes crear un nombre de usuario y su contraseña";
    String introCompany="Aquí, puedes crear una Empresa nueva o seleccionar una existente";
    String introAddress="Aquí, puedes crear una dirección para la Empresa nueva";
            
    
    @FXML private PasswordField fieldPasswd1,fieldPasswd2 ;
    @FXML private TextField textFieldPW1,textFieldPW2,fieldUsername, 
                            fieldVAT,fieldComName,fieldLegalName,
                            fieldEmail,fieldWeb,fieldStreet,
                            fieldStNumber,fieldApt,fieldCP,
                            fieldCity,fieldState,fieldCountry;
    @FXML private Label labelIntro;
    @FXML private GridPane paneUser, paneCompany, paneAddress;
    @FXML private HBox paneFootUser, paneFootCompany, paneFootAddress;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        textFieldPW1.setVisible(false);
        textFieldPW2.setVisible(false);
        labelIntro.setText(introUser);
    }
    
    @FXML protected void onPressedSeePW(){
        textFieldPW1.setText(fieldPasswd1.getText());
        fieldPasswd1.setVisible(false);
        textFieldPW1.setVisible(true);
        textFieldPW2.setText(fieldPasswd2.getText());
        fieldPasswd2.setVisible(false);
        textFieldPW2.setVisible(true);
    }
    
    @FXML protected void onReleaseSeePW(){
        textFieldPW1.setVisible(false);
        fieldPasswd1.setVisible(true);
        textFieldPW2.setVisible(false);
        fieldPasswd2.setVisible(true);
    }
    
    @FXML protected void onClicCancel(ActionEvent e){
        Button source=(Button)e.getSource();
        Stage stage=(Stage)source.getScene().getWindow();
        stage.close();
    }
    
    @FXML protected void onClicNextFromUser(){
        labelIntro.setText(introCompany);
        user.setUserName(fieldUsername.getText());
        user.setPasswd(fieldPasswd1.getText());
        paneUser.setVisible(false);
        paneCompany.setVisible(true);
        paneFootUser.setVisible(false);
        paneFootCompany.setVisible(true);
    }
    
    @FXML protected void onClicNextFromCompany(){
        labelIntro.setText(introAddress);
        user.setVatNumber(fieldVAT.getText());
        user.setComName(fieldComName.getText());
        user.setLegalName(fieldLegalName.getText());
        user.setEmail(fieldEmail.getText());
        user.setWeb(fieldWeb.getText());
        paneCompany.setVisible(false);
        paneFootCompany.setVisible(false);
        
        paneAddress.setVisible(true);
        paneFootAddress.setVisible(true);
    }
    
    @FXML protected void onClicNextFromAddress(){
        user.getAddress().setStreet(fieldStreet.getText());
        user.getAddress().setStNumber(fieldStNumber.getText());
        user.getAddress().setApt(fieldApt.getText());
        user.getAddress().setCp(fieldCP.getText());
        user.getAddress().setCity(fieldCity.getText());
        user.getAddress().setState(fieldState.getText());
        user.getAddress().setCountry(fieldCountry.getText());
    }
    
    @FXML protected void onClicBackFromCompany(){
        labelIntro.setText(introUser);
        paneUser.setVisible(true);
        paneCompany.setVisible(false);
        paneFootUser.setVisible(true);
        paneFootCompany.setVisible(false);
    }
    
    @FXML protected void onClicBackFromAddress(){
        labelIntro.setText(introCompany);
        paneCompany.setVisible(true);
        paneAddress.setVisible(false);
        paneFootCompany.setVisible(true);
        paneFootAddress.setVisible(false);
    }
    
}
