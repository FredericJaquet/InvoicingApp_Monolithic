/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.config.Configuration;
import com.invoicingapp.config.Translations;
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
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewInvoiceCustomerController implements Initializable {

    private InvoiceCustomer invoice;
    private Customer customer;
    private Users user;
    private BankAccount bankAccount;
    private Configuration config;
    private ChangeRate changeRate;
    private ArrayList<Orders> orders=new ArrayList();
    private int imgSize=175;
    private int language;
    private int pages=1;
    private int page=1;
    private int maxLinesPerPage=25;
    private double totalNet=0;
    private double totalVAT=0;
    private double totalWithholding=0;
    private double totalInvoice=0;
    private double totalToPay=0;
    private int[] ordersIndex;
    
    @FXML private VBox paneInvoice,vbOrders,paneMain;
    
    @FXML private Label lbVATNumber,lbStreet,lbCityCp,lbCountry,lbEmail,lbWeb,lbLegalName,
            lbCustComName,lbCustLegalName,lbCustVATNumber,lbCustStreet,lbCustCPCity,lbCustStateCountry,lbCustEmail,lbCustWeb,
            lbPageNumber,lbPageTotal,lbIBAN,lbHolder,lbBranch,lbPayMethod,lbDuedate,
            lbTotalNet,lbVAT,lbTotalVAT,lbWithholding,lbTotalWithholding,lbTotalInvoice,lbTotalToPay,lbTotalInvoice2,lbTotalToPay2,
            lbTitleName,lbTitleVATNumber,lbTitleAddress,lbTitleCPCity,lbTitleCountry,lbTitleEmail,lbTitleWeb,
            lbTitleInvoice,lbTitleNumber,lbTitleDate,lbTitlePage,lbTitleOf,lbTitleBankDetails,lbTitlePayMethod,lbTitleHolder,lbTitleBranch,lbTitleDuedate,
            lbTitleTotalNet,lbTitleVAT,lbTitleTotalVAT,lbTitleWithholding,lbTitleTotalWithholding,lbTitleTotalInvoice,lbTitleTotalToPay,lbTitleTotalInvoice2,lbTitleTotalToPay2;
    @FXML Button btnPrev,btnNext;
    @FXML private ImageView ivLogo;
    
    public void initData(InvoiceCustomer invoice){
        this.invoice=invoice;
        language=getLanguage(invoice.getLanguage());
        config=Configuration.getConfiguration();
        getObjects();
        setLogo();
        setData();
        getTotals();
        setTotals();
        setPageNumber();
        setTitles();
        setIndexOrdersPerPages();
        setOrders();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnPrev.setVisible(false);
        btnNext.setVisible(true);
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
    
    private void getObjects(){
        customer=invoice.getCustomer();
        user=invoice.getUser();
        bankAccount=invoice.getBankAccount();
        changeRate=invoice.getChangeRate();
        orders=invoice.getOrders();        
    }
    
    private void setData(){ 
        //User info
        lbLegalName.setText(user.getLegalName());
        lbVATNumber.setText(user.getVatNumber());
        lbStreet.setText(user.getAddress().getStreet()+" "+user.getAddress().getStNumber()+" "+user.getAddress().getApt());
        lbCityCp.setText(user.getAddress().getCp()+" / "+user.getAddress().getCity());
        lbCountry.setText(user.getAddress().getCountry());
        lbEmail.setText(user.getEmail());
        lbWeb.setText(user.getWeb());
        //Customer info
        lbCustComName.setText(customer.getComName());
        lbCustLegalName.setText(customer.getLegalName());
        lbCustVATNumber.setText(customer.getVatNumber());
        lbCustStreet.setText(customer.getAddress().getStreet()+" "+customer.getAddress().getStNumber()+" "+customer.getAddress().getApt());
        lbCustCPCity.setText(customer.getAddress().getCp()+" / "+customer.getAddress().getCity());
        lbCustStateCountry.setText(customer.getAddress().getState()+" / "+customer.getAddress().getCountry());
        lbCustEmail.setText(customer.getEmail());
        lbCustWeb.setText(customer.getWeb());
        lbPayMethod.setText(customer.getPayMethod());
        lbHolder.setText(bankAccount.getHolder());
        lbBranch.setText(bankAccount.getBranch());
        lbIBAN.setText(bankAccount.getIban());
        lbDuedate.setText(invoice.getDuedate().toString());
    }
    
    private void getTotals(){
        totalNet=invoice.getTotal();
        totalVAT=invoice.getTotalVAT();
        totalWithholding=invoice.getTotalWithholding();
        totalToPay=invoice.getTotalToPay();
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
    
    private void setTotals(){
        lbTotalInvoice2.setVisible(false);
        lbTotalToPay2.setVisible(false);
        
        lbTotalNet.setText(String.format("%.2f"+changeRate.getCurrency1(),totalNet));
        lbVAT.setText(String.format("%.2f%%",customer.getDefaultVAT()));
        lbTotalVAT.setText(String.format("%.2f"+changeRate.getCurrency1(),totalVAT));
        lbWithholding.setText(String.format("%.2f%%",customer.getDefaultWithholding()));
        lbTotalWithholding.setText(String.format("%.2f"+changeRate.getCurrency1(),totalWithholding));
        lbTotalInvoice.setText(String.format("%.2f"+changeRate.getCurrency1(),totalInvoice));
        lbTotalToPay.setText(String.format("%.2f"+changeRate.getCurrency1(),totalToPay));
        if(changeRate.getIdChangeRate()!=1){
            lbTotalInvoice2.setVisible(true);
            lbTotalToPay2.setVisible(true);
            lbTotalInvoice2.setText(String.format("%.2f"+changeRate.getCurrency2(),totalInvoice*changeRate.getRate()));
            lbTotalToPay2.setText(String.format("%.2f"+changeRate.getCurrency2(),totalToPay*changeRate.getRate()));
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
        lbPageNumber.setText(String.valueOf(page));
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
