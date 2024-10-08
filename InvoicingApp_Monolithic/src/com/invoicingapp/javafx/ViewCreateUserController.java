/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.config.Configuration;
import com.invoicingapp.config.Translations;
import com.invoicingapp.tools.Validations;
import invoicingapp_monolithic.BankAccount;
import invoicingapp_monolithic.Company;
import invoicingapp_monolithic.ContactPerson;
import invoicingapp_monolithic.Phone;
import invoicingapp_monolithic.Users;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewCreateUserController implements Initializable {

    private Configuration config;
    private Users user=new Users();
    private ArrayList<Company> companies=new ArrayList();
    private Stage stage;
    private String logoPath;
    private final String introUser="Datos del usuario";
    private final String errorEmpty="Falta un dato obligatorio.";
    private final String errorAddress="Es obligatorio añadir una dirección.";
    private final String errorPassword="Las contraseñas no coinciden.";
    private boolean control=true;
    
    @FXML private Label labelIntro, labelPassword,labelError;
    @FXML private PasswordField fieldPasswd1,fieldPasswd2 ;
    @FXML private TextField textFieldPW1,textFieldPW2,fieldUsername, 
            fieldVATNumber,fieldComName,fieldLegalName,fieldEmail,fieldWeb;
    @FXML private ComboBox cbCompany;
    @FXML ChoiceBox<String> cbDefaultLanguage;
    @FXML private GridPane paneUser,paneCompany;
    @FXML private HBox paneFootUser,paneFootCompany;
    @FXML private VBox paneCreateUser;
    @FXML Button btnImportLogo;
    
    public void initData(Configuration config){
        this.config=config;
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        config=Configuration.loadConfiguration();
        companies=Company.getAllFromDB();
        textFieldPW1.setVisible(false);
        textFieldPW2.setVisible(false);
        labelIntro.setText(introUser);
        cbDefaultLanguage.getItems().addAll( Translations.languages);
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
    
    @FXML protected void onClicCancel(){
        if(logoPath!=null){
            try {
                System.out.println(logoPath);
                Files.deleteIfExists(Paths.get(logoPath));
            } catch (IOException ex) {
                Logger.getLogger(ViewCreateUserController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        closeWindow();
    }
    
    @FXML protected void onClicNextFromUser(){
        if(Validations.isNotEmpty(fieldUsername,labelError,errorEmpty)&&Validations.isNotEmpty(fieldPasswd1,labelError,errorEmpty)&&Validations.isNotEmpty(fieldPasswd2,labelError,errorEmpty)){
            if(fieldPasswd1.getText().equals(fieldPasswd2.getText())){
                user.setUserName(fieldUsername.getText());
                user.setPasswd(fieldPasswd1.getText());
        
                paneUser.setVisible(false);
                paneCompany.setVisible(true);
                paneFootUser.setVisible(false);
                paneFootCompany.setVisible(true);
                
                labelPassword.setVisible(false);
                labelError.setVisible(false);
            }else{
                labelPassword.setText(errorPassword);
            }
        }
    }
    
    @FXML protected void onClicSave(){
        control=true;
        labelError.setVisible(false);
        
        if(user.getAddress().getStreet()==null){
            labelError.setText(errorAddress);
            labelError.setVisible(true);
            control=false;
        }
        if(!Validations.isNotEmpty(fieldVATNumber,labelError,errorEmpty)){
            control=false;
        }
        if(!Validations.isNotEmpty(fieldLegalName,labelError,errorEmpty)){
            control=false;
        }
        
        if(control){
            user.setVatNumber(fieldVATNumber.getText());
            user.setComName(fieldComName.getText());
            user.setLegalName(fieldLegalName.getText());
            user.setEmail(fieldEmail.getText());
            user.setWeb(fieldWeb.getText());
            user.setDefaultLanguage(cbDefaultLanguage.getValue());
            user.addToDB();
            
            config.setLogoPath(logoPath);
            config.save();
            
            closeWindow();
        }
    }
    
    @FXML protected void onClicBackFromCompany(){
        labelIntro.setText(introUser);
        labelError.setVisible(false);
        paneUser.setVisible(true);
        paneCompany.setVisible(false);
        paneFootUser.setVisible(true);
        paneFootCompany.setVisible(false); 
    }
    
    @FXML protected void onClicAddAddress(){
        ViewNewAddressController controller=null;
        FXMLLoader loader=switchWindow("viewNewAddress.fxml");
        
        controller=loader.getController();
        controller.initData(user.getAddress());
    }
    
    @FXML protected void onClicAddContact(){
        ViewNewContactController controller=null;
        FXMLLoader loader=switchWindow("viewNewContact.fxml");
        ContactPerson contact=new ContactPerson();
        
        controller=loader.getController();
        controller.initData(contact);
        user.addContactPerson(contact);
    }
    
    @FXML protected void onClicAddBankAccount(){
        ViewNewBankAccountController controller=null;
        FXMLLoader loader=switchWindow("viewNewBankAccount.fxml");
        BankAccount bankAccount=new BankAccount();
        
        controller=loader.getController();
        controller.initData(bankAccount);
        user.addBankAccount(bankAccount);   
    }
    
    @FXML protected void onClicAddPhone(){
        ViewNewPhoneController controller=null;
        FXMLLoader loader=switchWindow("viewNewPhone.fxml");
        Phone phone=new Phone();
        
        controller=loader.getController();
        controller.initData(phone);
        user.addPhone(phone);
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
        
        fieldVATNumber.setText(company.getVatNumber());
        fieldComName.setText(company.getComName());
        fieldLegalName.setText(company.getLegalName());
        fieldEmail.setText(company.getEmail());
        fieldWeb.setText(company.getWeb());
        
        user.setIdCompany(company.getIdCompany());
        user.setAddress(company.getAddress());
    }
    
    @FXML protected void handleImportLogo() {
        FileChooser fileChooser=new FileChooser();
        fileChooser.setTitle("Seleccionar Logo");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        Stage stage=(Stage) btnImportLogo.getScene().getWindow();
        File selectedFile=fileChooser.showOpenDialog(stage);        

        if (selectedFile!=null) {
            logoPath=selectedFile.getAbsolutePath();
        }
    }
    
    private FXMLLoader switchWindow(String path){
        FXMLLoader loader=new FXMLLoader();
        Parent root=null;
        Scene scene;
        Stage newStage=new Stage();
        stage=(Stage)paneCreateUser.getScene().getWindow();
        
        loader.setLocation(getClass().getResource(path));
        try {
            root=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewCreateCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        scene=new Scene(root);
        newStage.setScene(scene);
        newStage.show();
        
        newStage.setOnHiding(event -> {
                stage.show();
            });
        closeWindow();
        
        return loader;
    }
    
    private void closeWindow(){
        stage=(Stage)paneCreateUser.getScene().getWindow();
        stage.close();
    }
    
}
