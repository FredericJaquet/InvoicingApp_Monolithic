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
    
    @FXML private PasswordField fieldPasswd1,fieldPasswd2 ;
    @FXML private TextField textFieldPW1,textFieldPW2,fieldUsername, 
                            fieldVAT,fieldComName,fieldLegalName,
                            fieldEmail,fieldWeb;
    @FXML private Label labelIntro;
    @FXML private GridPane paneUser, paneCompany;
    @FXML private HBox paneFoot1, paneFoot2;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        textFieldPW1.setVisible(false);
        textFieldPW2.setVisible(false);
        labelIntro.setText("Aquí, puedes crear un nombre de usuario y su contraseña");
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
    
    @FXML protected void onClicNext1(){
        labelIntro.setText("Aquí, puedes crear una Empresa nueva o seleccionar una existente");
        user.setUserName(fieldUsername.getText());
        user.setPasswd(fieldPasswd1.getText());
        paneUser.setVisible(false);
        paneCompany.setVisible(true);
        paneFoot1.setVisible(false);
        paneFoot2.setVisible(true);
    }
    
    @FXML protected void onClicNext2(){
        labelIntro.setText("Aquí, puedes crear una dirección para la Mepresa nueva");
        user.setVatNumber(fieldVAT.getText());
        user.setComName(fieldComName.getText());
        user.setLegalName(fieldLegalName.getText());
        user.setEmail(fieldEmail.getText());
        user.setWeb(fieldWeb.getText());
        paneCompany.setVisible(false);
    }
    
    @FXML protected void onClicBack2(){
        labelIntro.setText("Aquí, puedes crear un nombre de usuario y su contraseña");
        paneUser.setVisible(true);
        paneCompany.setVisible(false);
        paneFoot1.setVisible(true);
        paneFoot2.setVisible(false);
    }
    
}
