/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.Customer;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class ViewCustomersController implements Initializable {
    
    @FXML private TableView<Customer> tableCustomers;
    @FXML private TableColumn<Customer, String> columnComName, columnVATNbr, columnEmail, ColumnWeb;
    @FXML private VBox paneCustomers;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        createTableCustomers();
    }
    
    @FXML protected void onCreateCustomer(){
            Parent root=null;
        try {
            root = FXMLLoader.load(getClass().getResource("viewCreateCustomer.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(ViewLoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scene scene=new Scene(root);
        Stage stage=new Stage();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("../img/Icon.png"))); 
        stage.setScene(scene);
        
        stage.setOnHiding(event -> {
                paneCustomers.getParent().setDisable(false);
            });
        
        stage.show();
        paneCustomers.getParent().setDisable(true);
    }
    
    private void createTableCustomers(){
        ObservableList<Customer> customers=FXCollections.observableArrayList(Customer.getAllCustomersFromDB());
        
        columnComName.setCellValueFactory(new PropertyValueFactory<Customer, String>("comName"));
        columnVATNbr.setCellValueFactory(new PropertyValueFactory<Customer, String>("vatNumber"));;
        columnEmail.setCellValueFactory(new PropertyValueFactory<Customer, String>("email"));;
        ColumnWeb.setCellValueFactory(new PropertyValueFactory<Customer, String>("web"));;
                
        tableCustomers.setItems(customers);
    }
    
}
