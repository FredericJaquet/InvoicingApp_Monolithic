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
import invoicingapp_monolithic.SchemeLine;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;


public class ViewCreateCustomerController implements Initializable {

    private String introCompany="Datos de la Empresa";
    private String introAddress="Dirección";
    private String introContact="Persona de contacto";
    private String introPhone="Teléfono de contacto";
    private String introFiscalData="Datos fiscales";
    private String introScheme="Esquema de facturación";
    private Customer customer=new Customer();
    private ArrayList<CustomProv> companies=new ArrayList();
    private ObservableList<SchemeLine> schemeLines=FXCollections.observableArrayList();
    private SchemeLine line=new SchemeLine();
    
    @FXML private Label labelIntro, labelError;
    @FXML private TextField fieldVAT,fieldComName,fieldLegalName,fieldEmailCompany,fieldWeb;
    @FXML private TextField fieldStreet,fieldStNumber,fieldApt,fieldCP,fieldCity,fieldState,fieldCountry;
    @FXML private TextField fieldDefaultVAT,fieldDefaultWithholding,fieldInvoicingMethod,fieldPayMethod,fieldDuedate;
    @FXML private TextField fieldPhoneNumber,fieldPhoneKind;
    @FXML private TextField fieldFirstname,fieldMiddlename,fieldLastname,fieldRole,fieldContactEmail;
    @FXML private TextField fieldSchemeName,fieldSourceLanguage,fieldTargetLanguage,fieldPrice,fieldFieldName;
    @FXML private CheckBox cbEurope,cbEnabled;
    @FXML private ComboBox cbCompany;
    @FXML private GridPane paneCompany, paneAddress,paneFiscalData,panePhone,paneContact,paneScheme;
    @FXML private HBox paneFootCompany,paneFootAddress,paneFootFiscalData,paneFootPhone,paneFootContact,paneFootScheme;
    @FXML TableView<SchemeLine> tableSchemeLine;
    @FXML TableColumn<SchemeLine,String>columnDescription;
    @FXML TableColumn<SchemeLine,Double>columnDiscount;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        companies=CustomProv.getAllCustomProvFromDB();
        createTableSchemeLines();
        
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
    
    @FXML protected void onClicCancelFromScheme(){
        labelIntro.setText(introFiscalData);
        
        paneFiscalData.setVisible(true);
        paneFootFiscalData.setVisible(true);
        paneScheme.setVisible(false);
        paneFootScheme.setVisible(false);
        
        fieldSchemeName.clear();
        fieldSourceLanguage.clear();
        fieldTargetLanguage.clear();
        fieldPrice.clear();
        fieldFieldName.clear();
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
            labelError.setVisible(true);
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
    
    @FXML protected void onClicAddScheme(){
        labelIntro.setText(introScheme);
        paneFiscalData.setVisible(false);
        paneFootFiscalData.setVisible(false);
        
        paneScheme.setVisible(true);
        paneFootScheme.setVisible(true);
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

    private void createTableSchemeLines(){
        
        columnDescription.setCellFactory(TextFieldTableCell.forTableColumn());
        columnDescription.setOnEditCommit(event -> {
            line = new SchemeLine();
            line.setDescription(event.getNewValue());
        });
        
        columnDiscount.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        columnDiscount.setOnEditCommit(event -> {
            line.setDiscount(event.getNewValue());
        });
        
        schemeLines.add(new SchemeLine("",0.0));
        
        tableSchemeLine.setItems(schemeLines);
        tableSchemeLine.setOnKeyPressed(event -> handleKeyEvent(event));
    }
    
    private void handleKeyEvent(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            int columnIndex = tableSchemeLine.getFocusModel().getFocusedCell().getColumn();
            int rowIndex = tableSchemeLine.getFocusModel().getFocusedCell().getRow();
            int newColumnIndex = (columnIndex + 1) % tableSchemeLine.getColumns().size();

            tableSchemeLine.getSelectionModel().clearAndSelect(rowIndex, tableSchemeLine.getColumns().get(newColumnIndex));
            tableSchemeLine.edit(rowIndex, tableSchemeLine.getColumns().get(newColumnIndex));
            event.consume();
        }else if (event.getCode() == KeyCode.ENTER) {
            int rowIndex = tableSchemeLine.getFocusModel().getFocusedCell().getRow();
            if (rowIndex < tableSchemeLine.getItems().size() - 1) {
                tableSchemeLine.getSelectionModel().clearAndSelect(rowIndex + 1, tableSchemeLine.getColumns().get(0));
                tableSchemeLine.edit(rowIndex + 1, tableSchemeLine.getColumns().get(0));
            } else {
                schemeLines.add(new SchemeLine("",0.0));
                tableSchemeLine.getSelectionModel().clearAndSelect(rowIndex + 1, tableSchemeLine.getColumns().get(0));
                tableSchemeLine.edit(rowIndex + 1, tableSchemeLine.getColumns().get(0));
            }
            tableSchemeLine.getSelectionModel().clearAndSelect(rowIndex + 1, tableSchemeLine.getColumns().get(0));
            tableSchemeLine.edit(rowIndex + 1, tableSchemeLine.getColumns().get(0));
            schemeLines.add(new SchemeLine("",0.0));
            event.consume();
        }
    }
}
    
