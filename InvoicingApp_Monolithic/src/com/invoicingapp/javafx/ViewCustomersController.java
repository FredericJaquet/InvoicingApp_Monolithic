/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.config.PathNames;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class ViewCustomersController implements Initializable {
    
    private ObservableList<Customer> customers;
    
    @FXML private TableView<Customer> tableCustomers;
    @FXML private TableColumn<Customer, String> columnComName, columnVATNbr, columnEmail, ColumnWeb;
    @FXML private VBox paneCustomers;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        onClicEnabled();
        tableCustomers.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                onSeeDetails();
            }
        });
    }
    
    @FXML protected void onCreateCustomer(){
        Parent root=null;
        Scene scene=null;
        Stage stage=new Stage();
        
        try {
            root = FXMLLoader.load(getClass().getResource("viewCreateCustomer.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(ViewCustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        scene=new Scene(root);
        stage.getIcons().add(new Image(getClass().getResourceAsStream(PathNames.ICON))); 
        stage.setScene(scene);
        
        stage.setOnHiding(event -> {
                paneCustomers.getParent().setDisable(false);
                createTableCustomers();
            });
        
        stage.show();
        paneCustomers.getParent().setDisable(true);
    }
    
    @FXML protected void onSeeDetails(){
        if(tableCustomers.getSelectionModel().getSelectedItem()!=null){
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
            controller.initData(tableCustomers.getSelectionModel().getSelectedItem());
            home=(BorderPane)paneCustomers.getParent();
            home.setCenter(detailsCustomerView);
        }
    }
    
    @FXML protected void onClicEnabled(){
        customers=FXCollections.observableArrayList(Customer.getAllCustomersFromDB(Customer.ENABLED));
        createTableCustomers();
    }
    
    @FXML protected void onClicDisabled(){
        customers=FXCollections.observableArrayList(Customer.getAllCustomersFromDB(Customer.DISABLED));
        createTableCustomers();
    }
    
    @FXML protected void onClicAll(){
        customers=FXCollections.observableArrayList(Customer.getAllCustomersFromDB());
        createTableCustomers();
    }
    
    private void createTableCustomers(){
        columnComName.setCellValueFactory(new PropertyValueFactory<Customer, String>("comName"));
        columnVATNbr.setCellValueFactory(new PropertyValueFactory<Customer, String>("vatNumber"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<Customer, String>("email"));
        ColumnWeb.setCellValueFactory(new PropertyValueFactory<Customer, String>("web"));
                
        tableCustomers.setItems(customers);
    }
    
}
