/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.InvoiceCustomer;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;


/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewInvoicePendingController implements Initializable {
    
    private InvoiceCustomer invoice;
    
    @FXML Label lbCustomer,lbNumber,lbTotal;
    @FXML CheckBox cbPaid;
    
    
    public void initData(InvoiceCustomer invoice){
        this.invoice=invoice;
        setData();
    }
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {}    
    
    @FXML protected void onCheckPaid(){
        invoice.setPaid(cbPaid.isSelected());
    }
    
    private void setData(){
        lbCustomer.setText(invoice.getCustomer().getComName());
        lbNumber.setText(invoice.getDocNumber());
        lbTotal.setText(String.format("%.2f€",invoice.getTotalToPayInLocalCurrency()));
    }
    
}
