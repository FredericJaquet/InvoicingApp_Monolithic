/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.Customer;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;


public class ViewDetailsCustomerController implements Initializable {

    private Customer customer;
    
    public void initData(Customer customer){
        this.customer=customer;
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
