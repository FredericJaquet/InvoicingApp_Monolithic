/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.Document;
import invoicingapp_monolithic.InvoiceCustomer;
import invoicingapp_monolithic.InvoiceProvider;
import java.net.URL;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewReportInOutcomesInvoicesController implements Initializable {

    private InvoiceCustomer invoiceCustomer;
    private InvoiceProvider invoiceProvider;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final NumberFormat formatterDouble = NumberFormat.getCurrencyInstance(new Locale("es", "ES"));
    private int invoiceType;
    
    @FXML private Label lbDocNumber,lbDate,lbTotalNet,lbTotalVAT,lbTotalWithholding;
    
    public void initData(Document invoice){
        if(invoice.getClass().equals(InvoiceCustomer.class)){
            this.invoiceCustomer=(InvoiceCustomer)invoice;
            invoiceType=1;
        }
        else if(invoice.getClass().equals(InvoiceProvider.class)){
            this.invoiceProvider=(InvoiceProvider)invoice;
            invoiceType=2;
        }
        
        if(invoiceType==1){
            getInvoiceCustomerData();
        }else if(invoiceType==2){
            getInvoiceProviderData();
        }
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {}    
    
    private void getInvoiceCustomerData(){
        lbDocNumber.setText(invoiceCustomer.getDocNumber());
        lbDate.setText(invoiceCustomer.getDocDate().format(formatter));
        lbTotalNet.setText(formatterDouble.format(invoiceCustomer.getTotalInLocalCurrency()));
        lbTotalVAT.setText(formatterDouble.format(invoiceCustomer.getTotalVATInLocalCurrency()));
        lbTotalWithholding.setText(formatterDouble.format(invoiceCustomer.getTotalWithholdingInLocalCurrency()));
    }
    
    private void getInvoiceProviderData(){
        lbDocNumber.setText(invoiceProvider.getDocNumber());
        lbDate.setText(invoiceProvider.getDocDate().format(formatter));
        lbTotalNet.setText(formatterDouble.format(invoiceProvider.getTotalInLocalCurrency()));
        lbTotalVAT.setText(formatterDouble.format(invoiceProvider.getTotalVATInLocalCurrency()));
        lbTotalWithholding.setText(formatterDouble.format(invoiceProvider.getTotalWithholdingInLocalCurrency()));
    }
    
}
