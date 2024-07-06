/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.config.Configuration;
import invoicingapp_monolithic.CustomProv;
import invoicingapp_monolithic.Orders;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewNewInvoiceCustomerController implements Initializable {

    private ArrayList<Orders> pendingOrders;
    private ArrayList<CustomProv> companies=new ArrayList();
    private CustomProv company;
    private Configuration config=Configuration.getConfiguration();
    private String logoPath=config.getLogoPath();
    private int node=1;
    
    @FXML private VBox VBoxNewInvoice;
    @FXML private ComboBox cbCustomProvs;
    
    public void initData(CustomProv customProv){
        this.company=customProv;
        
        for(int i=0;i<companies.size();i++){
            if(companies.get(i).getIdCustomProv()==company.getIdCustomProv()){
                cbCustomProvs.getSelectionModel().select(i);
            }
        }
        
        getOrders();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        companies=CustomProv.getAllCustomProvFromDB(CustomProv.ENABLED);
        populateCbCustomProvs();
    }
    
    @FXML protected void getSelectionCBCustomProvs(){
        company=(CustomProv) cbCustomProvs.getSelectionModel().getSelectedItem();
        getOrders();
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
        pendingOrders=company.getOrdersFromDB(CustomProv.NOTBILLED);
        FXMLLoader loader;
        Parent ordersListView=null;
        ViewOrdersListController controller=null;
        
        for(int i=1;i<node;i++){
            VBoxNewInvoice.getChildren().remove(node);
        }
        
        for (int j=0;j<pendingOrders.size();j++) {
            loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("viewOrdersList.fxml"));
            try {
                ordersListView=loader.load();
            } catch (IOException ex) {
                Logger.getLogger(ViewNewInvoiceCustomerController.class.getName()).log(Level.SEVERE, null, ex);
            }
            controller=loader.getController();
            controller.initData(pendingOrders.get(j));
            VBoxNewInvoice.getChildren().add(ordersListView);
            node++;
        }  
    }
}
