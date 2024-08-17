/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.CustomProv;
import invoicingapp_monolithic.Customer;
import invoicingapp_monolithic.InvoiceCustomer;
import invoicingapp_monolithic.InvoiceProvider;
import invoicingapp_monolithic.Provider;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewInvoicesProviderController implements Initializable {

    private ObservableList<InvoiceProvider> invoices;
    private ArrayList<Provider> companies=new ArrayList();
    private Provider provider;
    
    @FXML private TableView<InvoiceProvider> tableInvoices;
    @FXML private TableColumn<InvoiceProvider, String> columnComName, columnNbr,columnTotal;
    @FXML private TableColumn<InvoiceProvider, LocalDate> columnDate;
    @FXML private ComboBox<Provider> cbProviders;
    @FXML private VBox paneInvoicesProvider;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        companies=Provider.getAllProvidersFromDB(CustomProv.ENABLED);
        populateCbCustomers();
        invoices=FXCollections.observableArrayList(InvoiceProvider.getAllInvoicesFromDB());
        createTableInvoices();
        tableInvoices.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                onSeeInvoice();
            }
        });
    }
    
    @FXML protected void onCreateInvoice(){
        FXMLLoader loader=new FXMLLoader();
        Parent invoiceProviderView=null;
        ViewNewInvoiceProviderController controller=null;
        BorderPane home=(BorderPane)paneInvoicesProvider.getParent();
        
        loader.setLocation(getClass().getResource("viewNewInvoiceProvider.fxml"));
        try {
            invoiceProviderView=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewInvoicesProviderController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(2);
        home.setCenter(invoiceProviderView);
    }
    
    @FXML protected void onSeeInvoice(){
        FXMLLoader loader=new FXMLLoader();
        Parent invoiceView=null;
        ViewInvoiceProviderController controller=null;
        BorderPane home=null;
        
        loader.setLocation(getClass().getResource("viewInvoiceProvider.fxml"));
        try {
            invoiceView=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewInvoicesProviderController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(tableInvoices.getSelectionModel().getSelectedItem());
        home=(BorderPane)paneInvoicesProvider.getParent();
        home.setCenter(invoiceView);
    }
    
    @FXML protected void getSelectionCBProviders(){
        provider=cbProviders.getSelectionModel().getSelectedItem();
        provider.getInvoicesFromDB();
        invoices=FXCollections.observableArrayList(provider.getInvoices());
        createTableInvoices();
    }
    
    private void createTableInvoices(){
        for(int i=0;i<invoices.size();i++){
            invoices.get(i).setComName();
            invoices.get(i).setTotalString();
        }
        columnComName.setCellValueFactory(new PropertyValueFactory<>("comName"));
        columnNbr.setCellValueFactory(new PropertyValueFactory<>("docNumber"));
        columnDate.setCellValueFactory(new PropertyValueFactory<>("docDate"));
        columnTotal.setCellValueFactory(new PropertyValueFactory<>("totalString"));
                
        tableInvoices.setItems(invoices);
    }
    
    private void populateCbCustomers(){
        ObservableList<Provider> list =FXCollections.observableArrayList(companies);
        cbProviders.setItems(list);
        
        cbProviders.setCellFactory(new Callback<ListView<Provider>, ListCell<Provider>>() {
            @Override
            public ListCell<Provider> call(ListView<Provider> p) {
                return new ListCell<Provider>() {
                    @Override
                    protected void updateItem(Provider item, boolean empty) {
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
        
        cbProviders.setButtonCell(new ListCell<Provider>() {
            @Override
            protected void updateItem(Provider item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.getComName());
                } else {
                    setText(null);
                }
            }
        });
    }  
    
}
