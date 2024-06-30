/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.bbdd.ConnectionDB;
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
    private ArrayList<Phone> newPhones=new ArrayList();
    private ArrayList<ContactPerson> contacts=new ArrayList();
    private ArrayList<ContactPerson> newContacts=new ArrayList();
    private ArrayList<Scheme> schemes=new ArrayList();
    private ArrayList<Scheme> newSchemes=new ArrayList();
    private int iContacts=0,iPhones=0,iSchemes=0;
    private String query="";
    
    @FXML Label lb_Company_ComName,lb_Company_LegalName,lb_Company_Web,lb_Company_Email,lb_Company_VATNumber,lb_CustomProv_DefaultVAT,
            lb_Customer_Duedate,lb_CustomProv_DefaultWithholding,lb_Customer_InvoicingMethod,lb_Customer_PayMethod,lb_CustomProv_Europe,
            lb_CustomProv_Enabled,lb_Address_Street,lb_Address_StNumber,lb_Address_City,lb_Address_State,lb_Address_Apt,lb_Address_CP,lb_Address_Country,
            lb_ContactPerson_Firstname,lb_ContactPerson_Middlename,lb_ContactPerson_Lastname,lb_ContactPerson_Role,lb_ContactPerson_Email,
            lb_Phone_PhoneNumber,lb_Phone_Kind,lb_Scheme_SchemeName,lb_Scheme_SourceLanguage,lb_Scheme_TargetLanguage,
            lb_Scheme_Price,lb_Scheme_Units,lb_Scheme_FieldName;
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
        lb_Company_ComName.setText(customer.getComName());
        lb_Company_LegalName.setText(customer.getLegalName());
        lb_Company_Web.setText(customer.getWeb());
        lb_Company_Email.setText(customer.getEmail());
        lb_Company_VATNumber.setText(customer.getVatNumber());
        lb_CustomProv_DefaultVAT.setText(String.valueOf(customer.getDefaultVAT())+"%");
        lb_Customer_Duedate.setText(String.valueOf(customer.getDuedate()));
        lb_CustomProv_DefaultWithholding.setText(String.valueOf(customer.getDefaultWithholding())+"%");
        lb_Customer_InvoicingMethod.setText(customer.getInvoicingMethod());
        lb_Customer_PayMethod.setText(customer.getPayMethod());
        lb_Address_Street.setText(customer.getAddress().getStreet());
        lb_Address_StNumber.setText(customer.getAddress().getStNumber());
        lb_Address_City.setText(customer.getAddress().getCity());
        lb_Address_State.setText(customer.getAddress().getState());
        lb_Address_Apt.setText(customer.getAddress().getApt());
        lb_Address_CP.setText(customer.getAddress().getCp());
        lb_Address_Country.setText(customer.getAddress().getCountry());
        
        if(customer.isEurope()){
            lb_CustomProv_Europe.setText("Sí");
        }else{
            lb_CustomProv_Europe.setText("No");
        }
        if(customer.isEnabled()){
            lb_CustomProv_Enabled.setText("Sí");
        }else{
            lb_CustomProv_Enabled.setText("No");
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
        makeLabelEditable(lb_Company_ComName, fieldComName);
        makeLabelEditable(lb_Company_LegalName, fieldLegalName);
        makeLabelEditable(lb_Company_Web, fieldWeb);
        makeLabelEditable(lb_Company_Email, fieldCompEmail);
        makeLabelEditable(lb_Company_VATNumber, fieldVATNumber);
        makeLabelEditable(lb_CustomProv_DefaultVAT, fieldDefaultVAT);
        makeLabelEditable(lb_Customer_Duedate, fieldDuedate);
        makeLabelEditable(lb_CustomProv_DefaultWithholding, fieldDefaultWithholding);
        makeLabelEditable(lb_Customer_InvoicingMethod, fieldInvoicingMethod);
        makeLabelEditable(lb_Customer_PayMethod, fieldPayMethod);
        makeLabelEditable(lb_CustomProv_Europe, cbEurope);
        makeLabelEditable(lb_CustomProv_Enabled, cbEnabled);
        makeLabelEditable(lb_Address_Street, fieldStreet);
        makeLabelEditable(lb_Address_StNumber, fieldStNumber);
        makeLabelEditable(lb_Address_City, fieldCity);
        makeLabelEditable(lb_Address_State, fieldState);
        makeLabelEditable(lb_Address_Apt, fieldApt);
        makeLabelEditable(lb_Address_CP, fieldCP);
        makeLabelEditable(lb_Address_Country, fieldCountry);
        makeLabelEditable(lb_ContactPerson_Firstname, fieldFirstname);
        makeLabelEditable(lb_ContactPerson_Middlename, fieldMiddlename);
        makeLabelEditable(lb_ContactPerson_Lastname, fieldLastname);
        makeLabelEditable(lb_ContactPerson_Role, fieldRole);
        makeLabelEditable(lb_ContactPerson_Email, fieldContactEmail);
        makeLabelEditable(lb_Phone_PhoneNumber, fieldPhoneNumber);
        makeLabelEditable(lb_Phone_Kind, fieldKind);
        makeLabelEditable(lb_Scheme_SchemeName, fieldSchemeName);
        makeLabelEditable(lb_Scheme_SourceLanguage, fieldSourceLanguage);
        makeLabelEditable(lb_Scheme_TargetLanguage, fieldTargetLanguage);
        makeLabelEditable(lb_Scheme_Price, fieldPrice);
        makeLabelEditable(lb_Scheme_Units, fieldUnits);
        makeLabelEditable(lb_Scheme_FieldName, fieldFieldName);
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
        newContacts.add(contact);
        
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
        newPhones.add(phone);
        
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
        newSchemes.add(scheme);
        
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
    
    @FXML protected void onClicSave(){
        ConnectionDB con=new ConnectionDB();
        
        con.openConnection();
        con.noReturnQuery(query);
        con.closeConnection();
        
        for(int i=0;i<newContacts.size();i++){
            newContacts.get(i).addToDB();
        }
        
        for(int j=0;j<newPhones.size();j++){
            newPhones.get(j).addToDB();
        }
        
        for(int k=0;k<schemes.size();k++){
            newSchemes.get(k).addToDB();
        }
        
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
            lb_ContactPerson_Firstname.setText(contacts.get(iContacts).getFirstname());
            lb_ContactPerson_Middlename.setText(contacts.get(iContacts).getMiddlename());
            lb_ContactPerson_Lastname.setText(contacts.get(iContacts).getLastname());
            lb_ContactPerson_Role.setText(contacts.get(iContacts).getRole());
            lb_ContactPerson_Email.setText(contacts.get(iContacts).getEmail());
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
            lb_Phone_Kind.setText(phones.get(iPhones).getPhoneNumber());
            lb_Phone_PhoneNumber.setText(phones.get(iPhones).getKind());
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
            lb_Scheme_SchemeName.setText(schemes.get(iSchemes).getName());
            lb_Scheme_SourceLanguage.setText(schemes.get(iSchemes).getSourceLanguage());
            lb_Scheme_TargetLanguage.setText(schemes.get(iSchemes).getTargetLanguage());
            lb_Scheme_Price.setText(String.valueOf(schemes.get(iSchemes).getPrice()));
            lb_Scheme_Units.setText(schemes.get(iSchemes).getUnits());
            lb_Scheme_FieldName.setText(schemes.get(iSchemes).getField());
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
        
        getQuery(label);
        
    }
    
    private void switchToLabel(Label label, CheckBox checkBox) {
        if(checkBox.isSelected()){
            label.setText("Sí");
        }else{
            label.setText("No");
        }
        
        label.setVisible(true);
        checkBox.setVisible(false);
        
        getQuery(label);
    }
    
    private void getQuery(Label label){
        String fixId=label.getId();
        String table=fixId.substring(fixId.indexOf("_",0)+1,fixId.indexOf("_",3));
        String field=fixId.substring(fixId.indexOf("_",3)+1);
        String newValue=label.getText();
        if(table.equals("Phone")){
            query=query.concat("UPDATE Phone SET"+field+"="+newValue+" WHERE PhoneNumber="+customer.getPhones().get(iPhones).getPhoneNumber()+";");
        }else{
            if(field.equals("Europe")||field.equals("Enabled")){
                if(newValue.equals("Sí")){
                    newValue="1";
                }else if(newValue.equals("No")){
                    newValue="0";
                }
            }
            query=query.concat("UPDATE "+table+" SET "+field+"='"+newValue+"' WHERE id"+table+"="+getID(table)+";");
        }
    }
    
    private int getID(String table){
        int id=0;
        
        switch(table){
            case("Address"):id=customer.getAddress().getIdAddress();break;
            case("Company"):id=customer.getIdCompany();break;
            case("CustomProv"):id=customer.getIdCustomProv();break;
            case("Customer"):id=customer.getIdCustomer();break;
            case("ContactPerson"):id=customer.getContacts().get(iContacts).getIdContactPerson();break;
            case("Scheme"):id=customer.getSchemes().get(iSchemes).getIdScheme();break;
        }
        
        return id;
    }

}
