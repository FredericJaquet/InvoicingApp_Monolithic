/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.BankAccount;
import invoicingapp_monolithic.Company;
import invoicingapp_monolithic.ContactPerson;
import invoicingapp_monolithic.CustomProv;
import invoicingapp_monolithic.Customer;
import invoicingapp_monolithic.Phone;
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


public class ViewCreateCustomerController implements Initializable {

    private String introCompany="Datos de la Empresa";
    private String introFiscalData="Datos fiscales";
    private Customer customer=new Customer();
    private ArrayList<CustomProv> companies=new ArrayList();
    private Stage stage;
    
    @FXML private Label labelIntro, labelError;
    @FXML private TextField fieldVAT,fieldComName,fieldLegalName,fieldEmailCompany,fieldWeb;
    @FXML private TextField fieldDefaultVAT,fieldDefaultWithholding,fieldInvoicingMethod,fieldPayMethod,fieldDuedate;
    @FXML private CheckBox cbEurope,cbEnabled;
    @FXML private ComboBox cbCompany;
    @FXML private ChoiceBox<String> cbLanguage;
    @FXML private GridPane paneCompany, paneFiscalData;
    @FXML private HBox paneFootCompany,paneFootFiscalData;
    @FXML private VBox paneCreateCustomer;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        companies=CustomProv.getAllCustomProvFromDB();        
        labelIntro.setText(introCompany);
        cbLanguage.getItems().addAll("Español", "English", "Français");
        
    }
    
    @FXML protected void onClicCancel(){
        stage=(Stage)paneCreateCustomer.getScene().getWindow();
        stage.close();
    }
    
    @FXML protected void onClicNext(){
        labelIntro.setText(introFiscalData);
        customer.setVatNumber(fieldVAT.getText());
        customer.setComName(fieldComName.getText());
        customer.setLegalName(fieldLegalName.getText());
        customer.setEmail(fieldEmailCompany.getText());
        customer.setWeb(fieldWeb.getText());
        
        paneCompany.setVisible(false);
        paneFootCompany.setVisible(false);
        paneFiscalData.setVisible(true);
        paneFootFiscalData.setVisible(true);
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
        if(customer.getAddress().getStreet()==null){
            labelError.setText("Es obligatorio añadir una dirección.");
            labelError.setVisible(true);
        }else{
            stage=(Stage)paneCreateCustomer.getScene().getWindow();
            double defaultVAT=0,defaultWithholding=0;
            int duedate=0;
            boolean control=true;
            String language="";
        
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
                labelError.setText("Uno de los datos introducidos no es correcto.");
                labelError.setVisible(true);
            }else{
                customer.setDefaultVAT(defaultVAT);
                customer.setDefaultWithholding(defaultWithholding);
                customer.setEurope(cbEurope.isSelected());
                customer.setEnabled(cbEnabled.isSelected());
                customer.setInvoicingMethod(fieldInvoicingMethod.getText());
                customer.setPayMethod(fieldPayMethod.getText());
                customer.setDuedate(duedate);
                customer.setDefaultLanguage(cbLanguage.getValue());
                customer.addToDB();
                
                stage.close();
            }
        }
    }
    
    @FXML protected void onClicAddAddress(){
        ViewNewAddressController controller=null;
        FXMLLoader loader=switchWindow("viewNewAddress.fxml");
        
        controller=loader.getController();
        controller.initData(customer.getAddress());
    }
    
    @FXML protected void onClicAddContact(){
        ViewNewContactController controller=null;
        FXMLLoader loader=switchWindow("viewNewContact.fxml");
        ContactPerson contact=new ContactPerson();
        
        controller=loader.getController();
        controller.initData(contact);
        customer.addContactPerson(contact);
    }
    
    @FXML protected void onClicAddPhone(){
        ViewNewPhoneController controller=null;
        FXMLLoader loader=switchWindow("viewNewPhone.fxml");
        Phone phone=new Phone();
        
        controller=loader.getController();
        controller.initData(phone);
        customer.addPhone(phone);
    }
    
    @FXML protected void onClicAddScheme(){
        ViewNewSchemeController controller=null;
        FXMLLoader loader=switchWindow("viewNewScheme.fxml");
        Scheme scheme=new Scheme();
        
        controller=loader.getController();
        controller.initData(scheme);
        customer.addScheme(scheme);
    }
    
    @FXML protected void onClicAddBankAccount(){
        ViewNewBankAccountController controller=null;
        FXMLLoader loader=switchWindow("viewNewBankAccoutnt.fxml");
        BankAccount bankAccount=new BankAccount();
        
        controller=loader.getController();
        controller.initData(bankAccount);
        customer.addBankAccount(bankAccount);
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
        
        customer.setIdCompany(customProv.getIdCompany());
        customer.setAddress(customProv.getAddress());
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
        
        stage=(Stage)paneCreateCustomer.getScene().getWindow();
        newStage.setOnHiding(event -> {
                stage.show();
            });
        
        stage.close();
        
        return loader;
    }

}

    
