/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.Customer;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;


public class ViewDetailsCustomerController implements Initializable {

    private Customer customer=new Customer();
    
    @FXML Label lbComName,lbLegalName,lbWeb,lbCompEmail,lbVATNumber,lbDefaultVAT,lbDuedate,lbDefaultWithholding,lbInvoicingMethod,lbPayMethod,lbEurope,lbEnabled,lbStreet,lbStNumber,lbCity,lbState,lbApt,lbCP,lbCountry;
    
    public void initData(Customer customer){
        this.customer=customer;
        
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
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
