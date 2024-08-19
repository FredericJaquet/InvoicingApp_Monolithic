/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.CustomProv;
import invoicingapp_monolithic.Customer;
import invoicingapp_monolithic.InvoiceCustomer;
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


public class ViewInvoicesCustomerController implements Initializable {

    private ObservableList<InvoiceCustomer> invoices;
    private ArrayList<Customer> companies=new ArrayList();
    private Customer customer;
    
    @FXML private TableView<InvoiceCustomer> tableInvoices;
    @FXML private TableColumn<InvoiceCustomer, String> columnComName, columnNbr,columnTotal;
    @FXML private TableColumn<InvoiceCustomer, LocalDate> columnDate;
    @FXML private ComboBox<Customer> cbCustomers;
    @FXML private VBox paneInvoicesCustomer;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        onClicAll();
        companies=Customer.getAllCustomersFromDB(CustomProv.ENABLED);
        populateCbCustomers();
        tableInvoices.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                onSeeInvoice();
            }
        });
    }
    
    @FXML protected void onCreateInvoice(){
        FXMLLoader loader=new FXMLLoader();
        Parent invoiceCustomerView=null;
        ViewNewInvoiceCustomerController controller=null;
        BorderPane home=(BorderPane)paneInvoicesCustomer.getParent();
        
        loader.setLocation(getClass().getResource("viewNewInvoiceCustomer.fxml"));
        try {
            invoiceCustomerView=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewDetailsCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(2);
        home.setCenter(invoiceCustomerView);
    }
    
    @FXML protected void onSeeInvoice(){
        if(tableInvoices.getSelectionModel().getSelectedItem()!=null){
            FXMLLoader loader=new FXMLLoader();
            Parent invoiceView=null;
            ViewInvoiceCustomerController controller=null;
            BorderPane home=null;
        
            loader.setLocation(getClass().getResource("viewInvoiceCustomer.fxml"));
            try {
                invoiceView=loader.load();
            } catch (IOException ex) {
                Logger.getLogger(ViewInvoicesCustomerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
            controller=loader.getController();
            controller.initData(tableInvoices.getSelectionModel().getSelectedItem(),2);
            home=(BorderPane)paneInvoicesCustomer.getParent();
            home.setCenter(invoiceView);
        }
    }
    
    @FXML protected void onClicPaid(){
        invoices=FXCollections.observableArrayList(InvoiceCustomer.getAllInvoicesFromDB(InvoiceCustomer.PAID));
        createTableInvoices();
    }
    
    @FXML protected void onClicNotPaid(){
        invoices=FXCollections.observableArrayList(InvoiceCustomer.getAllInvoicesFromDB(InvoiceCustomer.NOTPAID));
        createTableInvoices();
    }
    
    @FXML protected void onClicAll(){
        invoices=FXCollections.observableArrayList(InvoiceCustomer.getAllInvoicesFromDB());
        createTableInvoices();
    }
    
    @FXML protected void getSelectionCBCustomers(){
        customer=cbCustomers.getSelectionModel().getSelectedItem();
        customer.getInvoicesFromDB();
        invoices=FXCollections.observableArrayList(customer.getInvoices());
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
        ObservableList<Customer> list =FXCollections.observableArrayList(companies);
        cbCustomers.setItems(list);
        
        cbCustomers.setCellFactory((ListView<Customer> p) -> new ListCell<Customer>() {
            @Override
            protected void updateItem(Customer item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.getComName());
                } else {
                    setText(null);
                }
            }
        });
        
        cbCustomers.setButtonCell(new ListCell<Customer>() {
            @Override
            protected void updateItem(Customer item, boolean empty) {
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
