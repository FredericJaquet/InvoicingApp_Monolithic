/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.Customer;
import invoicingapp_monolithic.InvoiceCustomer;
import invoicingapp_monolithic.ReportInvoiceCustomer;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewReportIncomesController implements Initializable {
    
    private ReportInvoiceCustomer report;
    private int pages=1;
    private int page=1;
    private int[] customersIndex;
    private final int maxLinesPerPage=30;
    private final int linesPerHeader=3;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final NumberFormat formatterDouble = NumberFormat.getCurrencyInstance(new Locale("es", "ES"));
    
    @FXML private Label lbTitleFrom, lbTitleUntill, lbFrom,lbUntill,lbTotal;
    @FXML private DatePicker dpFrom, dpUntill;
    @FXML private VBox paneMain, paneReport, paneReports;
    @FXML private ScrollPane paneScroll;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lbTitleFrom.setVisible(false);
        lbFrom.setVisible(false);
        lbTitleUntill.setVisible(false);
        lbUntill.setVisible(false);
    }
    
    @FXML protected void getReport(){
        if(dpFrom.getValue()!=null){
            lbTitleFrom.setVisible(true);
            lbFrom.setVisible(true);
            lbFrom.setText(dpFrom.getValue().format(formatter));
        }
        if(dpUntill.getValue()!=null){
            lbTitleUntill.setVisible(true);
            lbUntill.setVisible(true);
            lbUntill.setText(dpUntill.getValue().format(formatter));
        }
        if(dpFrom.getValue()!=null&&dpUntill.getValue()!=null){
            report=new ReportInvoiceCustomer(dpFrom.getValue(),dpUntill.getValue());
            report.getCustomersFromDB();
            lbTotal.setText(formatterDouble.format(report.getTotalInvoices()));
            getReports();
        }
        if(dpFrom.getValue()==null){
            lbTitleFrom.setVisible(false);
            lbFrom.setVisible(false);
        }
        if(dpUntill.getValue()==null){
            lbTitleUntill.setVisible(false);
            lbUntill.setVisible(false);
        }
    }
    
    @FXML protected void onClicThisYear(){
        dpFrom.setValue(LocalDate.ofYearDay(LocalDate.now().getYear(), 1));
        dpUntill.setValue(LocalDate.now());
    }
    
    @FXML protected void onClicThisQ(){
        int year=LocalDate.now().getYear();
        int quarter=(LocalDate.now().getMonthValue()-1)/3;
        int month=(quarter*3)+1;
        int day=1;
        dpFrom.setValue(LocalDate.of(year, month,day));
        dpUntill.setValue(LocalDate.now());
    }
    
    @FXML protected void onClicThisMonth(){
        dpFrom.setValue(LocalDate.of(LocalDate.now().getYear(),LocalDate.now().getMonth(), 1));
        dpUntill.setValue(LocalDate.now());
    }
    
    @FXML protected void onClicAddPendingOrders(){
        if(dpFrom.getValue()!=null&&dpUntill.getValue()!=null){
            report.getCustomersWithPendingOrdersFromDB();
            lbTotal.setText(formatterDouble.format(report.getTotalInvoices()));
            getReports();
        }
    }
    
    @FXML protected void onClicExcludePendingOrders(){
        getReport();
    }
    
    @FXML protected void onClicPrint(){
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
        PrinterJob job = PrinterJob.createPrinterJob();
        page=1;
        
        paneScroll.setVbarPolicy(ScrollBarPolicy.NEVER);
        setPageNumber();
        setIndexCustomersPerPages();
        setInvoicesToPrint();
        
        if (job != null && job.showPrintDialog(paneReport.getScene().getWindow())) {
            boolean success = true;

            for (int i=0;i<pages;i++) {
                success = success && printNode(paneReport, job, pageLayout);
                if (!success) {
                    break;
                }
                if(page<pages){
                    page++;
                    setInvoicesToPrint();
                }
            }
            if (success) {
                job.endJob();
            }
        }
        
        paneScroll.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        getReports();
    }
    
    private boolean printNode(Node node, PrinterJob job, PageLayout pageLayout){
        boolean success;
        double scaleX = pageLayout.getPrintableWidth() / node.getBoundsInParent().getWidth();
        double scaleY = pageLayout.getPrintableHeight() / node.getBoundsInParent().getHeight();
        double scale = Math.min(scaleX, scaleY);
        
        node.getTransforms().add(new Scale(scale, scale));
        node.setTranslateX(-30);
        node.setTranslateY(-135);
        success=job.printPage(pageLayout, node);
           
        node.getTransforms().clear();
        node.setTranslateX(0);
        node.setTranslateY(0);
        
        return success;
    }
    
    private void getReports(){
        paneReports.getChildren().clear();
        for(int i=0;i<report.getCustomers().size();i++){
            addReportInOutcomes(report.getCustomers().get(i));
        }
    }
    
    private void addReportInOutcomes(Customer customer){
        FXMLLoader loader;
        Parent reportPerCustomerView=null;
        ViewReportInOutcomesPerCustomProvController controller=null;
        
        loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("viewReportInOutcomesPerCustomProv.fxml"));
        try {
            reportPerCustomerView=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewReportIncomesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(customer);
        paneReports.getChildren().add(reportPerCustomerView);
    }
    
    private void setPageNumber(){
        int linesNumber=0;
        pages=1;
        
        for(int i=0;i<report.getCustomers().size();i++){
            linesNumber=linesNumber+report.getCustomers().get(i).getInvoices().size()+linesPerHeader;
            if(linesNumber>maxLinesPerPage){
                pages++;
                linesNumber=report.getCustomers().get(i).getInvoices().size()+linesPerHeader;
            }
        }
        customersIndex=new int[pages+1];
        setIndexCustomersPerPages();
    }
    
    private void setIndexCustomersPerPages(){
        int linesNumber=0;
        int j=1;
        
        customersIndex[0]=0;
        
        for(int i=0;i<report.getCustomers().size();i++){
            linesNumber=linesNumber+report.getCustomers().get(i).getInvoices().size()+linesPerHeader;
            if(linesNumber>maxLinesPerPage){
                customersIndex[j]=i;
                j++;
                linesNumber=report.getCustomers().get(i).getInvoices().size()+linesPerHeader;
            }
        }
        customersIndex[pages]=report.getCustomers().size();
    }
    
    private void setInvoicesToPrint(){
        paneReports.getChildren().clear();
        for(int i=customersIndex[page-1];i<customersIndex[page];i++){
            addReportInOutcomes(report.getCustomers().get(i));
        }
    }
}
