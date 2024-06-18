/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.Company;
import invoicingapp_monolithic.ContactPerson;
import invoicingapp_monolithic.Phone;
import invoicingapp_monolithic.Users;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

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
    String introContact="Aquí, puedes añadir una persona de contacto";
    String introPhone="Aquí, puedes añadir un número de teléfono";
    ArrayList<Company> companies=new ArrayList();
    
    @FXML private PasswordField fieldPasswd1,fieldPasswd2 ;
    @FXML private TextField textFieldPW1,textFieldPW2,fieldUsername, 
            fieldVAT,fieldComName,fieldLegalName,fieldEmailCompany,fieldWeb,
            fieldStreet,fieldStNumber,fieldApt,fieldCP,fieldCity,
            fieldState,fieldCountry,
            fieldFirstname,fieldMiddlename,fieldLastname,fieldRole,fieldContactEmail,
            fieldPhoneNumber, fieldPhoneKind;
    @FXML ComboBox cbCompany;
    @FXML private Label labelIntro, labelPassword;
    @FXML private GridPane paneUser,paneCompany,paneAddress,paneContact,panePhone;
    @FXML private HBox paneFootUser,paneFootCompany,paneFootAddress,paneFootContact,paneFootPhone;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        companies=Company.getAllFromDB();
        
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
        
        if(fieldPasswd1.getText().equals(fieldPasswd2.getText())){
            user.setUserName(fieldUsername.getText());
            user.setPasswd(fieldPasswd1.getText());
        
            paneUser.setVisible(false);
            paneCompany.setVisible(true);
            paneFootUser.setVisible(false);
            paneFootCompany.setVisible(true);
            labelPassword.setText("");
        }else{
            labelPassword.setText("Las contraseñas no coinciden");
        }
    }
    
    @FXML protected void onClicNextFromCompany(){
        labelIntro.setText(introAddress);
        user.setVatNumber(fieldVAT.getText());
        user.setComName(fieldComName.getText());
        user.setLegalName(fieldLegalName.getText());
        user.setEmail(fieldEmailCompany.getText());
        user.setWeb(fieldWeb.getText());
        
        paneCompany.setVisible(false);
        paneFootCompany.setVisible(false);
        paneAddress.setVisible(true);
        paneFootAddress.setVisible(true);
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
    
    @FXML protected void onClicAddContact(){
        labelIntro.setText(introContact);
        paneCompany.setVisible(false);
        paneFootCompany.setVisible(false);
        
        paneContact.setVisible(true);
        paneFootContact.setVisible(true);
    }
    
    @FXML protected void onClicAddPhone(){
        labelIntro.setText(introPhone);
        paneCompany.setVisible(false);
        paneFootCompany.setVisible(false);
        
        panePhone.setVisible(true);
        paneFootPhone.setVisible(true);
    }
    
    @FXML protected void onClicCancelFromContact(){
        labelIntro.setText(introCompany);
        paneCompany.setVisible(true);
        paneFootCompany.setVisible(true);
        
        paneContact.setVisible(false);
        paneFootContact.setVisible(false);
        
        fieldFirstname.clear();
        fieldMiddlename.clear();
        fieldLastname.clear();
        fieldRole.clear();
        fieldContactEmail.clear();
    }
    
    @FXML protected void onClicCancelFromPhone(){
        labelIntro.setText(introCompany);
        paneCompany.setVisible(true);
        paneFootCompany.setVisible(true);
        
        panePhone.setVisible(false);
        paneFootPhone.setVisible(false);
        
        fieldPhoneNumber.clear();
        fieldPhoneKind.clear();
    }
    
    @FXML protected void onClicSaveFromContact(){
        ContactPerson contact=new ContactPerson();
        
        contact.setFirstname(fieldFirstname.getText());
        contact.setMiddlename(fieldMiddlename.getText());
        contact.setLastname(fieldLastname.getText());
        contact.setRole(fieldRole.getText());
        contact.setEmail(fieldContactEmail.getText());
        
        user.addContactPerson(contact);
        
        labelIntro.setText(introCompany);
        paneCompany.setVisible(true);
        paneFootCompany.setVisible(true);
        
        paneContact.setVisible(false);
        paneFootContact.setVisible(false);
        fieldFirstname.clear();
        fieldMiddlename.clear();
        fieldLastname.clear();
        fieldRole.clear();
        fieldContactEmail.clear();
    }
    
    @FXML protected void onClicSaveFromPhone(){
        Phone phone=new Phone();
        
        phone.setPhoneNumber(fieldPhoneNumber.getText());
        phone.setKind(fieldPhoneKind.getText());
        
        user.addPhone(phone);
        
        labelIntro.setText(introCompany);
        paneCompany.setVisible(true);
        paneFootCompany.setVisible(true);
        
        panePhone.setVisible(false);
        paneFootPhone.setVisible(false);
        fieldPhoneNumber.clear();
        fieldPhoneKind.clear();
    }
    
    @FXML protected void onClicSaveFromAddress(ActionEvent e){
        Button source=(Button)e.getSource();
        Stage stage=(Stage)source.getScene().getWindow();
        
        user.getAddress().setStreet(fieldStreet.getText());
        user.getAddress().setStNumber(fieldStNumber.getText());
        user.getAddress().setApt(fieldApt.getText());
        user.getAddress().setCp(fieldCP.getText());
        user.getAddress().setCity(fieldCity.getText());
        user.getAddress().setState(fieldState.getText());
        user.getAddress().setCountry(fieldCountry.getText());
        user.addToDB();
        
        stage.close();
    }
    
    @FXML protected void populateComboBox(){
        ObservableList<Company> companyObs =FXCollections.observableArrayList(companies);
        cbCompany.setItems(companyObs);
        
        cbCompany.setCellFactory(new Callback<ListView<Company>, ListCell<Company>>() {
            @Override
            public ListCell<Company> call(ListView<Company> p) {
                return new ListCell<Company>() {
                    @Override
                    protected void updateItem(Company item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getComName());
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });
        
        cbCompany.setButtonCell(new ListCell<Company>() {
            @Override
            protected void updateItem(Company item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.getComName());
                } else {
                    setText(null);
                }
            }
        });
    }
    
    @FXML protected void getSelectionComboBox(){
        Company company=(Company) cbCompany.getSelectionModel().getSelectedItem();
        
        fieldVAT.setText(company.getVatNumber());
        fieldComName.setText(company.getComName());
        fieldLegalName.setText(company.getLegalName());
        fieldEmailCompany.setText(company.getEmail());
        fieldWeb.setText(company.getWeb());
        fieldStreet.setText(company.getAddress().getStreet());
        fieldStNumber.setText(company.getAddress().getStNumber());
        fieldApt.setText(company.getAddress().getApt());
        fieldCP.setText(company.getAddress().getCp());
        fieldCity.setText(company.getAddress().getCity());
        fieldState.setText(company.getAddress().getState());
        fieldCountry.setText(company.getAddress().getCountry());
        
        user.setIdCompany(company.getIdCompany());
        user.getAddress().setIdAddress(company.getAddress().getIdAddress());
        
    }
}
