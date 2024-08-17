/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.bbdd.ConnectionDB;
import com.invoicingapp.config.Configuration;
import com.invoicingapp.config.Translations;
import com.invoicingapp.tools.LabelFeatures;
import invoicingapp_monolithic.BankAccount;
import invoicingapp_monolithic.ChangeRate;
import invoicingapp_monolithic.Customer;
import invoicingapp_monolithic.InvoiceCustomer;
import invoicingapp_monolithic.Orders;
import invoicingapp_monolithic.Users;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;

public class ViewInvoiceCustomerController implements Initializable {

    private InvoiceCustomer invoice;
    private Customer customer;
    private Users user;
    private BankAccount bankAccount;
    private Configuration config;
    private ChangeRate changeRate;
    private LabelFeatures lbFeatures=new LabelFeatures();
    private ArrayList<Orders> orders=new ArrayList();
    private ArrayList<String> query=new ArrayList();
    private int language;
    private int pages=1;
    private int page=1;    
    private double totalNet=0;
    private double totalVAT=0;
    private double totalWithholding=0;
    private double totalInvoice=0;
    private double totalToPay=0;
    private int[] ordersIndex;
    private final int maxLinesPerPage=24;
    private final int imgSize=150;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    @FXML private VBox paneInvoice,vbOrders,paneMain;
    @FXML private Label lbVATNumber,lbStreet,lbCityCp,lbCountry,lbEmail,lbWeb,lbLegalName,
            lbCustComName,lbCustLegalName,lbCustVATNumber,lbCustStreet,lbCustCPCity,lbCustStateCountry,lbCustEmail,lbCustWeb,
            lbDocDate,lbDocNumber,lbPageNumber,lbPageTotal,lbIBAN,lbHolder,lbBranch,lbPayMethod,lbDuedate,
            lbTotalNet,lbVAT,lbTotalVAT,lbWithholding,lbTotalWithholding,lbTotalInvoice,lbTotalToPay,lbTotalInvoice2,lbTotalToPay2,
            lbTitleName,lbTitleVATNumber,lbTitleAddress,lbTitleCPCity,lbTitleCountry,lbTitleEmail,lbTitleWeb,
            lbTitleInvoice,lbTitleNumber,lbTitleDate,lbTitlePage,lbTitleOf,lbTitleBankDetails,lbTitlePayMethod,lbTitleHolder,lbTitleBranch,lbTitleDuedate,
            lbTitleTotalNet,lbTitleVAT,lbTitleTotalVAT,lbTitleWithholding,lbTitleTotalWithholding,lbTitleTotalInvoice,lbTitleTotalToPay,lbTitleTotalInvoice2,lbTitleTotalToPay2;
    @FXML private TextField tfDocNumber;
    @FXML private Button btnPrev,btnNext;
    @FXML private CheckBox cbPaid;
    @FXML private DatePicker dpDocDate;
    @FXML private ImageView ivLogo;
    
    public void initData(InvoiceCustomer invoice){
        this.invoice=invoice;
        language=getLanguage(invoice.getLanguage());
        
        lbFeatures.makeLabelEditable(lbDocNumber, tfDocNumber, "Document","docNumber",invoice.getIdDocument());
        lbFeatures.makeLabelEditable(lbDocDate, dpDocDate, "Document","docDate",invoice.getIdDocument());
        
        getObjects();
        setLogo();
        setData();
        getTotals();
        setTotals();
        setPageNumber();
        if(pages<2){
            btnNext.setVisible(false);
        }
        setTitles();
        setIndexOrdersPerPages();
        setOrders();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnPrev.setVisible(false);
        btnNext.setVisible(true);
        config=Configuration.getConfiguration();
    }
    
    @FXML protected void onClicPrev(){
        btnPrev.setVisible(true);
        btnNext.setVisible(true);
        page--;
        if(page==1){
            btnPrev.setVisible(false);
        }
        lbPageNumber.setText(String.valueOf(page));
        setOrders();
    }
    
    @FXML protected void onClicNext(){
        btnPrev.setVisible(true);
        btnNext.setVisible(true);
        page++;
        if(page==pages){
            btnNext.setVisible(false);
        }
        lbPageNumber.setText(String.valueOf(page));
        setOrders();
    }
    
    @FXML protected void onClicBack(){
        FXMLLoader loader=new FXMLLoader();
        Parent detailsCustomerView=null;
        ViewDetailsCustomerController controller=null;
        BorderPane home=null;
        
        loader.setLocation(getClass().getResource("viewDetailsCustomer.fxml"));
        try {
            detailsCustomerView=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewInvoiceCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(customer);
        home=(BorderPane)paneMain.getParent();
        home.setCenter(detailsCustomerView);
    }
    
    @FXML protected void print() {
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
        PrinterJob job = PrinterJob.createPrinterJob();
        
        if (job != null && job.showPrintDialog(paneInvoice.getScene().getWindow())) {
            boolean success = true;

            for (int i=0;i<pages;i++) {
                success = success && printNode(paneInvoice, job, pageLayout);
                if (!success) {
                    break;
                }
                if(page<pages){
                    onClicNext();
                }
            }
            if (success) {
                job.endJob();
            }
        }
        page=1;
        btnPrev.setVisible(false);
        btnNext.setVisible(true);
        lbPageNumber.setText(String.valueOf(page));
        setOrders();
    }
    
    @FXML protected void onClicSave(){
        ConnectionDB con=new ConnectionDB();
        query.addAll(lbFeatures.getQuery());
        if(!query.isEmpty()){
            con.openConnection();
            for(int i=0;i<query.size();i++){
                con.executeUpdate(query.get(i));
            }
            con.closeConnection();
        }
        lbFeatures.resetQuery();
        query.clear();
    }
    
    @FXML protected void onClicPaid(){
        invoice.updateDB("paid", cbPaid.isSelected());
    }
    
    private boolean printNode(Node node, PrinterJob job, PageLayout pageLayout){
        boolean success;
        double scaleX = pageLayout.getPrintableWidth() / node.getBoundsInParent().getWidth();
        double scaleY = pageLayout.getPrintableHeight() / node.getBoundsInParent().getHeight();
        double scale = Math.min(scaleX, scaleY);
        
        node.getTransforms().add(new Scale(scale, scale));
        success=job.printPage(pageLayout, node);
           
        node.getTransforms().clear();
        node.setTranslateX(0);
        node.setTranslateY(0);
        
        return success;
    }
    
    private void getObjects(){
        customer=invoice.getCustomer();
        user=invoice.getUser();
        bankAccount=invoice.getBankAccount();
        changeRate=invoice.getChangeRate();
        orders=invoice.getOrders();        
    }
    
    private void setData(){
        //isPaid
        cbPaid.setSelected(invoice.isPaid());
        //User info
        lbLegalName.setText(user.getLegalName());
        lbVATNumber.setText(user.getVatNumber());
        lbStreet.setText(user.getAddress().getStreet()+" "+user.getAddress().getStNumber()+" "+user.getAddress().getApt());
        lbCityCp.setText(user.getAddress().getCp()+" / "+user.getAddress().getCity());
        lbCountry.setText(user.getAddress().getCountry());
        lbEmail.setText(user.getEmail());
        lbWeb.setText(user.getWeb());
        //Invoice info
        setInvoiceInfo();
        //Customer info
        lbCustComName.setText(customer.getComName());
        lbCustLegalName.setText(customer.getLegalName());
        lbCustVATNumber.setText(customer.getVatNumber());
        lbCustStreet.setText(customer.getAddress().getStreet()+" "+customer.getAddress().getStNumber()+" "+customer.getAddress().getApt());
        lbCustCPCity.setText(customer.getAddress().getCp()+" / "+customer.getAddress().getCity());
        lbCustStateCountry.setText(customer.getAddress().getState()+" / "+customer.getAddress().getCountry());
        lbCustEmail.setText(customer.getEmail());
        lbCustWeb.setText(customer.getWeb());
        //Bank Details
        lbPayMethod.setText(customer.getPayMethod());
        lbHolder.setText(bankAccount.getHolder());
        lbBranch.setText(bankAccount.getBranch());
        lbIBAN.setText(bankAccount.getIban());
        lbDuedate.setText(invoice.getDuedate().format(formatter));
    }
    
    private void setInvoiceInfo(){
        lbDocDate.setText(invoice.getDocDate().format(formatter));
        lbDocNumber.setText(invoice.getDocNumber());
    }
    
    private void getTotals(){
        totalNet=invoice.getTotal();
        totalVAT=invoice.getTotalVAT();
        totalWithholding=invoice.getTotalWithholding();
        totalInvoice=totalNet+totalVAT;
        totalToPay=invoice.getTotalToPay();
    }
    
    private void setTotals(){
        lbTotalInvoice2.setVisible(false);
        lbTotalToPay2.setVisible(false);
        
        lbTotalNet.setText(String.format("%.2f"+invoice.getCurrency(),totalNet));
        lbVAT.setText(String.format("%.2f%%",customer.getDefaultVAT()));
        lbTotalVAT.setText(String.format("%.2f"+invoice.getCurrency(),totalVAT));
        lbWithholding.setText(String.format("%.2f%%",customer.getDefaultWithholding()));
        lbTotalWithholding.setText(String.format("%.2f"+invoice.getCurrency(),totalWithholding));
        lbTotalInvoice.setText(String.format("%.2f"+invoice.getCurrency(),totalInvoice));
        lbTotalToPay.setText(String.format("%.2f"+invoice.getCurrency(),totalToPay));
        if(changeRate.getIdChangeRate()!=1){
            lbTotalInvoice2.setVisible(true);
            lbTotalToPay2.setVisible(true);
            lbTotalInvoice2.setText(String.format("%.2f"+changeRate.getCurrency2(),totalInvoice*changeRate.getRate()));
            lbTotalToPay2.setText(String.format("%.2f"+changeRate.getCurrency2(),totalToPay*changeRate.getRate()));
        }
   }
    
    private void setOrders(){
        FXMLLoader loader;
        Parent ordersListView=null;
        ViewOrdersForDocumentController controller=null;
        
        vbOrders.getChildren().clear();
        for (int i=ordersIndex[page-1];i<ordersIndex[page];i++) {
            loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("viewOrdersForDocument.fxml"));
            try {
                ordersListView=loader.load();
            } catch (IOException ex) {
                Logger.getLogger(ViewInvoiceCustomerController.class.getName()).log(Level.SEVERE, null, ex);
            }
            controller=loader.getController();
            controller.initData(orders.get(i),changeRate);
            
            vbOrders.getChildren().add(ordersListView);
        }
    }
    
    private void setLogo(){
        InputStream isImage=null;
        File img=null;
        try {
            img=new File(config.getLogoPath());
            isImage = (InputStream) new FileInputStream(img);
            ivLogo.setImage(new Image(isImage));
            ivLogo.setFitWidth(imgSize);
            ivLogo.setFitHeight(imgSize);
            ivLogo.setPreserveRatio(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ViewNewInvoiceCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                isImage.close();
            } catch (IOException ex) {
                Logger.getLogger(ViewNewInvoiceCustomerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void setTitles(){
        lbTitleTotalInvoice2.setVisible(false);
        lbTitleTotalToPay2.setVisible(false);
        lbTitleName.setText(Translations.titleName[language]);
        lbTitleVATNumber.setText(Translations.titleVATNumber[language]);
        lbTitleAddress.setText(Translations.titleAddress[language]);
        lbTitleCPCity.setText(Translations.titleCPCity[language]);
        lbTitleCountry.setText(Translations.titleCountry[language]);
        lbTitleEmail.setText(Translations.titleEmail[language]);
        lbTitleWeb.setText(Translations.titleWeb[language]);
        lbTitleInvoice.setText(Translations.titleInvoice[language]);
        lbTitleNumber.setText(Translations.titleNumber[language]);
        lbTitlePage.setText(Translations.titlePage[language]);
        lbPageNumber.setText(String.valueOf(page));
        lbTitleOf.setText(Translations.titleOf[language]);
        lbPageTotal.setText(String.valueOf(pages));
        lbTitleDate.setText(Translations.titleDate[language]);
        lbTitleBankDetails.setText(Translations.titleBankDetails[language]);
        lbTitlePayMethod.setText(Translations.titlePayMethod[language]);
        lbTitleHolder.setText(Translations.titleHolder[language]);
        lbTitleBranch.setText(Translations.titleBranch[language]);
        lbTitleDuedate.setText(Translations.titleDuedate[language]);
        lbTitleTotalNet.setText(Translations.titleTotalNet[language]);
        lbTitleVAT.setText(Translations.titleVAT[language]);
        lbTitleTotalVAT.setText(Translations.titleTotalVAT[language]);
        lbTitleWithholding.setText(Translations.titleWithholding[language]);
        lbTitleTotalWithholding.setText(Translations.titleTotalWithholding[language]);
        lbTitleTotalInvoice.setText(Translations.titleTotalInvoice[language]);
        lbTitleTotalToPay.setText(Translations.titleTotalToPay[language]);
        if(changeRate.getIdChangeRate()!=1){
            lbTitleTotalInvoice2.setVisible(true);
            lbTitleTotalToPay2.setVisible(true);
            lbTitleTotalInvoice2.setText(Translations.titleTotalInvoice[language]);
            lbTitleTotalToPay2.setText(Translations.titleTotalToPay[language]);
        }
    }
    
    private int getLanguage(String language){
        int l=0;
        switch (language) {
            case "English":
                l=0;
                break;
            case "Español":
                l=1;
                break;
            case "Français":
                l=2;
                break;
            default:
                break;
        }
        return l;
    }
    
    private void setPageNumber(){
        int linesNumber=0;
        
        for(int i=0;i<orders.size();i++){
            linesNumber=linesNumber+orders.get(i).getItems().size()+1;
            if(linesNumber>maxLinesPerPage){
                pages++;
                linesNumber=orders.get(i).getItems().size()+1;
            }
        }
        ordersIndex=new int[pages+1];
    }
    
    private void setIndexOrdersPerPages(){
        int linesNumber=0;
        int j=1;
        
        ordersIndex[0]=0;
        
        for(int i=0;i<orders.size();i++){
            linesNumber=linesNumber+orders.get(i).getItems().size()+1;
            if(linesNumber>maxLinesPerPage){
                ordersIndex[j]=i;
                j++;
                linesNumber=orders.get(i).getItems().size()+1;
            }
        }
        ordersIndex[pages]=orders.size();
    }
}
