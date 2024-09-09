/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.bbdd.ConnectionDB;
import com.invoicingapp.config.Configuration;
import com.invoicingapp.config.Translations;
import com.invoicingapp.tools.LabelFeatures;
import invoicingapp_monolithic.Orders;
import invoicingapp_monolithic.Provider;
import invoicingapp_monolithic.PurchaseOrder;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewPOController implements Initializable {

    private PurchaseOrder po;
    private Provider provider;
    private Users user;
    private Configuration config;
    private LabelFeatures lbFeatures=new LabelFeatures();
    private ArrayList<Orders> orders=new ArrayList();
    private ArrayList<String> query=new ArrayList();
    private boolean changes=false;
    private int language;
    private int pages=1;
    private int page=1;    
    private double totalNet=0;
    private double totalPO=0;
    
    private int[] ordersIndex;
    private final int maxLinesPerPage=24;
    private final int imgSize=150;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    @FXML private VBox panePO,vbOrders,paneMain;
    @FXML private Label lbVATNumber,lbStreet,lbCityCp,lbCountry,lbEmail,lbWeb,lbLegalName,
            lbProvComName,lbProvLegalName,lbProvVATNumber,lbProvStreet,lbProvCPCity,lbProvStateCountry,lbProvEmail,lbProvWeb,
            lbDocDate,lbDocNumber,lbPageNumber,lbPageTotal,lbDeliveryNote,lbTotalNet,lbVAT,lbTotal,
            lbTitleName,lbTitleVATNumber,lbTitleAddress,lbTitleCPCity,lbTitleCountry,lbTitleEmail,lbTitleWeb,
            lbTitlePO,lbTitleNumber,lbTitleDate,lbTitlePage,lbTitleOf,lbTitleDeliveryNote,
            lbTitleTotalNet,lbTitleVAT,lbTitleTotal;
    @FXML private TextField tfDocNumber;
    @FXML private Button btnPrev,btnNext;
    @FXML private ChoiceBox<String> cbStatus;
    @FXML private DatePicker dpDocDate;
    @FXML private ImageView ivLogo;
    
    public void initData(PurchaseOrder po){
        this.po=po;
        language=getLanguage(po.getLanguage());
        populateCbStatus();
        cbStatus.getSelectionModel().select(po.getStatus());
        
        lbFeatures.makeLabelEditable(lbDocNumber, tfDocNumber, "Document","docNumber",po.getIdDocument());
        lbFeatures.makeLabelEditable(lbDocDate, dpDocDate, "Document","docDate",po.getIdDocument());
        
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
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnPrev.setVisible(false);
        btnNext.setVisible(true);
        config=Configuration.loadConfiguration();
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
        Parent detailsProviderView=null;
        ViewDetailsProviderController controller=null;
        BorderPane home=null;
        
        loader.setLocation(getClass().getResource("viewDetailsProvider.fxml"));
        try {
            detailsProviderView=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewPOController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(provider);
        home=(BorderPane)paneMain.getParent();
        home.setCenter(detailsProviderView);
    }
    
    @FXML protected void print() {
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
        PrinterJob job = PrinterJob.createPrinterJob();
        
        if (job != null && job.showPrintDialog(panePO.getScene().getWindow())) {
            boolean success = true;

            for (int i=0;i<pages;i++) {
                success = success && printNode(panePO, job, pageLayout);
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
    
    private void getSelectionCBStatus(String newValue){
        po.setStatus(getStatus(newValue));
        po.updateDB("status", po.getStatus());
    }
    
    private void populateCbStatus(){
        cbStatus.getItems().addAll(Translations.status[language]);
        cbStatus.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            getSelectionCBStatus(newValue);
        });
    }
    
    private int getStatus(String status){
        int result=0;
        
        if(status.equals(Translations.status[language][0])){
            result=0;
        }else if(status.equals(Translations.status[language][1])){
            result=1;
        }else if(status.equals(Translations.status[language][2])){
            result=2;
        }
        return result;
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
        provider=po.getProvider();
        user=po.getUser();
        orders=po.getOrders();
        System.out.println("POController linea 249: "+orders.size());
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
        setPOInfo();
        //Customer info
        lbProvComName.setText(provider.getComName());
        lbProvLegalName.setText(provider.getLegalName());
        lbProvVATNumber.setText(provider.getVatNumber());
        lbProvStreet.setText(provider.getAddress().getStreet()+" "+provider.getAddress().getStNumber()+" "+provider.getAddress().getApt());
        lbProvCPCity.setText(provider.getAddress().getCp()+" / "+provider.getAddress().getCity());
        lbProvStateCountry.setText(provider.getAddress().getState()+" / "+provider.getAddress().getCountry());
        lbProvEmail.setText(provider.getEmail());
        lbProvWeb.setText(provider.getWeb());
        //Notes
        lbDeliveryNote.setText(po.getDeadline().format(formatter));
    }
    
    private void setPOInfo(){
        lbDocDate.setText(po.getDocDate().format(formatter));
        lbDocNumber.setText(po.getDocNumber());
    }
    
    private void getTotals(){
        totalNet=po.getTotal();
        totalPO=totalNet+(totalNet*(provider.getDefaultVAT()/100));
    }
    
    private void setTotals(){
        lbTotalNet.setText(String.format("%.2f"+po.getCurrency(),totalNet));
        lbVAT.setText(String.format("%.2f%%",provider.getDefaultVAT()));
        lbTotal.setText(String.format("%.2f"+po.getCurrency(),totalPO));
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
            controller.initData(orders.get(i),po.getCurrency());
            
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
        lbTitlePO.setText(Translations.titlePO[language]);
        lbTitleNumber.setText(Translations.titleNumber[language]);
        lbTitlePage.setText(Translations.titlePage[language]);
        lbPageNumber.setText(String.valueOf(page));
        lbTitleOf.setText(Translations.titleOf[language]);
        lbPageTotal.setText(String.valueOf(pages));
        lbTitleDate.setText(Translations.titleDate[language]);
        lbTitleDeliveryNote.setText(Translations.titleDelivery[language]);
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
    
}
