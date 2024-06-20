/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.Customer;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class ViewCustomersController implements Initializable {
    
    @FXML private TableView<Customer> tableCustomers;
    @FXML private TableColumn<Customer, String> columnComName, columnVATNbr, columnEmail, ColumnWeb;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        ObservableList<Customer> customers =FXCollections.observableArrayList(Customer.getAllCustomersFromDB());
        
        columnComName.setCellValueFactory(new PropertyValueFactory<Customer, String>("comName"));
        columnVATNbr.setCellValueFactory(new PropertyValueFactory<Customer, String>("vatNumber"));;
        columnEmail.setCellValueFactory(new PropertyValueFactory<Customer, String>("email"));;
        ColumnWeb.setCellValueFactory(new PropertyValueFactory<Customer, String>("web"));;
                
        tableCustomers.setItems(customers);

    }    
    
}
