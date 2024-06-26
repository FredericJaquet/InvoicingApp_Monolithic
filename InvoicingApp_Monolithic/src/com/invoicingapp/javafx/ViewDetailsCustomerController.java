/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.ContactPerson;
import invoicingapp_monolithic.Customer;
import invoicingapp_monolithic.Phone;
import invoicingapp_monolithic.Scheme;
import invoicingapp_monolithic.SchemeLine;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class ViewDetailsCustomerController implements Initializable {

    private Customer customer=new Customer();
    private ArrayList<Phone> phones=new ArrayList();
    private ArrayList<ContactPerson> contacts=new ArrayList();
    private ArrayList<Scheme> schemes=new ArrayList();
    private int iContacts=0,iPhones=0,iSchemes=0;
    
    @FXML Label lbComName,lbLegalName,lbWeb,lbCompEmail,lbVATNumber,lbDefaultVAT,
            lbDuedate,lbDefaultWithholding,lbInvoicingMethod,lbPayMethod,lbEurope,
            lbEnabled,lbStreet,lbStNumber,lbCity,lbState,lbApt,lbCP,lbCountry,
            lbFirstname,lbMiddlename,lbLastname,lbRole,lbContactEmail,
            lbPhoneNumber,lbKind,lbSchemeName,lbSourceLanguage,lbTargetLanguage,
            lbPrice,lbUnits,lbFieldName;
    @FXML TextField fieldComName,fieldLegalName,fieldWeb,fieldCompEmail,fieldVATNumber,fieldDefaultVAT,
            fieldDuedate,fieldDefaultWithholding,fieldInvoicingMethod,fieldPayMethod,
            fieldStreet,fieldStNumber,fieldCity,fieldState,fieldApt,fieldCP,fieldCountry,
            fieldFirstname,fieldMiddlename,fieldLastname,fieldRole,fieldContactEmail,
            fieldPhoneNumber,fieldKind,fieldSchemeName,fieldSourceLanguage,fieldTargetLanguage,
            fieldPrice,fieldUnits,fieldFieldName;
    @FXML CheckBox cbEurope,cbEnabled;
    @FXML Button btnContactLeft,btnContactRight,btnPhoneLeft,btnPhoneRight,btnNewContact,btnNewPhone,
            btnSchemeLeft,btnSchemeRight, btnNewScheme;
    @FXML GridPane gridContacts,gridPhones,gridScheme;
    @FXML ScrollPane paneDetailsCustomer;
    @FXML TableView<SchemeLine> tableSchemeLines;
    @FXML TableColumn<SchemeLine,String> columnDescription;
    @FXML TableColumn<SchemeLine,Double> columnDiscount;
    
    public void initData(Customer customer){
        this.customer=customer;
        contacts=customer.getContacts();
        phones=customer.getPhones();
        schemes=customer.getSchemes();
        
        //Company data
        lbComName.setText(customer.getComName());
        lbLegalName.setText(customer.getLegalName());
        lbWeb.setText(customer.getWeb());
        lbCompEmail.setText(customer.getEmail());
        lbVATNumber.setText(customer.getVatNumber());
        lbDefaultVAT.setText(String.valueOf(customer.getDefaultVAT())+"%");
        lbDuedate.setText(String.valueOf(customer.getDuedate()));
        lbDefaultWithholding.setText(String.valueOf(customer.getDefaultWithholding())+"%");
        lbInvoicingMethod.setText(customer.getInvoicingMethod());
        lbPayMethod.setText(customer.getPayMethod());
        lbStreet.setText(customer.getAddress().getStreet());
        lbStNumber.setText(customer.getAddress().getStNumber());
        lbCity.setText(customer.getAddress().getCity());
        lbState.setText(customer.getAddress().getState());
        lbApt.setText(customer.getAddress().getApt());
        lbCP.setText(customer.getAddress().getCp());
        lbCountry.setText(customer.getAddress().getCountry());
        
        if(customer.isEurope()){
            lbEurope.setText("Sí");
        }else{
            lbEurope.setText("No");
        }
        if(customer.isEnabled()){
            lbEnabled.setText("Sí");
        }else{
            lbEnabled.setText("No");
        }
        
        //Contact Person data
        showContacts();
        
        //Phone data
        showPhones();
        
        //Scheme
        showSchemes();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeLabelEditable(lbComName, fieldComName);
        makeLabelEditable(lbLegalName, fieldLegalName);
        makeLabelEditable(lbWeb, fieldWeb);
        makeLabelEditable(lbCompEmail, fieldCompEmail);
        makeLabelEditable(lbVATNumber, fieldVATNumber);
        makeLabelEditable(lbDefaultVAT, fieldDefaultVAT);
        makeLabelEditable(lbDuedate, fieldDuedate);
        makeLabelEditable(lbDefaultWithholding, fieldDefaultWithholding);
        makeLabelEditable(lbInvoicingMethod, fieldInvoicingMethod);
        makeLabelEditable(lbPayMethod, fieldPayMethod);
        makeLabelEditable(lbEurope, cbEurope);
        makeLabelEditable(lbEnabled, cbEnabled);
        makeLabelEditable(lbStreet, fieldStreet);
        makeLabelEditable(lbStNumber, fieldStNumber);
        makeLabelEditable(lbCity, fieldCity);
        makeLabelEditable(lbState, fieldState);
        makeLabelEditable(lbApt, fieldApt);
        makeLabelEditable(lbCP, fieldCP);
        makeLabelEditable(lbCountry, fieldCountry);
        makeLabelEditable(lbFirstname, fieldFirstname);
        makeLabelEditable(lbMiddlename, fieldMiddlename);
        makeLabelEditable(lbLastname, fieldLastname);
        makeLabelEditable(lbRole, fieldRole);
        makeLabelEditable(lbContactEmail, fieldContactEmail);
        makeLabelEditable(lbPhoneNumber, fieldPhoneNumber);
        makeLabelEditable(lbKind, fieldKind);
        makeLabelEditable(lbSchemeName, fieldSchemeName);
        makeLabelEditable(lbSourceLanguage, fieldSourceLanguage);
        makeLabelEditable(lbTargetLanguage, fieldTargetLanguage);
        makeLabelEditable(lbPrice, fieldPrice);
        makeLabelEditable(lbUnits, fieldUnits);
        makeLabelEditable(lbFieldName, fieldFieldName);
    }
    
    @FXML protected void onNextScheme(){
        iSchemes++;
        showSchemes();
    }
    
    @FXML protected void onPrevScheme(){
        iSchemes--;
        showSchemes();
    }
    
    @FXML protected void onNextContact(){
        iContacts++;
        showContacts();
    }
    
    @FXML protected void onPrevContact(){
        iContacts--;
        showContacts();
    }
    
    @FXML protected void onNextPhone(){
        iPhones++;
        showPhones();
    }
    
    @FXML protected void onPrevPhone(){
        iPhones--;
        showPhones();
    }
    
    @FXML protected void onClicAddContact(){
        ContactPerson contact=new ContactPerson();
        FXMLLoader loader=new FXMLLoader();
        Parent root=null;
        Scene scene;
        ViewNewContactController controller=null;
        
        loader.setLocation(getClass().getResource("viewNewContact.fxml"));
        try {
            root=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewCreateCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(contact);
        customer.addContactPerson(contact);
        
        scene=new Scene(root);
        Stage viewNewContact=new Stage();
        viewNewContact.setScene(scene);
        viewNewContact.show();
        
        viewNewContact.setOnHiding(event -> {
                paneDetailsCustomer.getParent().setDisable(false);
                showContacts();
            });
        
        paneDetailsCustomer.getParent().setDisable(true);
    }
    
    @FXML protected void onClicAddPhone(){
        Phone phone=new Phone();
        FXMLLoader loader=new FXMLLoader();
        Parent root=null;
        Scene scene;
        Stage stage;
        ViewNewPhoneController controller=null;
        
        loader.setLocation(getClass().getResource("viewNewPhone.fxml"));
        try {
            root=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewCreateCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(phone);
        customer.addPhone(phone);
        
        scene=new Scene(root);
        Stage viewNewPhone=new Stage();
        viewNewPhone.setScene(scene);
        viewNewPhone.show();
        
        viewNewPhone.setOnHiding(event -> {
                paneDetailsCustomer.getParent().setDisable(false);
                showPhones();
            });
        
        paneDetailsCustomer.getParent().setDisable(true);
    }
    
    @FXML protected void onClicAddScheme(){
        Scheme scheme=new Scheme();
        FXMLLoader loader=new FXMLLoader();
        Parent root=null;
        Scene scene;
        ViewNewSchemeController controller=null;
        
        loader.setLocation(getClass().getResource("viewNewScheme.fxml"));
        try {
            root=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewCreateCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(scheme);
        customer.addScheme(scheme);
        
        scene=new Scene(root);
        Stage viewNewScheme=new Stage();
        viewNewScheme.setScene(scene);
        viewNewScheme.show();
        
        viewNewScheme.setOnHiding(event -> {
                paneDetailsCustomer.getParent().setDisable(false);
                showSchemes();
            });
        
        paneDetailsCustomer.getParent().setDisable(true);
    }
    
    private void showContacts(){
        btnContactLeft.setVisible(true);
        btnContactRight.setVisible(true);
        
        if(iContacts==0){
            btnContactLeft.setVisible(false);
        }

        if(iContacts<contacts.size()){
            gridContacts.setDisable(false);
            btnNewContact.setVisible(false);
            lbFirstname.setText(contacts.get(iContacts).getFirstname());
            lbMiddlename.setText(contacts.get(iContacts).getMiddlename());
            lbLastname.setText(contacts.get(iContacts).getLastname());
            lbRole.setText(contacts.get(iContacts).getRole());
            lbContactEmail.setText(contacts.get(iContacts).getEmail());
        }
        else{
            gridContacts.setDisable(true);
            btnNewContact.setVisible(true);
            btnContactRight.setVisible(false);
        }
    }
    
    private void showPhones(){
        
        btnPhoneLeft.setVisible(true);
        btnPhoneRight.setVisible(true);
        
        if(iPhones==0){
            btnPhoneLeft.setVisible(false);
        }
        
        if(iPhones<phones.size()){
            gridPhones.setDisable(false);
            btnNewPhone.setVisible(false);
            lbKind.setText(phones.get(iPhones).getPhoneNumber());
            lbPhoneNumber.setText(phones.get(iPhones).getKind());
        }
        else{
            gridPhones.setDisable(true);
            btnNewPhone.setVisible(true);
            btnPhoneRight.setVisible(false);
        }
    }
    
    private void showSchemes(){
        btnSchemeLeft.setVisible(true);
        btnSchemeRight.setVisible(true);
        
        if(iSchemes==0){
            btnSchemeLeft.setVisible(false);
        }
        
        if(iSchemes<schemes.size()){
            gridScheme.setDisable(false);
            btnNewScheme.setVisible(false);
            lbSchemeName.setText(schemes.get(iSchemes).getName());
            lbSourceLanguage.setText(schemes.get(iSchemes).getSourceLanguage());
            lbTargetLanguage.setText(schemes.get(iSchemes).getTargetLanguage());
            lbPrice.setText(String.valueOf(schemes.get(iSchemes).getPrice()));
            lbUnits.setText(schemes.get(iSchemes).getUnits());
            lbFieldName.setText(schemes.get(iSchemes).getField());
            createTableSchemeLines(schemes.get(iSchemes));
        }else{
            gridScheme.setDisable(true);
            btnNewScheme.setVisible(true);
            btnSchemeRight.setVisible(false);
        }
    }
    
    private void createTableSchemeLines(Scheme scheme){
        ObservableList<SchemeLine> lines=FXCollections.observableArrayList(scheme.getLines());
        
        columnDescription.setCellValueFactory(new PropertyValueFactory<SchemeLine, String>("description"));
        columnDiscount.setCellValueFactory(new PropertyValueFactory<SchemeLine,Double>("discount"));
        
        tableSchemeLines.setItems(lines);
        
    }

    private void makeLabelEditable(Label label, TextField textField) {
        textField.setVisible(false);

        label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getClickCount() == 2) {
                switchToTextField(label, textField);
            }
        });

        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                switchToLabel(label, textField);
            }
        });
    }
    
    private void makeLabelEditable(Label label, CheckBox checkBox) {
        checkBox.setVisible(false);

        label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getClickCount() == 2) {
                switchToCheckBox(label, checkBox);
            }
        });

        checkBox.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                switchToLabel(label, checkBox);
            }
        });
    }
    
    private void switchToTextField(Label label, TextField textField) {
        textField.setText(label.getText());
        textField.setVisible(true);
        textField.requestFocus();
        label.setVisible(false);
    }
    
    private void switchToCheckBox(Label label, CheckBox checkBox) {
        if(label.getText().equals("Sí")){
            checkBox.setSelected(true);
        }else{
            checkBox.setSelected(false);
        }
        checkBox.requestFocus();
        checkBox.setVisible(true);
        label.setVisible(false);
    }
    
    private void switchToLabel(Label label, TextField textField) {
        label.setText(textField.getText());
        label.setVisible(true);
        textField.setVisible(false);
        
        
    }
    
    private void switchToLabel(Label label, CheckBox checkBox) {
        if(checkBox.isSelected()){
            label.setText("Sí");
        }else{
            label.setText("No");
        }
        
        label.setVisible(true);
        checkBox.setVisible(false);
    }

}
