/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.ContactPerson;
import invoicingapp_monolithic.Customer;
import invoicingapp_monolithic.Phone;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;


public class ViewDetailsCustomerController implements Initializable {

    private Customer customer=new Customer();
    private ArrayList<Phone> phones=new ArrayList();
    private ArrayList<ContactPerson> contacts=new ArrayList();
    private int iContacts=0,iPhones=0;
    
    @FXML Label lbComName,lbLegalName,lbWeb,lbCompEmail,lbVATNumber,lbDefaultVAT,
            lbDuedate,lbDefaultWithholding,lbInvoicingMethod,lbPayMethod,lbEurope,
            lbEnabled,lbStreet,lbStNumber,lbCity,lbState,lbApt,lbCP,lbCountry,
            lbFirstname,lbMiddlename,lbLastname,lbRole,lbContactEmail,
            lbPhoneNumber,lbKind;
    @FXML TextField fieldComName,fieldLegalName,fieldWeb,fieldCompEmail,fieldVATNumber,fieldDefaultVAT,
            fieldDuedate,fieldDefaultWithholding,fieldInvoicingMethod,fieldPayMethod,
            fieldStreet,fieldStNumber,fieldCity,fieldState,fieldApt,fieldCP,fieldCountry,
            fieldFirstname,fieldMiddlename,fieldLastname,fieldRole,fieldContactEmail,
            fieldPhoneNumber,fieldKind;
    @FXML CheckBox cbEurope,cbEnabled;
    @FXML Button btnContactLeft,btnContactRight,btnPhoneLeft,btnPhoneRight,btnNewContact,btnNewPhone;
    @FXML GridPane gridContacts,gridPhones;
    
    public void initData(Customer customer){
        this.customer=customer;
        contacts=customer.getContacts();
        phones=customer.getPhones();
        
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
        //makeLabelEditable(lbEurope, fieldEurope);
        //makeLabelEditable(lbEnabled, fieldEnabled);
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
    
    private void switchToTextField(Label label, TextField textField) {
        textField.setText(label.getText());
        textField.setVisible(true);
        textField.requestFocus();
        label.setVisible(false);
    }
    
    private void switchToLabel(Label label, TextField textField) {
        label.setText(textField.getText());
        label.setVisible(true);
        textField.setVisible(false);
    }

}
