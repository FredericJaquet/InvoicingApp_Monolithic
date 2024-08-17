/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.Provider;
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

public class ViewProvidersController implements Initializable {

    private ObservableList<Provider> providers;
    
    @FXML private TableView<Provider> tableProviders;
    @FXML private TableColumn<Provider, String> columnComName, columnVATNbr, columnEmail, ColumnWeb;
    @FXML private VBox paneProviders;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        onClicEnabled();
        tableProviders.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                onSeeDetails();
            }
        });
    }
    
    @FXML protected void onCreateProvider(){
        Parent root=null;
        Scene scene=null;
        Stage stage=new Stage();
            
        try {
            root = FXMLLoader.load(getClass().getResource("viewCreateProvider.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(ViewProvidersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        scene=new Scene(root);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("../img/Icon.png"))); 
        stage.setScene(scene);
        
        stage.setOnHiding(event -> {
                paneProviders.getParent().setDisable(false);
                createTableProviders();
            });
        
        stage.show();
        paneProviders.getParent().setDisable(true);
    }
    
    @FXML protected void onSeeDetails(){
        FXMLLoader loader=new FXMLLoader();
        Parent detailsProviderView=null;
        ViewDetailsProviderController controller=null;
        BorderPane home=null;
        
        loader.setLocation(getClass().getResource("viewDetailsProvider.fxml"));
        try {
            detailsProviderView=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewProvidersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(tableProviders.getSelectionModel().getSelectedItem());
        home=(BorderPane)paneProviders.getParent();
        home.setCenter(detailsProviderView);
    }
    
    @FXML protected void onClicEnabled(){
        providers=FXCollections.observableArrayList(Provider.getAllProvidersFromDB(Provider.ENABLED));
        createTableProviders();
    }
    
    @FXML protected void onClicDisabled(){
        providers=FXCollections.observableArrayList(Provider.getAllProvidersFromDB(Provider.DISABLED));
        createTableProviders();
    }
    
    @FXML protected void onClicAll(){
        providers=FXCollections.observableArrayList(Provider.getAllProvidersFromDB());
        createTableProviders();
    }
    
    private void createTableProviders(){        
        columnComName.setCellValueFactory(new PropertyValueFactory<Provider, String>("comName"));
        columnVATNbr.setCellValueFactory(new PropertyValueFactory<Provider, String>("vatNumber"));;
        columnEmail.setCellValueFactory(new PropertyValueFactory<Provider, String>("email"));;
        ColumnWeb.setCellValueFactory(new PropertyValueFactory<Provider, String>("web"));;
                
        tableProviders.setItems(providers);
    }
    
}
