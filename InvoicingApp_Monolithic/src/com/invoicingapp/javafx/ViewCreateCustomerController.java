/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.config.Translations;
import com.invoicingapp.tools.Validations;
import invoicingapp_monolithic.BankAccount;
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
import javafx.scene.image.Image;
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
    private final String errorFormat="Uno de los datos introducidos no es correcto.";
    private final String errorEmpty="Falta un dato obligatorio.";
    private final String errorAddress="Es obligatorio añadir una dirección.";
    private boolean control=true;
    
    @FXML private Label labelIntro, labelError;
    @FXML private TextField fieldVATNumber,fieldComName,fieldLegalName,fieldEmailCompany,fieldWeb;
    @FXML private TextField fieldDefaultVAT,fieldDefaultWithholding,fieldInvoicingMethod,fieldPayMethod,fieldDuedate;
    @FXML private CheckBox cbEurope,cbEnabled;
    @FXML private ComboBox<CustomProv> cbCompany;
    @FXML private ChoiceBox<String> cbDefaultLanguage;
    @FXML private GridPane paneCompany, paneFiscalData;
    @FXML private HBox paneFootCompany,paneFootFiscalData;
    @FXML private VBox paneCreateCustomer;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        companies=CustomProv.getAllCustomProvFromDB();        
        labelIntro.setText(introCompany);
        cbDefaultLanguage.getItems().addAll(Translations.languages);
    }
    
    @FXML protected void onClicCancel(){
        closeWindow();
    }
    
    @FXML protected void onClicNext(){
        if(Validations.isNotEmpty(fieldVATNumber,labelError,errorEmpty)&&Validations.isNotEmpty(fieldLegalName,labelError,errorEmpty)){
            customer.setVatNumber(fieldVATNumber.getText());
            customer.setComName(fieldComName.getText());
            customer.setLegalName(fieldLegalName.getText());
            customer.setEmail(fieldEmailCompany.getText());
            customer.setWeb(fieldWeb.getText());
        
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
        control=true;
        labelError.setVisible(false);
        
        if(customer.getAddress().getStreet()==null){
            labelError.setText(errorAddress);
            labelError.setVisible(true);
            control=false;
        }
        if(!Validations.isDouble(fieldDefaultVAT, labelError, errorFormat)){
            control=false;
        }
        if(!Validations.isDouble(fieldDefaultWithholding, labelError, errorFormat)){
            control=false;
        }
        if(!Validations.isInteger(fieldDuedate, labelError, errorFormat)){
            control=false;
        }
        
        if(control){
            customer.setDefaultVAT(Double.parseDouble(fieldDefaultVAT.getText()));
            customer.setDefaultWithholding(Double.parseDouble(fieldDefaultWithholding.getText()));
            customer.setEurope(cbEurope.isSelected());
            customer.setEnabled(cbEnabled.isSelected());
            customer.setInvoicingMethod(fieldInvoicingMethod.getText());
            customer.setPayMethod(fieldPayMethod.getText());
            customer.setDuedate(Integer.parseInt(fieldDuedate.getText()));
            customer.setDefaultLanguage(cbDefaultLanguage.getValue());
            customer.addToDB();
            
            closeWindow();
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
        FXMLLoader loader=switchWindow("viewNewBankAccount.fxml");
        BankAccount bankAccount=new BankAccount();
        
        controller=loader.getController();
        controller.initData(bankAccount);
        customer.addBankAccount(bankAccount);
    }
    
    @FXML protected void populateComboBox(){
        ObservableList<CustomProv> companyObs =FXCollections.observableArrayList(companies);
        cbCompany.setItems(companyObs);
        
        cbCompany.setCellFactory(new Callback<ListView<CustomProv>, ListCell<CustomProv>>() {
            @Override
            public ListCell<CustomProv> call(ListView<CustomProv> p) {
                return new ListCell<CustomProv>() {
                    @Override
                    protected void updateItem(CustomProv item, boolean empty) {
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
        
        cbCompany.setButtonCell(new ListCell<CustomProv>() {
            @Override
            protected void updateItem(CustomProv item, boolean empty) {
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
        CustomProv cust= cbCompany.getSelectionModel().getSelectedItem();
        
        fieldVATNumber.setText(cust.getVatNumber());
        fieldComName.setText(cust.getComName());
        fieldLegalName.setText(cust.getLegalName());
        fieldEmailCompany.setText(cust.getEmail());
        fieldWeb.setText(cust.getWeb());
        
        customer.setIdCompany(cust.getIdCompany());
        customer.setAddress(cust.getAddress());
    }
    
    private FXMLLoader switchWindow(String path){
        FXMLLoader loader=new FXMLLoader();
        Parent root=null;
        Scene scene;
        Stage newStage=new Stage();
        //stage=(Stage)paneCreateCustomer.getScene().getWindow();
        
        loader.setLocation(getClass().getResource(path));
        try {
            root=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewCreateCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        scene=new Scene(root);
        
        newStage.setScene(scene);
        newStage.getIcons().add(new Image(getClass().getResourceAsStream("../img/Icon.png"))); 
        newStage.show();
        
        newStage.setOnHiding(event -> {
                stage.show();
            });
        closeWindow();
        
        return loader;
    }
    
    private void closeWindow(){
        stage=(Stage)paneCreateCustomer.getScene().getWindow();
        stage.close();
    }

}