/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.CustomProv;
import invoicingapp_monolithic.Orders;
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

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewOrdersController implements Initializable {

    private ObservableList<Orders> orders;
    private ArrayList<CustomProv> companies=new ArrayList();
    private CustomProv company;
    
    @FXML private TableView<Orders> tableOrders;
    @FXML private TableColumn<Orders, String> columnComName, columnNbr,columnTotal;
    @FXML private TableColumn<Orders, LocalDate> columnDate;
    @FXML private ComboBox<CustomProv> cbCompanies;
    @FXML private VBox paneOrders;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        onClicAll();
        companies=CustomProv.getAllCustomProvFromDB(CustomProv.ENABLED);
        populateCbCompanies();
        tableOrders.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                onSeeOrder();
            }
        });
    }
    
    @FXML protected void onCreateOrder(){
        FXMLLoader loader=new FXMLLoader();
        Parent newOrderView=null;
        ViewNewOrderController controller=null;
        BorderPane home=(BorderPane)paneOrders.getParent();
        
        loader.setLocation(getClass().getResource("viewNewOrder.fxml"));
        try {
            newOrderView=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewOrdersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(3);
        home.setCenter(newOrderView);
    }
    
    @FXML protected void onSeeOrder(){
        FXMLLoader loader=new FXMLLoader();
        Parent orderView=null;
        ViewOrderController controller=null;
        BorderPane home=null;
        
        loader.setLocation(getClass().getResource("viewOrder.fxml"));
        try {
            orderView=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewOrdersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(tableOrders.getSelectionModel().getSelectedItem());
        home=(BorderPane)paneOrders.getParent();
        home.setCenter(orderView);
    }
    
    @FXML protected void onClicCustomers(){
        orders=FXCollections.observableArrayList(Orders.getAllOrdersFromDB(Orders.CUSTOMERS));
        createTableOrders();
    }
    
    @FXML protected void onClicProviders(){
        orders=FXCollections.observableArrayList(Orders.getAllOrdersFromDB(Orders.PROVIDERS));
        createTableOrders();
    }
    
    @FXML protected void onClicAll(){
        orders=FXCollections.observableArrayList(Orders.getAllOrdersFromDB());
        createTableOrders();
    }
    
    @FXML protected void getSelectionCBCompanies(){
        company=cbCompanies.getSelectionModel().getSelectedItem();
        company.getOrdersFromDB();
        orders=FXCollections.observableArrayList(company.getOrders());
        createTableOrders();
    }
    
    private void createTableOrders(){
        columnComName.setCellValueFactory(new PropertyValueFactory<>("comName"));
        columnNbr.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnDate.setCellValueFactory(new PropertyValueFactory<>("dateOrder"));
        columnTotal.setCellValueFactory(new PropertyValueFactory<>("totalString"));
                
        tableOrders.setItems(orders);
    }
    
    private void populateCbCompanies(){
        ObservableList<CustomProv> list =FXCollections.observableArrayList(companies);
        cbCompanies.setItems(list);
        
        cbCompanies.setCellFactory((ListView<CustomProv> p) -> new ListCell<CustomProv>() {
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
        
        cbCompanies.setButtonCell(new ListCell<CustomProv>() {
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
}
