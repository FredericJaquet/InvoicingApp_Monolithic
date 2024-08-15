/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.InvoiceCustomer;
import invoicingapp_monolithic.ReportPending;
import invoicingapp_monolithic.ReportPendingPerMonth;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
public class ViewReportPendingController implements Initializable {

    private ArrayList<InvoiceCustomer> invoices=new ArrayList();
    private ReportPending report;
    
    @FXML private Label lbTotalPending;
    @FXML private VBox paneMonthlyReport;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getInvoices();
    }
    
    @FXML protected void onClicUpdate(){
        for(int i=0;i<invoices.size();i++){
            if(invoices.get(i).isPaid()){
                invoices.get(i).updateDB("paid", true);
            }
        }
        getInvoices();
    }
    
    private void getInvoices(){
        invoices.clear();
        report=new ReportPending();
        invoices=report.getInvoices();
        lbTotalPending.setText(String.format("%.2f€",report.getTotalToBePaid()));
        paneMonthlyReport.getChildren().clear();
        for(int i=0;i<report.getMonthlyReport().size();i++){
            if(report.getMonthlyReport().get(i)!=null){
                addMonthlyReport(report.getMonthlyReport().get(i));
            }
        }
    }
    
    private void addMonthlyReport(ReportPendingPerMonth report){
        FXMLLoader loader;
        Parent monthlyReportView=null;
        ViewMonthlyReportPendingController controller=null;
        
        loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("viewMonthlyReportPending.fxml"));
        try {
            monthlyReportView=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewReportPendingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(report);
        paneMonthlyReport.getChildren().add(monthlyReportView);
    }
    
}
