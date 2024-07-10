/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.BankAccount;
import invoicingapp_monolithic.Company;
import invoicingapp_monolithic.ContactPerson;
import invoicingapp_monolithic.CustomProv;
import invoicingapp_monolithic.Phone;
import invoicingapp_monolithic.Provider;
import invoicingapp_monolithic.Scheme;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewCreateProviderController implements Initializable {

    private String introCompany="Datos de la Empresa";
    private String introFiscalData="Datos fiscales";
    private Provider provider=new Provider();
    private ArrayList<CustomProv> companies=new ArrayList();
    private Stage stage;
    
    @FXML private Label labelIntro, labelError;
    @FXML private TextField fieldVATNumber,fieldComName,fieldLegalName,fieldEmailCompany,fieldWeb;
    @FXML private TextField fieldDefaultVAT,fieldDefaultWithholding;
    @FXML private CheckBox cbEurope,cbEnabled;
    @FXML ComboBox cbCompany;
    @FXML ChoiceBox<String> cbDefaultLanguage;
    @FXML private GridPane paneCompany,paneFiscalData;
    @FXML HBox paneFootCompany,paneFootFiscalData;
    @FXML VBox paneCreateProvider;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        companies=CustomProv.getAllCustomProvFromDB();
        labelIntro.setText(introCompany);
        cbDefaultLanguage.getItems().addAll("English","Español","Français");
    }    
    
    @FXML protected void onClicCancel(){
        stage=(Stage)paneCreateProvider.getScene().getWindow();
        stage.close();
    }
    
    @FXML protected void onClicNext(){
        boolean control=true;
        
        labelIntro.setText(introFiscalData);
        labelError.setVisible(false);
        fieldVATNumber.getStyleClass().remove("error");
        fieldLegalName.getStyleClass().remove("error");
        
        if(fieldVATNumber.getText().isEmpty()){
            labelError.setText("Falta un dato obligatorio.");
            labelError.setVisible(true);
            fieldVATNumber.getStyleClass().add("error");
            control=false;
        }
        if(fieldLegalName.getText().isEmpty()){
            labelError.setText("Falta un dato obligatorio.");
            labelError.setVisible(true);
            fieldLegalName.getStyleClass().add("error");
            control=false;
        }
        if(control){
            provider.setVatNumber(fieldVATNumber.getText());
            provider.setComName(fieldComName.getText());
            provider.setLegalName(fieldLegalName.getText());
            provider.setEmail(fieldEmailCompany.getText());
            provider.setWeb(fieldWeb.getText());
            
            paneCompany.setVisible(false);
            paneFootCompany.setVisible(false);
            paneFiscalData.setVisible(true);
            paneFootFiscalData.setVisible(true);
        }        
    }
    
    @FXML protected void onClicBack(){
        labelError.setVisible(false);
        labelIntro.setText(introCompany);
        paneCompany.setVisible(true);
        paneFiscalData.setVisible(false);
        paneFootCompany.setVisible(true);
        paneFootFiscalData.setVisible(false);
    }
    
    @FXML protected void onClicSave(){
        boolean control=true;
        
        if(provider.getAddress().getStreet()==null){
            labelError.setText("Es obligatorio añadir una dirección.");
            labelError.setVisible(true);
            control=false;
        }
        stage=(Stage)paneCreateProvider.getScene().getWindow();
        double defaultVAT=0,defaultWithholding=0;
        
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
        
        if(!control){
            labelError.setText("Un dato introducido no es correcto.");
            labelError.setVisible(true);
        }else{
            provider.setDefaultVAT(defaultVAT);
            provider.setDefaultWithholding(defaultWithholding);
            provider.setEurope(cbEurope.isSelected());
            provider.setEnabled(cbEnabled.isSelected());
            provider.setDefaultLanguage(cbDefaultLanguage.getValue());
            provider.addToDB();
        
            stage.close();
        }
    }
        
    @FXML protected void onClicAddAddress(){
        ViewNewAddressController controller=null;
        FXMLLoader loader=switchWindow("viewNewAddress.fxml");
        
        controller=loader.getController();
        controller.initData(provider.getAddress());
    }
    
    @FXML protected void onClicAddContact(){
        ViewNewContactController controller=null;
        FXMLLoader loader=switchWindow("viewNewContact.fxml");
        ContactPerson contact=new ContactPerson();
        
        controller=loader.getController();
        controller.initData(contact);
        provider.addContactPerson(contact);
    }
    
    @FXML protected void onClicAddPhone(){
        ViewNewPhoneController controller=null;
        FXMLLoader loader=switchWindow("viewNewPhone.fxml");
        Phone phone=new Phone();
        
        controller=loader.getController();
        controller.initData(phone);
        provider.addPhone(phone);
    }
    
    @FXML protected void onClicAddScheme(){
        ViewNewSchemeController controller=null;
        FXMLLoader loader=switchWindow("viewNewScheme.fxml");
        Scheme scheme=new Scheme();
        
        controller=loader.getController();
        controller.initData(scheme);
        provider.addScheme(scheme);
    }
    
    @FXML protected void onClicAddBankAccount(){
        ViewNewBankAccountController controller=null;
        FXMLLoader loader=switchWindow("viewNewBankAccount.fxml");
        BankAccount bankAccount=new BankAccount();
        
        controller=loader.getController();
        controller.initData(bankAccount);
        provider.addBankAccount(bankAccount);
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
        
        fieldVATNumber.setText(customProv.getVatNumber());
        fieldComName.setText(customProv.getComName());
        fieldLegalName.setText(customProv.getLegalName());
        fieldEmailCompany.setText(customProv.getEmail());
        fieldWeb.setText(customProv.getWeb());
        
        provider.setIdCompany(customProv.getIdCompany());
        provider.setAddress(customProv.getAddress());
    }
    
    private FXMLLoader switchWindow(String path){
        FXMLLoader loader=new FXMLLoader();
        Parent root=null;
        Scene scene;
        loader.setLocation(getClass().getResource(path));
        try {
            root=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewCreateCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        scene=new Scene(root);
        Stage newStage=new Stage();
        newStage.setScene(scene);
        newStage.show();
        
        stage=(Stage)paneCreateProvider.getScene().getWindow();
        newStage.setOnHiding(event -> {
                stage.show();
            });
        
        stage.close();
        
        return loader;
    }

}