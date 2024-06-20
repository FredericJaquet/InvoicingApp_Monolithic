/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.Company;
import invoicingapp_monolithic.ContactPerson;
import invoicingapp_monolithic.CustomProv;
import invoicingapp_monolithic.Customer;
import invoicingapp_monolithic.Phone;
import java.net.URL;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;


public class ViewCreateCustomerController implements Initializable {

    String introCompany="Datos de la Empresa";
    String introAddress="Dirección";
    String introContact="Persona de contacto";
    String introPhone="Teléfono de contacto";
    String introFiscalData="Datos fiscales";
    Customer customer=new Customer();
    ArrayList<CustomProv> companies=new ArrayList();
    
    @FXML private Label labelIntro;
    @FXML private TextField fieldVAT,fieldComName,fieldLegalName,fieldEmailCompany,fieldWeb;
    @FXML private TextField fieldStreet,fieldStNumber,fieldApt,fieldCP,fieldCity,fieldState,fieldCountry;
    @FXML private TextField fieldDefaultVAT,fieldDefaultWithholding,fieldInvoicingMethod,fieldPayMethod,fieldDuedate;
    @FXML private TextField fieldPhoneNumber,fieldPhoneKind;
    @FXML private TextField fieldFirstname,fieldMiddlename,fieldLastname,fieldRole,fieldContactEmail;
    @FXML private CheckBox cbEurope,cbEnabled;
    @FXML ComboBox cbCompany;
    @FXML private GridPane paneCompany, paneAddress,paneFiscalData,panePhone,paneContact;
    @FXML HBox paneFootCompany,paneFootAddress,paneFootFiscalData,paneFootPhone,paneFootContact;  
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        companies=CustomProv.getAllCustomProvFromDB();
        
        labelIntro.setText(introCompany);
    }
    
    @FXML protected void onClicCancel(ActionEvent e){
        Button source=(Button)e.getSource();
        Stage stage=(Stage)source.getScene().getWindow();
        stage.close();
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
    
    @FXML protected void onClicNextFromCompany(){
        labelIntro.setText(introAddress);
        customer.setVatNumber(fieldVAT.getText());
        customer.setComName(fieldComName.getText());
        customer.setLegalName(fieldLegalName.getText());
        customer.setEmail(fieldEmailCompany.getText());
        customer.setWeb(fieldWeb.getText());
        
        paneCompany.setVisible(false);
        paneFootCompany.setVisible(false);
        paneAddress.setVisible(true);
        paneFootAddress.setVisible(true);
    }
    
    @FXML protected void onClicNextFromAddress(){
        labelIntro.setText(introFiscalData);
        customer.getAddress().setStreet(fieldStreet.getText());
        customer.getAddress().setStNumber(fieldStNumber.getText());
        customer.getAddress().setApt(fieldApt.getText());
        customer.getAddress().setCp(fieldCP.getText());
        customer.getAddress().setCity(fieldCity.getText());
        customer.getAddress().setState(fieldState.getText());
        customer.getAddress().setCountry(fieldCountry.getText());
        
        paneAddress.setVisible(false);
        paneFootAddress.setVisible(false);
        paneFiscalData.setVisible(true);
        paneFootFiscalData.setVisible(true);
    }
    
    @FXML protected void onClicSaveFromFiscalData(ActionEvent e){
        Button source=(Button)e.getSource();
        Stage stage=(Stage)source.getScene().getWindow();
        double defaultVAT=0,defaultWithholding=0;
        int duedate=0;
        boolean control=true;
        
        try{
            defaultVAT=Double.parseDouble(fieldDefaultVAT.getText());
        }catch(NumberFormatException ex){
            fieldDefaultVAT.getStyleClass().add("error");
            control=false;
        }
        try{
            defaultWithholding=Double.parseDouble(fieldDefaultWithholding.getText());
        }catch(NumberFormatException ex){
            fieldDefaultWithholding.getStyleClass().add("error");
            control=false;
        }
        try{
            duedate=Integer.parseInt(fieldDuedate.getText());
        }catch(NumberFormatException ex){
            fieldDuedate.getStyleClass().add("error");
            control=false;
        }
        
        if(!control){
            try {
                Thread.sleep(3*1000);
            } catch (InterruptedException ex) {
                System.out.println(e);
            }
            fieldDefaultVAT.getStyleClass().remove("error");
        }else{
            customer.setDefaultVAT(defaultVAT);
            customer.setDefaultWithholding(defaultWithholding);
            customer.setEurope(cbEurope.isSelected());
            customer.setEnabled(cbEnabled.isSelected());
            customer.setInvoicingMethod(fieldInvoicingMethod.getText());
            customer.setPayMethod(fieldPayMethod.getText());
            customer.setDuedate(duedate);
            customer.addToDB();
        
            stage.close();
        }
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
    
    @FXML protected void onClicSaveFromPhone(){
        Phone phone=new Phone();
        
        phone.setPhoneNumber(fieldPhoneNumber.getText());
        phone.setKind(fieldPhoneKind.getText());
        
        customer.addPhone(phone);
        
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
        
        customer.addContactPerson(contact);
        
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
    
    @FXML protected void onClicBackFromAddress(){
        labelIntro.setText(introCompany);
        paneCompany.setVisible(true);
        paneAddress.setVisible(false);
        paneFootCompany.setVisible(true);
        paneFootAddress.setVisible(false);
    }
    
    @FXML protected void onClicBackFromFiscalData(){
        labelIntro.setText(introAddress);
        paneAddress.setVisible(true);
        paneFiscalData.setVisible(false);
        paneFootAddress.setVisible(true);
        paneFootFiscalData.setVisible(false);
    }
    
    @FXML protected void populateComboBox(){
        ObservableList<CustomProv> companyObs =FXCollections.observableArrayList(companies);
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
        CustomProv customProv=(CustomProv) cbCompany.getSelectionModel().getSelectedItem();
        
        fieldVAT.setText(customProv.getVatNumber());
        fieldComName.setText(customProv.getComName());
        fieldLegalName.setText(customProv.getLegalName());
        fieldEmailCompany.setText(customProv.getEmail());
        fieldWeb.setText(customProv.getWeb());
        fieldStreet.setText(customProv.getAddress().getStreet());
        fieldStNumber.setText(customProv.getAddress().getStNumber());
        fieldApt.setText(customProv.getAddress().getApt());
        fieldCP.setText(customProv.getAddress().getCp());
        fieldCity.setText(customProv.getAddress().getCity());
        fieldState.setText(customProv.getAddress().getState());
        fieldCountry.setText(customProv.getAddress().getCountry());
        
        customer.setIdCompany(customProv.getIdCompany());
        customer.getAddress().setIdAddress(customProv.getAddress().getIdAddress());   
    }
}
