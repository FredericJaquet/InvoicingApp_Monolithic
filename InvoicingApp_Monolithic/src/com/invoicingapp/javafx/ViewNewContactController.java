/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.tools.Validations;
import invoicingapp_monolithic.ContactPerson;
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
public class ViewNewContactController implements Initializable {

    private ContactPerson contact;
    private boolean control=true;
    private final String errorEmpty="Falta un dato obligatorio.";
    
    @FXML private Label labelError;
    @FXML private TextField fieldFirstname,fieldMiddlename, fieldLastname, fieldRole,fieldEmail;
    @FXML private VBox paneNewContact;
    
    protected void initData(ContactPerson contact){
        this.contact=contact;
        if(contact.getFirstname()!=null){
            fieldFirstname.setText(contact.getFirstname());
            fieldMiddlename.setText(contact.getMiddlename());
            fieldLastname.setText(contact.getLastname());
            fieldRole.setText(contact.getRole());
            fieldEmail.setText(contact.getEmail());
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
        if(Validations.isNotEmpty(fieldFirstname,labelError,errorEmpty)&&Validations.isNotEmpty(fieldMiddlename,labelError,errorEmpty)){
            contact.setFirstname(fieldFirstname.getText());
            contact.setMiddlename(fieldMiddlename.getText());
            contact.setLastname(fieldLastname.getText());
            contact.setRole(fieldRole.getText());
            contact.setEmail(fieldEmail.getText());
            closeWindow();
        }
    }
    
    private void closeWindow(){
        Stage stage = (Stage)paneNewContact.getScene().getWindow();
        stage.close();
    }
}
