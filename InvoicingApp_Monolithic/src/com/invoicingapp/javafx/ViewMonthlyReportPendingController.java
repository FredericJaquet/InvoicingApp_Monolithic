/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.InvoiceCustomer;
import invoicingapp_monolithic.ReportPendingPerMonth;
import java.io.IOException;
import java.net.URL;
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
public class ViewMonthlyReportPendingController implements Initializable {

    private ReportPendingPerMonth report;
    
    @FXML Label lbYear,lbMonth,lbTotal;
    @FXML private VBox paneMonthlyReport;
    
    public void initData(ReportPendingPerMonth report){
        this.report=report;
        
        getHeader();
        
        paneMonthlyReport.getChildren().clear();
        for(int i=0;i<report.getInvoices().size();i++){
            addInvoices(report.getInvoices().get(i));
        }
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {}
    
    private void getHeader(){
        int month=report.getMonth();
        int year=report.getYear();
        
        switch(month){
            case(1):lbMonth.setText("Enero");break;
            case(2):lbMonth.setText("Febrero");break;
            case(3):lbMonth.setText("Marzo");break;
            case(4):lbMonth.setText("Abril");break;
            case(5):lbMonth.setText("Mayo");break;
            case(6):lbMonth.setText("Junio");break;
            case(7):lbMonth.setText("Julio");break;
            case(8):lbMonth.setText("Agosto");break;
            case(9):lbMonth.setText("Septiembre");break;
            case(10):lbMonth.setText("Octubre");break;
            case(11):lbMonth.setText("Noviembre");break;
            case(12):lbMonth.setText("Diciembre");break;
        }
        lbYear.setText(String.valueOf(year));
        
        lbTotal.setText(String.format("%.2f€",report.getTotal()));
    }
    
    private void addInvoices(InvoiceCustomer invoice){
        FXMLLoader loader;
        Parent invoicePendingView=null;
        ViewInvoicePendingController controller=null;
        
        loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("viewInvoicePending.fxml"));
        try {
            invoicePendingView=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewReportPendingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(invoice);
        paneMonthlyReport.getChildren().add(invoicePendingView);
        
    }
    
}
