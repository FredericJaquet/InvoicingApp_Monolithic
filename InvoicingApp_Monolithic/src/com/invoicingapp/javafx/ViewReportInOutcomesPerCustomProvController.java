/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.CustomProv;
import invoicingapp_monolithic.Customer;
import invoicingapp_monolithic.Document;
import invoicingapp_monolithic.Provider;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewReportInOutcomesPerCustomProvController implements Initializable {

    private Customer customer=null;
    private Provider provider=null;
    private NumberFormat formatterDouble = NumberFormat.getCurrencyInstance(new Locale("es", "ES"));
    private int lines=1;
    private final String[] styles={"lineUneven","lineEven"};
    
    @FXML Label lbLegalName,lbCountry,lbVATNumber,lbTotalNet,lbTotalVAT,lbTotalWithholding;
    @FXML VBox paneInvoices;
    
    public void initData(CustomProv company){
        if(company.getClass().equals(Customer.class)){
            this.customer=(Customer)company;
        }
        if(company.getClass().equals(Provider.class)){
            this.provider=(Provider)company;
        }
        
        lbLegalName.setText(company.getLegalName());
        lbCountry.setText(company.getAddress().getCountry());
        lbVATNumber.setText(company.getVatNumber());
        getTotals();
        getInvoices();
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {}    
    
    private void getTotals(){
        double totalNet=0;
        double totalVAT=0;
        double totalWithholding=0;
        
        if(customer!=null){
            totalNet=customer.getTotalNetCustomer();
            totalVAT=customer.getTotalVATCustomer();
            totalWithholding=customer.getTotalWithholdingCustomer();
        }else if(provider!=null){
            totalNet=provider.getTotalNetProvider();
            totalVAT=provider.getTotalVATProvider();
            totalWithholding=provider.getTotalWithholdingProvider();
        }
        
        lbTotalNet.setText(formatterDouble.format(totalNet));
        lbTotalVAT.setText(formatterDouble.format(totalVAT));
        lbTotalWithholding.setText(formatterDouble.format(totalWithholding));
    }
    
    private void getInvoices(){
        paneInvoices.getChildren().clear();
        if(customer!=null){
            for(int i=0;i<customer.getInvoices().size();i++){
                addInvoices(customer.getInvoices().get(i));
            }
        }else if(provider!=null){
            for(int i=0;i<provider.getInvoices().size();i++){
                addInvoices(provider.getInvoices().get(i));
            }
        }
    }
    
    private void addInvoices(Document invoice){
        FXMLLoader loader;
        Parent reportInOutcomesInvoicesView=null;
        ViewReportInOutcomesInvoicesController controller=null;
        
        loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("viewReportInOutcomesInvoices.fxml"));
        try {
            reportInOutcomesInvoicesView=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewReportInOutcomesPerCustomProvController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(invoice);
        reportInOutcomesInvoicesView.getStyleClass().add(styles[lines%2]);
        lines++;
        paneInvoices.getChildren().add(reportInOutcomesInvoicesView);
    }
}
