/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.bbdd.ConnectionDB;
import com.invoicingapp.config.Translations;
import com.invoicingapp.tools.LabelFeatures;
import com.invoicingapp.tools.Validations;
import invoicingapp_monolithic.BankAccount;
import invoicingapp_monolithic.ContactPerson;
import invoicingapp_monolithic.Phone;
import invoicingapp_monolithic.Provider;
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


public class ViewDetailsProviderController implements Initializable {
    private ArrayList<Phone> phones=new ArrayList();
    private ArrayList<Phone> newPhones=new ArrayList();
    private ArrayList<ContactPerson> contacts=new ArrayList();
    private ArrayList<ContactPerson> newContacts=new ArrayList();
    private ArrayList<Scheme> schemes=new ArrayList();
    private ArrayList<Scheme> newSchemes=new ArrayList();
    private ArrayList<BankAccount> accounts=new ArrayList();
    private ArrayList<BankAccount> newAccounts=new ArrayList();
    private ArrayList<String> query=new ArrayList();
    private int iContacts=0,iPhones=0,iSchemes=0,iAccounts=0;
    private boolean changes=false;
    private final String errorFormat="Uno de los datos introducidos no es correcto.";
    private Provider provider=new Provider();
    private LabelFeatures lbFeatures=new LabelFeatures();
    private ViewContactController contactController=null;
    private ViewPhonesController phonesController=null;
    private ViewSchemesController schemesController=null;
    private ViewAccountsController accountsController=null;
    
    @FXML private Label lb_Company_ComName,lb_Company_LegalName,lb_Company_Web,lb_Company_Email,lb_Company_VATNumber,lb_CustomProv_DefaultVAT,lb_Company_DefaultLanguage,
            lb_CustomProv_DefaultWithholding,lb_CustomProv_Europe,
            lb_CustomProv_Enabled,lb_Address_Street,lb_Address_StNumber,lb_Address_City,lb_Address_State,lb_Address_Apt,lb_Address_CP,lb_Address_Country,
            lbError;
    @FXML private TextField fieldComName,fieldLegalName,fieldWeb,fieldCompEmail,fieldVATNumber,fieldDefaultVAT,fieldDefaultWithholding,
            fieldStreet,fieldStNumber,fieldCity,fieldState,fieldApt,fieldCP,fieldCountry;
    @FXML private CheckBox cbEurope,cbEnabled;
    @FXML private ChoiceBox<String> cbLanguage;
    @FXML private Button btnContactLeft,btnContactRight,btnPhoneLeft,btnPhoneRight,btnNewContact,btnNewPhone,
            btnSchemeLeft,btnSchemeRight, btnNewScheme,btnNewBankAccount,btnAccountLeft,btnAccountRight,
            btnDeleteScheme,btnDeletePhone,btnDeleteContact,btnDeleteBankAccount;
    @FXML private ScrollPane paneDetailsProvider;
    @FXML private StackPane paneContacts,panePhones,paneSchemes,paneAccounts;
    
    public void initData(Provider provider){
        
        this.provider=provider;
        contacts=provider.getContacts();
        phones=provider.getPhones();
        schemes=provider.getSchemes();
        accounts=provider.getBankAccounts();
        
        lbFeatures.makeLabelEditable(lb_Company_ComName, fieldComName,"Company","comName",provider.getIdCompany());
        lbFeatures.makeLabelEditable(lb_Company_LegalName, fieldLegalName,"Company","legalName",provider.getIdCompany());
        lbFeatures.makeLabelEditable(lb_Company_Web, fieldWeb,"Company","web",provider.getIdCompany());
        lbFeatures.makeLabelEditable(lb_Company_Email, fieldCompEmail,"Company","email",provider.getIdCompany());
        lbFeatures.makeLabelEditable(lb_Company_VATNumber, fieldVATNumber,"Company","vatNumber",provider.getIdCompany());
        lbFeatures.makeLabelEditable(lb_Company_DefaultLanguage,cbLanguage,"Company","defaultLanguage",provider.getIdCompany());
        lbFeatures.makeLabelEditable(lb_CustomProv_DefaultVAT, fieldDefaultVAT,"CustomeProv","defaultVAT",provider.getIdCustomProv());
        lbFeatures.makeLabelEditable(lb_CustomProv_DefaultWithholding, fieldDefaultWithholding,"CustomProv","defaultWithholding",provider.getIdCustomProv());
        lbFeatures.makeLabelEditable(lb_CustomProv_Europe, cbEurope,"CustomProv","europe",provider.getIdCustomProv());
        lbFeatures.makeLabelEditable(lb_CustomProv_Enabled, cbEnabled,"CustomProv","enabled",provider.getIdCustomProv());
        lbFeatures.makeLabelEditable(lb_Address_Street, fieldStreet,"Address","street",provider.getAddress().getIdAddress());
        lbFeatures.makeLabelEditable(lb_Address_StNumber, fieldStNumber,"Address","stNumber",provider.getAddress().getIdAddress());
        lbFeatures.makeLabelEditable(lb_Address_City, fieldCity,"Address","city",provider.getAddress().getIdAddress());
        lbFeatures.makeLabelEditable(lb_Address_State, fieldState,"Address","state",provider.getAddress().getIdAddress());
        lbFeatures.makeLabelEditable(lb_Address_Apt, fieldApt,"Address","apt",provider.getAddress().getIdAddress());
        lbFeatures.makeLabelEditable(lb_Address_CP, fieldCP,"Address","cp",provider.getAddress().getIdAddress());
        lbFeatures.makeLabelEditable(lb_Address_Country, fieldCountry,"Address","country",provider.getAddress().getIdAddress());
        
        //Company data
        lb_Company_ComName.setText(provider.getComName());
        lb_Company_LegalName.setText(provider.getLegalName());
        lb_Company_Web.setText(provider.getWeb());
        lb_Company_Email.setText(provider.getEmail());
        lb_Company_VATNumber.setText(provider.getVatNumber());
        lb_Company_DefaultLanguage.setText(provider.getDefaultLanguage());
        lb_CustomProv_DefaultVAT.setText(String.valueOf(provider.getDefaultVAT())+"%");
        lb_CustomProv_DefaultWithholding.setText(String.valueOf(provider.getDefaultWithholding())+"%");
        //ADdress data
        lb_Address_Street.setText(provider.getAddress().getStreet());
        lb_Address_StNumber.setText(provider.getAddress().getStNumber());
        lb_Address_City.setText(provider.getAddress().getCity());
        lb_Address_State.setText(provider.getAddress().getState());
        lb_Address_Apt.setText(provider.getAddress().getApt());
        lb_Address_CP.setText(provider.getAddress().getCp());
        lb_Address_Country.setText(provider.getAddress().getCountry());
        //Others
        if(provider.isEurope()){
            lb_CustomProv_Europe.setText("Sí");
        }else{
            lb_CustomProv_Europe.setText("No");
        }
        if(provider.isEnabled()){
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
            Logger.getLogger(ViewDetailsProviderController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(contact);
        scene=new Scene(root);
        viewNewContact.setScene(scene);
        viewNewContact.show();
        
        viewNewContact.setOnHiding(event -> {
                paneDetailsProvider.getParent().setDisable(false);
                if(contact.getFirstname()!=null){
                    provider.addContactPerson(contact);
                    newContacts.add(contact);
                    changes=true;
                }
                showContacts();
            });
        
        paneDetailsProvider.getParent().setDisable(true);
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
            Logger.getLogger(ViewDetailsProviderController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(phone);
        scene=new Scene(root);
        viewNewPhone.setScene(scene);
        viewNewPhone.show();
        
        viewNewPhone.setOnHiding(event -> {
                paneDetailsProvider.getParent().setDisable(false);
                if(phone.getPhoneNumber()!=null){
                    provider.addPhone(phone);
                    newPhones.add(phone);
                    changes=true;
                }
                showPhones();
            });
        
        paneDetailsProvider.getParent().setDisable(true);
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
            Logger.getLogger(ViewDetailsProviderController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(scheme);
        scene=new Scene(root);
        viewNewScheme.setScene(scene);
        viewNewScheme.show();
        
        viewNewScheme.setOnHiding(event -> {
                paneDetailsProvider.getParent().setDisable(false);
                if(scheme.getName()!=null){
                    changes=true;
                    provider.addScheme(scheme);
                    newSchemes.add(scheme);
                }                
                showSchemes();
            });
        
        paneDetailsProvider.getParent().setDisable(true);
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
            Logger.getLogger(ViewDetailsProviderController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(account);
        scene=new Scene(root);
        viewNewBankAccount.setScene(scene);
        viewNewBankAccount.show();
        
        viewNewBankAccount.setOnHiding(event -> {
                paneDetailsProvider.getParent().setDisable(false);
                if(account.getIban()!=null){
                    changes=true;
                    provider.addBankAccount(account);
                    newAccounts.add(account);
                }
                showAccounts();
            });
        
        paneDetailsProvider.getParent().setDisable(true);
    }
    
    @FXML protected void onClicDeleteBankAccount(){
        ConfirmationDialog.show("¿Está seguro de querer eliminar esta cuenta bancaria?", this::deleteBankAccount, () -> {});
    }
    
    @FXML protected void onClicSave(){
        ConnectionDB con=new ConnectionDB();
        getQuery();
        if(!query.isEmpty()){
            if(Validations.isDouble(lb_CustomProv_DefaultVAT, lbError, errorFormat)&&Validations.isDouble(lb_CustomProv_DefaultWithholding, lbError, errorFormat)){
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
        }
    }
    
    @FXML protected void onClicDelete(){
        ConfirmationDialog.show("¿Está seguro de querer eliminar este cliente?", this::deleteCustomer, () -> {});
    }
    
    @FXML protected void onClicback(){
        getQuery();
        if(!changes||!query.isEmpty()){
            ConfirmationDialog.show("Hay cambios sin guardar. ¿Está seguro de querer volver sin guardar los cambios?", this::backToViewProviders, () -> {});
        }else{
            backToViewProviders();
        }
    }
    
    @FXML protected void onClicNewOrder(){
        FXMLLoader loader=new FXMLLoader();
        Parent newOrderView=null;
        ViewNewOrderController controller=null;
        BorderPane home=(BorderPane)paneDetailsProvider.getParent();
        
        loader.setLocation(getClass().getResource("viewNewOrder.fxml"));
        try {
            newOrderView=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewDetailsCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(provider,1);
        home.setCenter(newOrderView);
    }
    
    @FXML protected void onClicNewInvoice(){
        FXMLLoader loader=new FXMLLoader();
        Parent invoiceProviderView=null;
        ViewNewInvoiceProviderController controller=null;
        BorderPane home=(BorderPane)paneDetailsProvider.getParent();
        
        loader.setLocation(getClass().getResource("viewNewInvoiceProvider.fxml"));
        try {
            invoiceProviderView=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewDetailsProviderController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(provider);
        home.setCenter(invoiceProviderView);
    }
    
    @FXML protected void onClicNewPO(){
        FXMLLoader loader=new FXMLLoader();
        Parent newPOView=null;
        ViewNewPOController controller=null;
        BorderPane home=(BorderPane)paneDetailsProvider.getParent();
        
        loader.setLocation(getClass().getResource("viewNewPO.fxml"));
        try {
            newPOView=loader.load();
            home.setCenter(newPOView);
        } catch (IOException ex) {
            Logger.getLogger(ViewDetailsProviderController.class.getName()).log(Level.SEVERE, null, ex);
        }
        controller=loader.getController();
        controller.initData(provider);
        home.setCenter(newPOView);
    }
    
    private void showContacts(){
        FXMLLoader loader=new FXMLLoader();
        Parent contactsView=null;
        
        paneContacts.getChildren().clear();
        loader.setLocation(getClass().getResource("viewContact.fxml"));
        try{
            contactsView=loader.load();
        }catch (IOException ex) {
            Logger.getLogger(ViewDetailsProviderController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ViewDetailsProviderController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ViewDetailsProviderController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ViewDetailsProviderController.class.getName()).log(Level.SEVERE, null, ex);
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
        provider.deleteFromDB();
        backToViewProviders();
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
    
    private void backToViewProviders(){
        BorderPane home=(BorderPane)paneDetailsProvider.getParent();
        Parent providersView;
        try {
            providersView=FXMLLoader.load(getClass().getResource("viewProviders.fxml"));
            home.setCenter(providersView);
        } catch (IOException ex) {
            Logger.getLogger(ViewDetailsCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
}
