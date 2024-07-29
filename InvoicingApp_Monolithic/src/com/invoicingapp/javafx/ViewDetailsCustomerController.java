/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.bbdd.ConnectionDB;
import com.invoicingapp.config.Translations;
import com.invoicingapp.tools.LabelFeatures;
import invoicingapp_monolithic.BankAccount;
import invoicingapp_monolithic.ContactPerson;
import invoicingapp_monolithic.Customer;
import invoicingapp_monolithic.Phone;
import invoicingapp_monolithic.Scheme;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class ViewDetailsCustomerController implements Initializable {

    private Customer customer;
    private ArrayList<Phone> phones=new ArrayList();
    private ArrayList<Phone> newPhones=new ArrayList();
    private ArrayList<ContactPerson> contacts=new ArrayList();
    private ArrayList<ContactPerson> newContacts=new ArrayList();
    private ArrayList<Scheme> schemes=new ArrayList();
    private ArrayList<Scheme> newSchemes=new ArrayList();
    private ArrayList<BankAccount> accounts=new ArrayList();
    private ArrayList<BankAccount> newAccounts=new ArrayList();
    private int iContacts=0,iPhones=0,iSchemes=0,iAccounts=0;
    private ArrayList<String> query=new ArrayList();
    private boolean changes=false;
    private LabelFeatures lbFeatures=new LabelFeatures();
    private ViewContactController contactController=null;
    private ViewPhonesController phonesController=null;
    private ViewSchemesController schemesController=null;
    private ViewAccountsController accountsController=null;
    
    @FXML private Label lb_Company_ComName,lb_Company_LegalName,lb_Company_Web,lb_Company_Email,lb_Company_VATNumber,lb_CustomProv_DefaultVAT,lb_Company_DefaultLanguage,
            lb_Customer_Duedate,lb_CustomProv_DefaultWithholding,lb_Customer_InvoicingMethod,lb_Customer_PayMethod,lb_CustomProv_Europe,
            lb_CustomProv_Enabled,lb_Address_Street,lb_Address_StNumber,lb_Address_City,lb_Address_State,lb_Address_Apt,lb_Address_CP,lb_Address_Country;
    @FXML private TextField fieldComName,fieldLegalName,fieldWeb,fieldCompEmail,fieldVATNumber,fieldDefaultVAT,
            fieldDuedate,fieldDefaultWithholding,fieldInvoicingMethod,fieldPayMethod,
            fieldStreet,fieldStNumber,fieldCity,fieldState,fieldApt,fieldCP,fieldCountry;
    @FXML private CheckBox cbEurope,cbEnabled;
    @FXML private ChoiceBox<String> cbLanguage;
    @FXML private Button btnContactLeft,btnContactRight,btnPhoneLeft,btnPhoneRight,btnNewContact,btnNewPhone,
            btnSchemeLeft,btnSchemeRight, btnNewScheme,btnNewBankAccount,btnAccountLeft,btnAccountRight,
            btnDeleteScheme,btnDeletePhone,btnDeleteContact,btnDeleteBankAccount;
    @FXML private ScrollPane paneDetailsCustomer;
    @FXML private StackPane paneContacts,panePhones,paneSchemes,paneAccounts;
    
    public void initData(Customer customer){
        
        this.customer=customer;
        contacts=customer.getContacts();
        phones=customer.getPhones();
        schemes=customer.getSchemes();
        accounts=customer.getBankAccounts();
        
        lbFeatures.makeLabelEditable(lb_Company_ComName, fieldComName,"Company","comName",customer.getIdCompany());
        lbFeatures.makeLabelEditable(lb_Company_LegalName, fieldLegalName,"Company","legalName",customer.getIdCompany());
        lbFeatures.makeLabelEditable(lb_Company_Web, fieldWeb,"Company","web",customer.getIdCompany());
        lbFeatures.makeLabelEditable(lb_Company_Email, fieldCompEmail,"Company","email",customer.getIdCompany());
        lbFeatures.makeLabelEditable(lb_Company_VATNumber, fieldVATNumber,"Company","vatNumber",customer.getIdCompany());
        lbFeatures.makeLabelEditable(lb_Company_DefaultLanguage,cbLanguage,"Company","defaultLanguage",customer.getIdCompany());
        lbFeatures.makeLabelEditable(lb_CustomProv_DefaultVAT, fieldDefaultVAT,"CustomeProv","defaultVAT",customer.getIdCustomProv());
        lbFeatures.makeLabelEditable(lb_Customer_Duedate, fieldDuedate,"Customer","dueDate",customer.getIdCustomer());
        lbFeatures.makeLabelEditable(lb_CustomProv_DefaultWithholding, fieldDefaultWithholding,"CustomProv","defaultWithholding",customer.getIdCustomProv());
        lbFeatures.makeLabelEditable(lb_Customer_InvoicingMethod, fieldInvoicingMethod,"Customer","invoicingMethod",customer.getIdCustomer());
        lbFeatures.makeLabelEditable(lb_Customer_PayMethod, fieldPayMethod,"Customer","payMethod",customer.getIdCustomer());
        lbFeatures.makeLabelEditable(lb_CustomProv_Europe, cbEurope,"CustomProv","europe",customer.getIdCustomProv());
        lbFeatures.makeLabelEditable(lb_CustomProv_Enabled, cbEnabled,"CustomProv","enabled",customer.getIdCustomProv());
        lbFeatures.makeLabelEditable(lb_Address_Street, fieldStreet,"Address","street",customer.getAddress().getIdAddress());
        lbFeatures.makeLabelEditable(lb_Address_StNumber, fieldStNumber,"Address","stNumber",customer.getAddress().getIdAddress());
        lbFeatures.makeLabelEditable(lb_Address_City, fieldCity,"Address","city",customer.getAddress().getIdAddress());
        lbFeatures.makeLabelEditable(lb_Address_State, fieldState,"Address","state",customer.getAddress().getIdAddress());
        lbFeatures.makeLabelEditable(lb_Address_Apt, fieldApt,"Address","apt",customer.getAddress().getIdAddress());
        lbFeatures.makeLabelEditable(lb_Address_CP, fieldCP,"Address","cp",customer.getAddress().getIdAddress());
        lbFeatures.makeLabelEditable(lb_Address_Country, fieldCountry,"Address","country",customer.getAddress().getIdAddress());
        
        //Company data
        lb_Company_ComName.setText(customer.getComName());
        lb_Company_LegalName.setText(customer.getLegalName());
        lb_Company_Web.setText(customer.getWeb());
        lb_Company_Email.setText(customer.getEmail());
        lb_Company_VATNumber.setText(customer.getVatNumber());
        lb_Company_DefaultLanguage.setText(customer.getDefaultLanguage());
        lb_CustomProv_DefaultVAT.setText(String.valueOf(customer.getDefaultVAT())+"%");
        lb_Customer_Duedate.setText(String.valueOf(customer.getDuedate()));
        lb_CustomProv_DefaultWithholding.setText(String.valueOf(customer.getDefaultWithholding())+"%");
        lb_Customer_InvoicingMethod.setText(customer.getInvoicingMethod());
        lb_Customer_PayMethod.setText(customer.getPayMethod());
        //ADdress data
        lb_Address_Street.setText(customer.getAddress().getStreet());
        lb_Address_StNumber.setText(customer.getAddress().getStNumber());
        lb_Address_City.setText(customer.getAddress().getCity());
        lb_Address_State.setText(customer.getAddress().getState());
        lb_Address_Apt.setText(customer.getAddress().getApt());
        lb_Address_CP.setText(customer.getAddress().getCp());
        lb_Address_Country.setText(customer.getAddress().getCountry());
        //Others
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
        
        //Scheme data
        showSchemes();
        
        //Bank Accounts data
        showAccounts();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbLanguage.getItems().addAll(Translations.languages);
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
        updateContacts();
    }
    
    @FXML protected void onPrevContact(){
        iContacts--;
        updateContacts();
    }
    
    @FXML protected void onNextPhone(){
        iPhones++;
        showPhones();
    }
    
    @FXML protected void onPrevPhone(){
        iPhones--;
        showPhones();
    }
    
    @FXML protected void onPrevAccount(){
        iAccounts--;
        showAccounts();
    }
    
    @FXML protected void onNextAccount(){
        iAccounts++;
        showAccounts();
    }
    
    @FXML protected void onClicAddContact(){
        ContactPerson contact=new ContactPerson();
        FXMLLoader loader=new FXMLLoader();
        Parent root=null;
        Stage viewNewContact=new Stage();
        Scene scene;
        ViewNewContactController controller=null;
        
        loader.setLocation(getClass().getResource("viewNewContact.fxml"));
        try {
            root=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewDetailsCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(contact);
        scene=new Scene(root);
        viewNewContact.setScene(scene);
        viewNewContact.show();
        
        viewNewContact.setOnHiding(event -> {
                paneDetailsCustomer.getParent().setDisable(false);
                if(contact.getFirstname()!=null){
                    customer.addContactPerson(contact);
                    newContacts.add(contact);
                    changes=true;
                }
                showContacts();
            });
        
        paneDetailsCustomer.getParent().setDisable(true);
    }
    
    @FXML protected void onClicDeteleContact(){
        ConfirmationDialog.show("¿Está seguro de querer eliminar este contacto?", this::deleteContact, () -> {});
    }
    
    @FXML protected void onClicAddPhone(){
        Phone phone=new Phone();
        FXMLLoader loader=new FXMLLoader();
        Parent root=null;
        Scene scene;
        Stage viewNewPhone=new Stage();
        ViewNewPhoneController controller=null;
        
        loader.setLocation(getClass().getResource("viewNewPhone.fxml"));
        try {
            root=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewDetailsCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(phone);
        scene=new Scene(root);
        viewNewPhone.setScene(scene);
        viewNewPhone.show();
        
        viewNewPhone.setOnHiding(event -> {
                paneDetailsCustomer.getParent().setDisable(false);
                if(phone.getPhoneNumber()!=null){
                    customer.addPhone(phone);
                    newPhones.add(phone);
                    changes=true;
                }
                showPhones();
            });
        
        paneDetailsCustomer.getParent().setDisable(true);
    }
    
    @FXML protected void onClicDeletePhone(){
        ConfirmationDialog.show("¿Está seguro de querer eliminar este teléfono?", this::deletePhone, () -> {});
    }
    
    @FXML protected void onClicAddScheme(){
        Scheme scheme=new Scheme();
        FXMLLoader loader=new FXMLLoader();
        Parent root=null;
        Scene scene;
        Stage viewNewScheme=new Stage();
        ViewNewSchemeController controller=null;
        
        loader.setLocation(getClass().getResource("viewNewScheme.fxml"));
        try {
            root=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewDetailsCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(scheme);
        scene=new Scene(root);
        viewNewScheme.setScene(scene);
        viewNewScheme.show();
        
        viewNewScheme.setOnHiding(event -> {
                paneDetailsCustomer.getParent().setDisable(false);
                if(scheme.getName()!=null){
                    changes=true;
                    customer.addScheme(scheme);
                    newSchemes.add(scheme);
                }                
                showSchemes();
            });
        
        paneDetailsCustomer.getParent().setDisable(true);
    }
    
    @FXML protected void onClicDeleteScheme(){
        ConfirmationDialog.show("¿Está seguro de querer eliminar este esquema?", this::deleteScheme, () -> {});
    }
    
    @FXML protected void onClicAddBankAccount(){
        BankAccount account=new BankAccount();
        FXMLLoader loader=new FXMLLoader();
        Parent root=null;
        Scene scene;
        Stage viewNewBankAccount=new Stage();
        ViewNewBankAccountController controller=null;
        
        loader.setLocation(getClass().getResource("viewNewBankAccount.fxml"));
        try {
            root=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewDetailsCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(account);
        scene=new Scene(root);
        viewNewBankAccount.setScene(scene);
        viewNewBankAccount.show();
        
        viewNewBankAccount.setOnHiding(event -> {
                paneDetailsCustomer.getParent().setDisable(false);
                if(account.getIban()!=null){
                    changes=true;
                    customer.addBankAccount(account);
                    newAccounts.add(account);
                }
                showAccounts();
            });
        
        paneDetailsCustomer.getParent().setDisable(true);
    }
    
    @FXML protected void onClicDeleteBankAccount(){
        ConfirmationDialog.show("¿Está seguro de querer eliminar esta cuenta bancaria?", this::deleteBankAccount, () -> {});
    }
    
    @FXML protected void onClicSave(){
        ConnectionDB con=new ConnectionDB();
        getQuery();
        if(!query.isEmpty()){
            con.openConnection();
            for(int i=0;i<query.size();i++){
                con.executeUpdate(query.get(i));
            }
            con.closeConnection();
            contactController.resetQuery();
            phonesController.resetQuery();
            schemesController.resetQuery();
            accountsController.resetQuery();
            lbFeatures.resetQuery();
            query.clear();
        }
        
        for(int i=0;i<newContacts.size();i++){
            newContacts.get(i).addToDB();
        }
        
        for(int j=0;j<newPhones.size();j++){
            newPhones.get(j).addToDB();
        }
        
        for(int k=0;k<newSchemes.size();k++){
            newSchemes.get(k).addToDB();
        }
        
        for(int l=0;l<newAccounts.size();l++){
            newAccounts.get(l).addToDB();
        }
        changes=false;
    }
    
    @FXML protected void onClicDelete(){
        ConfirmationDialog.show("¿Está seguro de querer eliminar este cliente?", this::deleteCustomer, () -> {});
    }
    
    @FXML protected void onClicback(){
        getQuery();
        if(changes&&!query.isEmpty()){
            ConfirmationDialog.show("Hay cambios sin guardar. ¿Está seguro de querer volver sin guardar los cambios?", this::backToViewCustomers, () -> {});
        }else{
            backToViewCustomers();
        }
    }
    
    @FXML protected void onClicNewOrder(){
        FXMLLoader loader=new FXMLLoader();
        Parent newOrderView=null;
        ViewNewOrderController controller=null;
        BorderPane home=(BorderPane)paneDetailsCustomer.getParent();
        
        loader.setLocation(getClass().getResource("viewNewOrder.fxml"));
        try {
            newOrderView=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewDetailsCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(customer,0);
        home.setCenter(newOrderView);
    }
    
    @FXML protected void onClicNewInvoice(){
        FXMLLoader loader=new FXMLLoader();
        Parent invoiceCustomerView=null;
        ViewNewInvoiceCustomerController controller=null;
        BorderPane home=(BorderPane)paneDetailsCustomer.getParent();
        
        loader.setLocation(getClass().getResource("viewNewInvoiceCustomer.fxml"));
        try {
            invoiceCustomerView=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewDetailsCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(customer);
        home.setCenter(invoiceCustomerView);
    }
    
    @FXML protected void onClicNewQuote(){
        FXMLLoader loader=new FXMLLoader();
        Parent newQuoteView=null;
        ViewNewQuoteController controller=null;
        BorderPane home=(BorderPane)paneDetailsCustomer.getParent();
        
        loader.setLocation(getClass().getResource("viewNewQuote.fxml"));
        try {
            newQuoteView=loader.load();
            home.setCenter(newQuoteView);
        } catch (IOException ex) {
            Logger.getLogger(ViewDetailsCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        controller=loader.getController();
        controller.initData(customer);
        home.setCenter(newQuoteView);
    }
    
    private void showContacts(){
        FXMLLoader loader=new FXMLLoader();
        Parent contactsView=null;
        
        paneContacts.getChildren().clear();
        loader.setLocation(getClass().getResource("viewContact.fxml"));
        try{
            contactsView=loader.load();
        }catch (IOException ex) {
            Logger.getLogger(ViewDetailsCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        contactController=loader.getController();
        paneContacts.getChildren().add(contactsView);
        updateContacts();
    }
    
    private void showPhones(){
        FXMLLoader loader=new FXMLLoader();
        Parent phonesView=null;
        
        panePhones.getChildren().clear();
        loader.setLocation(getClass().getResource("viewPhones.fxml"));
        try{
            phonesView=loader.load();
        }catch (IOException ex) {
            Logger.getLogger(ViewDetailsCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        phonesController=loader.getController();
        panePhones.getChildren().add(phonesView);
        updatePhones();
    }
    
    private void showSchemes(){
        FXMLLoader loader=new FXMLLoader();
        Parent schemesView=null;
        
        paneSchemes.getChildren().clear();
        loader.setLocation(getClass().getResource("viewSchemes.fxml"));
        try{
            schemesView=loader.load();
        }catch (IOException ex) {
            Logger.getLogger(ViewDetailsCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        schemesController=loader.getController();
        paneSchemes.getChildren().add(schemesView);
        updateSchemes();
    }
    
    private void showAccounts(){
        FXMLLoader loader=new FXMLLoader();
        Parent accountsView=null;
        
        paneAccounts.getChildren().clear();
        loader.setLocation(getClass().getResource("viewAccounts.fxml"));
        try{
            accountsView=loader.load();
        }catch (IOException ex) {
            Logger.getLogger(ViewDetailsCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        accountsController=loader.getController();
        paneAccounts.getChildren().add(accountsView);
        updateAccounts();
    }
    
    public void updateAccounts(){        
        btnAccountLeft.setVisible(true);
        btnAccountRight.setVisible(true);
        btnDeleteBankAccount.setVisible(true);
        
        if(iAccounts==0){
            btnAccountLeft.setVisible(false);
        }
            
        if(iAccounts<accounts.size()){
            paneAccounts.setDisable(false);
            btnNewBankAccount.setVisible(false);
            accountsController.initData(accounts.get(iAccounts));
            if(iAccounts==0){
                btnAccountLeft.setVisible(false);
            }
        }else{
            paneAccounts.setDisable(true);
            btnNewBankAccount.setVisible(true);
            btnAccountRight.setVisible(false);
            btnDeleteBankAccount.setVisible(false);
        }
    }
    
    private void updateSchemes(){
        btnSchemeLeft.setVisible(true);
        btnSchemeRight.setVisible(true);
        btnDeleteScheme.setVisible(true);
        
        if(iSchemes<schemes.size()){
            paneSchemes.setDisable(false);
            btnNewScheme.setVisible(false);
            schemesController.initData(schemes.get(iSchemes));
            if(iSchemes==0){
               btnSchemeLeft.setVisible(false);
            }
        }else{
            paneSchemes.setDisable(true);
            btnNewScheme.setVisible(true);
            btnSchemeRight.setVisible(false);
            btnDeleteScheme.setVisible(false);
        }
    }
    
    private void updateContacts(){
        btnContactLeft.setVisible(true);
        btnContactRight.setVisible(true);
        btnDeleteContact.setVisible(true);

        if(iContacts<contacts.size()){
            paneContacts.setDisable(false);
            btnNewContact.setVisible(false);
            contactController.initData(contacts.get(iContacts));
            if(iContacts==0){
                btnContactLeft.setVisible(false);
            }
        }
        else{
            paneContacts.setDisable(true);
            btnNewContact.setVisible(true);
            btnContactRight.setVisible(false);
            btnDeleteContact.setVisible(false);
        }
    }
    
    private void updatePhones(){
        btnPhoneLeft.setVisible(true);
        btnPhoneRight.setVisible(true);
        btnDeletePhone.setVisible(true);
        
        if(iPhones<phones.size()){
            panePhones.setDisable(false);
            btnNewPhone.setVisible(false);
            
            phonesController.initData(phones.get(iPhones));
            if(iPhones==0){
                btnPhoneLeft.setVisible(false);
            }
        }
        else{
            panePhones.setDisable(true);
            btnNewPhone.setVisible(true);
            btnPhoneRight.setVisible(false);
            btnDeletePhone.setVisible(false);
        }
    }
    
    private void getQuery(){
        query=lbFeatures.getQuery();
        if(contactController!=null){
            query.addAll(contactController.getQuery());
        }
        if(phonesController!=null){
            query.addAll(phonesController.getQuery());
        }
        if(schemesController!=null){
            query.addAll(schemesController.getQuery());
        }
        if(accountsController!=null){
            query.addAll(accountsController.getQuery());
        }
    }
    
    private void deleteCustomer(){
        customer.deleteFromDB();
        backToViewCustomers();
    }
    
    private void deleteContact(){
        contacts.get(iContacts).deleteFromDB();
        contacts.remove(iContacts);
        if(iContacts>0){
            iContacts--;
        }
        showContacts();
    }
    
    private void deletePhone(){
        phones.get(iPhones).deleteFromDB();
        phones.remove(iPhones);
        if(iPhones>0){
            iPhones--;
        }
        showPhones();
    }
    
    private void deleteScheme(){
        schemes.get(iSchemes).deleteFromDB();
        schemes.remove(iSchemes);
        if(iSchemes>0){
            iSchemes--;
        }
        showSchemes();
    }
    
    private void deleteBankAccount(){
        accounts.get(iAccounts).deleteFromDB();
        accounts.remove(iAccounts);
        if(iAccounts>0){
            iAccounts--;
        }
        showAccounts();
    }
    
    private void backToViewCustomers(){
        BorderPane home=(BorderPane)paneDetailsCustomer.getParent();
        Parent customersView;
        try {
            customersView=FXMLLoader.load(getClass().getResource("viewCustomers.fxml"));
            home.setCenter(customersView);
        } catch (IOException ex) {
            Logger.getLogger(ViewDetailsCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
