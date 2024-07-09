/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.config.Configuration;
import invoicingapp_monolithic.CustomProv;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewNewInvoiceController implements Initializable {

    private ArrayList<Orders> pendingOrders;
    private ArrayList<CustomProv> companies=new ArrayList();
    private CustomProv company;
    private Users user=new Users();
    private InvoiceCustomer invoice=new InvoiceCustomer();
    private Configuration config;
    private String logoPath;
    private int imgSize=150;
    
    @FXML private VBox VBoxNewInvoice,vbItems;
    @FXML private ComboBox cbCustomProvs;
    @FXML private Label lbVATNumber,lbStreet,lbCityCp,lbEmail,lbWeb,lbLegalName,
            lbCustLegalName,lbCustVATNumber,lbCustStreet,lbCustCPCity,lbCustStateCountry,lbCustEmail,lbCustWeb,
            lbTotalNet,lbVAT,lbTotalVAT,lbWithholding,lbTotalWithholding,lbTotalInvoice,lbTotalToPay;
    @FXML ImageView ivLogo;
    @FXML CheckBox cbSelectAll;
    @FXML DatePicker dpDocDate;
    @FXML TextField tfDocNumber;
    
    public void initData(CustomProv customProv){
        this.company=customProv;
        pendingOrders=company.getOrdersFromDB(CustomProv.NOTBILLED);
        
        for(int i=0;i<companies.size();i++){
            if(companies.get(i).getIdCustomProv()==company.getIdCustomProv()){
                cbCustomProvs.getSelectionModel().select(i);
            }
        }
        updateData();
        getOrders();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        InputStream isImage=null;
        File img=null;
        
        config=Configuration.getConfiguration();
        logoPath=config.getLogoPath();
        companies=CustomProv.getAllCustomProvFromDB(CustomProv.ENABLED);
        populateCbCustomProvs();
        user.getFromDB(config.getIdUser());
        lbLegalName.setText(user.getLegalName());
        lbVATNumber.setText(user.getVatNumber());
        lbStreet.setText(user.getAddress().getStreet()+" "+user.getAddress().getStNumber()+" "+user.getAddress().getApt());
        lbCityCp.setText(user.getAddress().getCp()+" / "+user.getAddress().getCity());
        lbEmail.setText(user.getEmail());
        lbWeb.setText(user.getWeb());
        try {
            img=new File(logoPath);
            isImage = (InputStream) new FileInputStream(img);
            ivLogo.setImage(new Image(isImage));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ViewNewInvoiceController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                isImage.close();
            } catch (IOException ex) {
                Logger.getLogger(ViewNewInvoiceController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @FXML protected void getSelectionCBCustomProvs(){
        company=(CustomProv) cbCustomProvs.getSelectionModel().getSelectedItem();
        updateData();
        getOrders();
    }
    
    @FXML protected void selectAllOrders(){
        for(int i=0;i<pendingOrders.size();i++){
            pendingOrders.get(i).setSelected(cbSelectAll.isSelected());
        }
        getOrders();
    }
    
    @FXML protected void save(){
        invoice.setChangeRate(changeRate);
        invoice.setCustomer(company);
        invoice.setDocDate(dpDocDate.getValue());
        invoice.setDocNumber(tfDocNumber.getText());
        invoice.setDuedate(dpDocDate.getValue().plusDays(company.getduedate()));
        invoice.setOrders(orders);
        invoice.setPaid(false);
        invoice.setTitle("FACTURA");
        invoice.setUser(user);
        invoice.setVat(company.getDefaultVAT());
        invoice.setWithholding(company.getDefaultWithholding());
        
        
    }
    
    @FXML protected void cancel(){
        
    }
    
    private void populateCbCustomProvs(){
        ObservableList<CustomProv> companyObs =FXCollections.observableArrayList(companies);
        cbCustomProvs.setItems(companyObs);
        
        cbCustomProvs.setCellFactory(new Callback<ListView<CustomProv>, ListCell<CustomProv>>() {
            @Override
            public ListCell<CustomProv> call(ListView<CustomProv> p) {
                return new ListCell<CustomProv>() {
                    @Override
                    protected void updateItem(CustomProv item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getComName());
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });
        
        cbCustomProvs.setButtonCell(new ListCell<CustomProv>() {
            @Override
            protected void updateItem(CustomProv item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.getComName());
                } else {
                    setText(null);
                }
            }
        });
    }
    
    private void getOrders(){
        FXMLLoader loader;
        Parent ordersListView=null;
        ViewOrdersListController controller=null;
        
        vbItems.getChildren().clear();
        for (int j=0;j<pendingOrders.size();j++) {
            loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("viewOrdersList.fxml"));
            try {
                ordersListView=loader.load();
            } catch (IOException ex) {
                Logger.getLogger(ViewNewInvoiceController.class.getName()).log(Level.SEVERE, null, ex);
            }
            controller=loader.getController();
            controller.initData(pendingOrders.get(j),this);
            vbItems.getChildren().add(ordersListView);
        }
        updateTotals();
    }

    private void updateData(){
        lbCustLegalName.setText(company.getLegalName());
        lbCustVATNumber.setText(company.getVatNumber());
        lbCustStreet.setText(company.getAddress().getStreet()+" "+company.getAddress().getStNumber()+" "+company.getAddress().getApt());
        lbCustCPCity.setText(company.getAddress().getCp()+" / "+company.getAddress().getCity());
        lbCustStateCountry.setText(company.getAddress().getState()+" / "+company.getAddress().getCountry());
        lbCustEmail.setText(company.getEmail());
        lbCustWeb.setText(company.getWeb());
   }
   
    protected void updateTotals(){
        double totalNet=0;
        double totalVAT=0;
        double totalWithholding=0;
        double totalInvoice=0;
        double totalToPay=0;
        
        for(int i=0;i<pendingOrders.size();i++){
            if(pendingOrders.get(i).isSelected()){
                totalNet=totalNet+pendingOrders.get(i).getTotalOrder();
                totalVAT=totalVAT+(pendingOrders.get(i).getTotalOrder()*(company.getDefaultVAT()/100));
                totalWithholding=totalWithholding+(pendingOrders.get(i).getTotalOrder()*(company.getDefaultWithholding()/100));
                totalInvoice=totalInvoice+totalNet+totalVAT;
                totalToPay=totalInvoice+totalNet+totalVAT-totalWithholding;
            }
        }
        lbTotalNet.setText(String.format("%.2f€",totalNet));
        lbVAT.setText(String.format("%.2f%%",company.getDefaultVAT()));
        lbTotalVAT.setText(String.format("%.2f€",totalVAT));
        lbWithholding.setText(String.format("%.2f%%",company.getDefaultWithholding()));
        lbTotalWithholding.setText(String.format("%.2f€",totalWithholding));
        lbTotalInvoice.setText(String.format("%.2f€",totalInvoice));
        lbTotalToPay.setText(String.format("%.2f€",totalToPay));
        
   }
}
