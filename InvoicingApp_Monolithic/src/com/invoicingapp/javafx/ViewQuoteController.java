/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.bbdd.ConnectionDB;
import com.invoicingapp.config.Configuration;
import com.invoicingapp.config.Translations;
import invoicingapp_monolithic.Customer;
import invoicingapp_monolithic.Orders;
import invoicingapp_monolithic.Quotes;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;


public class ViewQuoteController implements Initializable {

    private Quotes quote;
    private Customer customer;
    private Users user;
    private Configuration config;
    private ArrayList<Orders> orders=new ArrayList();
    private boolean changes=false;
    private int language;
    private int pages=1;
    private int page=1;    
    private double totalNet=0;
    private double totalQuote=0;
    private String query="";
    private int[] ordersIndex;
    private final int maxLinesPerPage=24;
    private final int imgSize=150;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    @FXML private VBox paneQuote,vbOrders,paneMain;
    @FXML private Label lbVATNumber,lbStreet,lbCityCp,lbCountry,lbEmail,lbWeb,lbLegalName,
            lbCustComName,lbCustLegalName,lbCustVATNumber,lbCustStreet,lbCustCPCity,lbCustStateCountry,lbCustEmail,lbCustWeb,
            lbDocDate,lbDocNumber,lbPageNumber,lbPageTotal,lbPaymentNote,lbDeliveryNote,lbTotalNet,lbVAT,lbTotal,
            lbTitleName,lbTitleVATNumber,lbTitleAddress,lbTitleCPCity,lbTitleCountry,lbTitleEmail,lbTitleWeb,
            lbTitleQuote,lbTitleNumber,lbTitleDate,lbTitlePage,lbTitleOf,lbTitlePaymentNote,lbTitleDeliveryNote,
            lbTitleTotalNet,lbTitleVAT,lbTitleTotal;
    @FXML private TextField tfDocNumber;
    @FXML private Button btnPrev,btnNext;
    @FXML private ChoiceBox status;
    @FXML private DatePicker dpDocDate;
    @FXML private ImageView ivLogo;
    
    public void initData(Quotes quote){
        this.quote=quote;
        language=getLanguage(quote.getLanguage());
        
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
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnPrev.setVisible(false);
        btnNext.setVisible(true);
        config=Configuration.getConfiguration();
        
        makeLabelEditable(lbDocNumber, tfDocNumber, "Document","docNumber");
        makeLabelEditable(lbDocDate, dpDocDate, "Document","docDate");
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
            Logger.getLogger(ViewCustomersController.class.getName()).log(Level.SEVERE, null, ex);
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
        
        if (job != null && job.showPrintDialog(paneQuote.getScene().getWindow())) {
            boolean success = true;

            for (int i=0;i<pages;i++) {
                success = success && printNode(paneQuote, job, pageLayout);
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
        
        if((!query.equals(""))&&changes){
            con.openConnection();
            con.noReturnQuery(query);
            con.closeConnection();
        }
        query="";
        changes=false;
    }
    
    @FXML protected void getSelectionCBStatus(){
    completar
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
        customer=quote.getCustomer();
        user=quote.getUser();
        orders=quote.getOrders();        
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
        //Notes
        lbPaymentNote.setText(quote.getNotePayment());
        lbDeliveryNote.setText(quote.getNoteDelivery());
    }
    
    private void setInvoiceInfo(){
        lbDocDate.setText(quote.getDocDate().format(formatter));
        lbDocNumber.setText(quote.getDocNumber());
    }
    
    private void getTotals(){
        totalNet=quote.getTotal();
        totalQuote=totalNet+(totalNet*(customer.getDefaultVAT()/100));
    }
    
    private void setTotals(){
        lbTotalNet.setText(String.format("%.2f"+quote.getCurrency(),totalNet));
        lbVAT.setText(String.format("%.2f%%",customer.getDefaultVAT()));
        lbTotal.setText(String.format("%.2f"+quote.getCurrency(),totalQuote));
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
            controller.initData(orders.get(i),quote.getCurrency());
            
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
        lbTitleName.setText(Translations.titleName[language]);
        lbTitleVATNumber.setText(Translations.titleVATNumber[language]);
        lbTitleAddress.setText(Translations.titleAddress[language]);
        lbTitleCPCity.setText(Translations.titleCPCity[language]);
        lbTitleCountry.setText(Translations.titleCountry[language]);
        lbTitleEmail.setText(Translations.titleEmail[language]);
        lbTitleWeb.setText(Translations.titleWeb[language]);
        lbTitleQuote.setText(Translations.titleQuote[language]);
        lbTitleNumber.setText(Translations.titleNumber[language]);
        lbTitlePage.setText(Translations.titlePage[language]);
        lbPageNumber.setText(String.valueOf(page));
        lbTitleOf.setText(Translations.titleOf[language]);
        lbPageTotal.setText(String.valueOf(pages));
        lbTitleDate.setText(Translations.titleDate[language]);
        lbTitleDeliveryNote.setText(Translations.titleDelivery[language]);
        lbTitlePaymentNote.setText(Translations.titlePayment[language]);
        lbTitleTotalNet.setText(Translations.titleTotalNet[language]);
        lbTitleVAT.setText(Translations.titleVAT[language]);
        lbTitleTotal.setText(Translations.titleTotalQuote[language]);
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
    
    private void makeLabelEditable(Label label, TextField textField, String tableDB, String fieldDB) {
        textField.setVisible(false);

        label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getClickCount() == 2) {
                switchToTextField(label, textField);
            }
        });

        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                switchToLabel(label, textField, tableDB, fieldDB);
            }
        });
    }
    
    private void switchToTextField(Label label, TextField textField) {
        textField.setText(label.getText());
        textField.setVisible(true);
        textField.requestFocus();
        label.setVisible(false);
    }
    
    private void switchToLabel(Label label, TextField textField, String tableDB, String fieldDB) {
        label.setText(textField.getText());
        label.setVisible(true);
        textField.setVisible(false);
        getQuery(label,tableDB,fieldDB);
    }
    
    private void makeLabelEditable(Label label, DatePicker datePicker, String tableDB, String fieldDB) {
        datePicker.setVisible(false);
        label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getClickCount() == 2) {
                switchToDatePicker(label, datePicker);
            }
        });

        datePicker.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                switchToLabel(label, datePicker, tableDB, fieldDB);
            }
        });
    }
    
    private void switchToDatePicker(Label label, DatePicker datePicker) {
        datePicker.setVisible(true);
        label.setVisible(false);
    }
    
    private void switchToLabel(Label label, DatePicker datePicker, String tableDB, String fieldDB) {
        label.setText(datePicker.getValue().toString());
        label.setVisible(true);
        datePicker.setVisible(false);
        getQuery(label,tableDB,fieldDB);
        label.setText(datePicker.getValue().format(formatter));
    }
    
    private void getQuery(Label label, String tableDB, String fieldDB){
        String newValue=label.getText();
        query=query.concat("UPDATE "+tableDB+" SET "+fieldDB+"='"+newValue+"' WHERE idDocument="+quote.getIdDocument()+";");
        changes=true;
    }
    
}
