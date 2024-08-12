/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.CustomProv;
import invoicingapp_monolithic.Document;
import invoicingapp_monolithic.Provider;
import invoicingapp_monolithic.PurchaseOrder;
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
public class ViewPOsController implements Initializable {

    private ObservableList<PurchaseOrder> pos;
    private ArrayList<Provider> companies=new ArrayList();
    private Provider provider;
    
    @FXML private TableView<PurchaseOrder> tablePOs;
    @FXML private TableColumn<PurchaseOrder, String> columnComName, columnNbr,columnTotal;
    @FXML private TableColumn<PurchaseOrder, LocalDate> columnDate;
    @FXML private ComboBox<Provider> cbProviders;
    @FXML private VBox panePO;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        onClicAll();
        companies=Provider.getAllProvidersFromDB(CustomProv.ENABLED);
        populateCbCustomers();
        tablePOs.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                onSeePO();
            }
        });
    }
    
    @FXML protected void onCreatePO(){
        Parent newPOView=null;
        BorderPane home=null;
        
        try {
            newPOView = FXMLLoader.load(getClass().getResource("viewNewPO.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(ViewQuotesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        home=(BorderPane)panePO.getParent();
        home.setCenter(newPOView);
    }
    
    @FXML protected void onSeePO(){
        FXMLLoader loader=new FXMLLoader();
        Parent poView=null;
        ViewPOController controller=null;
        BorderPane home=null;
        
        loader.setLocation(getClass().getResource("viewPO.fxml"));
        try {
            poView=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewQuotesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(tablePOs.getSelectionModel().getSelectedItem());
        home=(BorderPane)panePO.getParent();
        home.setCenter(poView);
    }
    
    @FXML protected void onClicPending(){
        pos=FXCollections.observableArrayList(PurchaseOrder.getAllPOsFromDB(Document.PENDING));
        createTablePOs();
    }
    
    @FXML protected void onClicAccepted(){
        pos=FXCollections.observableArrayList(PurchaseOrder.getAllPOsFromDB(Document.ACCEPTED));
        createTablePOs();
    }
    
    @FXML protected void onClicRejected(){
        pos=FXCollections.observableArrayList(PurchaseOrder.getAllPOsFromDB(Document.REJECTED));
        createTablePOs();
    }
    
    @FXML protected void onClicAll(){
        pos=FXCollections.observableArrayList(PurchaseOrder.getAllPOsFromDB());
        createTablePOs();
    }
    
    @FXML protected void getSelectionCBCustomers(){
        provider=cbProviders.getSelectionModel().getSelectedItem();
        provider.getPOsFromDB();
        pos=FXCollections.observableArrayList(provider.getPos());
        createTablePOs();
    }
    
    private void createTablePOs(){
        for(int i=0;i<pos.size();i++){
            pos.get(i).setComName();
            pos.get(i).setTotalString();
        }
        columnComName.setCellValueFactory(new PropertyValueFactory<>("comName"));
        columnNbr.setCellValueFactory(new PropertyValueFactory<>("docNumber"));
        columnDate.setCellValueFactory(new PropertyValueFactory<>("docDate"));
        columnTotal.setCellValueFactory(new PropertyValueFactory<>("totalString"));
                
        tablePOs.setItems(pos);
    }
    
    private void populateCbCustomers(){
        ObservableList<Provider> list =FXCollections.observableArrayList(companies);
        cbProviders.setItems(list);
        
        cbProviders.setCellFactory((ListView<Provider> p) -> new ListCell<Provider>() {
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
