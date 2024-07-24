/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.CustomProv;
import invoicingapp_monolithic.Customer;
import invoicingapp_monolithic.InvoiceCustomer;
import invoicingapp_monolithic.Quotes;
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
public class ViewQuotesController implements Initializable {

    private ObservableList<Quotes> quotes;
    private ArrayList<Customer> companies=new ArrayList();
    private Customer customer;
    
    @FXML private TableView<Quotes> tableQuotes;
    @FXML private TableColumn<Quotes, String> columnComName, columnNbr,columnTotal;
    @FXML private TableColumn<Quotes, LocalDate> columnDate;
    @FXML private ComboBox<Customer> cbCustomers;
    @FXML private VBox paneQuotes;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        onClicAll();
        companies=Customer.getAllCustomersFromDB(CustomProv.ENABLED);
        populateCbCustomers();
        tableQuotes.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                //onSeeQuote();
            }
        });
    }
    
    @FXML protected void onCreateQuote(){
        FXMLLoader loader=new FXMLLoader();
        Parent newQuoteView=null;
        BorderPane home=null;
        
        try {
            newQuoteView = FXMLLoader.load(getClass().getResource("viewNewQuote.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(ViewQuotesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        home=(BorderPane)newQuoteView.getParent();
        home.setCenter(newQuoteView);
    }
    
    /*@FXML protected void onSeeQuote(){
        FXMLLoader loader=new FXMLLoader();
        Parent quoteView=null;
        ViewQuoteController controller=null;
        BorderPane home=null;
        
        loader.setLocation(getClass().getResource("viewQuote.fxml"));
        try {
            quoteView=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewQuotesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(tableQuotes.getSelectionModel().getSelectedItem());
        home=(BorderPane)paneQuotes.getParent();
        home.setCenter(quoteView);
    }*/
    
    @FXML protected void onClicPending(){
        quotes=FXCollections.observableArrayList(Quotes.getAllQuotesFromDB(Quotes.PENDING));
        createTableQuotes();
    }
    
    @FXML protected void onClicAccepted(){
        quotes=FXCollections.observableArrayList(Quotes.getAllQuotesFromDB(Quotes.ACCEPTED));
        createTableQuotes();
    }
    
    @FXML protected void onClicRejected(){
        quotes=FXCollections.observableArrayList(Quotes.getAllQuotesFromDB(Quotes.REJECTED));
        createTableQuotes();
    }
    
    @FXML protected void onClicAll(){
        quotes=FXCollections.observableArrayList(Quotes.getAllQuotesFromDB());
        createTableQuotes();
    }
    
    @FXML protected void getSelectionCBCustomers(){
        customer=cbCustomers.getSelectionModel().getSelectedItem();
        customer.getQuotesFromDB();
        quotes=FXCollections.observableArrayList(customer.getQuotes());
        createTableQuotes();
    }
    
    private void createTableQuotes(){
        for(int i=0;i<quotes.size();i++){
            quotes.get(i).setComName();
            quotes.get(i).setTotalString();
        }
        columnComName.setCellValueFactory(new PropertyValueFactory<>("comName"));
        columnNbr.setCellValueFactory(new PropertyValueFactory<>("docNumber"));
        columnDate.setCellValueFactory(new PropertyValueFactory<>("docDate"));
        columnTotal.setCellValueFactory(new PropertyValueFactory<>("totalString"));
                
        tableQuotes.setItems(quotes);
    }
    
    private void populateCbCustomers(){
        ObservableList<Customer> list =FXCollections.observableArrayList(companies);
        cbCustomers.setItems(list);
        
        cbCustomers.setCellFactory(new Callback<ListView<Customer>, ListCell<Customer>>() {
            @Override
            public ListCell<Customer> call(ListView<Customer> p) {
                return new ListCell<Customer>() {
                    @Override
                    protected void updateItem(Customer item, boolean empty) {
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
