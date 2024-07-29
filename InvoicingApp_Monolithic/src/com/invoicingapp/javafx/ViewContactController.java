/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.tools.LabelFeatures;
import invoicingapp_monolithic.ContactPerson;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class ViewContactController implements Initializable {

    private ArrayList<String> query=new ArrayList();
    private ContactPerson contact;
    private LabelFeatures lbFeatures=new LabelFeatures();
    
    @FXML private Label lb_ContactPerson_Firstname,lb_ContactPerson_Middlename,lb_ContactPerson_Lastname,lb_ContactPerson_Role,lb_ContactPerson_Email;
    @FXML private TextField fieldFirstname,fieldMiddlename,fieldLastname,fieldRole,fieldEmail;
    
    public void initData(ContactPerson contact){
        this.contact=contact;
        lb_ContactPerson_Firstname.setText(contact.getFirstname());
        lb_ContactPerson_Middlename.setText(contact.getMiddlename());
        lb_ContactPerson_Lastname.setText(contact.getLastname());
        lb_ContactPerson_Role.setText(contact.getRole());
        lb_ContactPerson_Email.setText(contact.getEmail());
        lbFeatures.makeLabelEditable(lb_ContactPerson_Firstname,fieldFirstname,"ContactPerson","firstName",contact.getIdContactPerson());
        lbFeatures.makeLabelEditable(lb_ContactPerson_Middlename,fieldMiddlename,"ContactPerson","middleName",contact.getIdContactPerson());
        lbFeatures.makeLabelEditable(lb_ContactPerson_Lastname,fieldLastname,"ContactPerson","lastName",contact.getIdContactPerson());
        lbFeatures.makeLabelEditable(lb_ContactPerson_Role,fieldRole,"ContactPerson","role",contact.getIdContactPerson());
        lbFeatures.makeLabelEditable(lb_ContactPerson_Email,fieldEmail,"ContactPerson","emaile",contact.getIdContactPerson());
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    public ContactPerson getContact(){
        return contact;
    }
    
    public ArrayList<String> getQuery(){
        query=lbFeatures.getQuery();
        return query;
    }
    
    public void resetQuery(){
        query.clear();
    }
    
}
