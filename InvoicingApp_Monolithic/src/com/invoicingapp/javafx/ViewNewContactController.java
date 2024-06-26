/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.ContactPerson;
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
public class ViewNewContactController implements Initializable {

    private ContactPerson contact;
    
    @FXML private TextField fieldFirstname,fieldMiddlename, fieldLastname, fieldRole,fieldEmail;
    @FXML private VBox paneNewContact;
    
    protected void initData(ContactPerson contact){
        this.contact=contact;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    @FXML protected void onClicCancel(){
        Stage stage = (Stage) paneNewContact.getScene().getWindow();
        stage.close();
    }
    
    @FXML protected void onClicSave(){
        contact.setFirstname(fieldFirstname.getText());
        contact.setMiddlename(fieldMiddlename.getText());
        contact.setLastname(fieldLastname.getText());
        contact.setRole(fieldRole.getText());
        contact.setEmail(fieldEmail.getText());
        
        Stage stage = (Stage) paneNewContact.getScene().getWindow();
        stage.close();
    }
    
    
}
