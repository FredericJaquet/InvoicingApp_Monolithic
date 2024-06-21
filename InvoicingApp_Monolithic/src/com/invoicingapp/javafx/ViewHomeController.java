/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewHomeController implements Initializable {

    private int imgSize=100;
    
    @FXML private VBox PaneCenterHome;
    @FXML private BorderPane mainPane;
    @FXML private Button btnCustomers,btnProviders,btnCreateOrder,
            btnCreateInvoiceCustomer,btnCreateInvoiceProvider,btnCreateQuote, 
            btnCreatePo,btnReportIncomes,btnReportOutcomes,
            btnReportPendings,btnGrafIncomes,btnGrafOutcomes;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setImageButton(btnCustomers,"../img/customers.png");
        setImageButton(btnProviders,"../img/providers.png");
        setImageButton(btnCreateOrder,"../img/orders.png");
        setImageButton(btnCreateInvoiceCustomer,"../img/invoiceCustomers.png");
        setImageButton(btnCreateInvoiceProvider,"../img/invoiceProviders.png");
        setImageButton(btnCreateQuote,"../img/quotes.png");
        setImageButton(btnCreatePo,"../img/po.png");
        setImageButton(btnReportIncomes,"../img/reportIncomes.png");
        setImageButton(btnReportOutcomes,"../img/reportOutcomes.png");
        setImageButton(btnReportPendings,"../img/reportPendings.png");
        setImageButton(btnGrafIncomes,"../img/grafIncomes.png");
        setImageButton(btnGrafOutcomes,"../img/grafOutcomes.png");
    }

    @FXML protected void onFocus(MouseEvent e){
        Button button =(Button) e.getSource();
        
        button.getStyleClass().clear();
        button.getStyleClass().add("btnFocused");
    }
    
    @FXML protected void onExit(MouseEvent e){
        Button button =(Button) e.getSource();
        
        button.getStyleClass().clear();
        button.getStyleClass().add("btnMenu");
    }
    
    @FXML protected void onHome(){
        mainPane.setCenter(PaneCenterHome);
    }
    
    @FXML protected void onItemCentralMenu(ActionEvent e){
        Button button=(Button) e.getSource();
        String idBtn=button.getId().substring(3);
        String window="view"+idBtn+".fxml";
        Parent customersView;
        try {
            customersView=FXMLLoader.load(getClass().getResource(window));
            mainPane.setCenter(customersView);
        } catch (IOException ex) {
            Logger.getLogger(ViewHomeController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    @FXML protected void onItemLatMenu(ActionEvent e){
        Button button=(Button) e.getSource();
        String idBtn=button.getId().substring(1);
        String window="View"+idBtn+".fxml";
        Parent customersView;
        try {
            customersView=FXMLLoader.load(getClass().getResource(window));
            mainPane.setCenter(customersView);
        } catch (IOException ex) {
            Logger.getLogger(ViewHomeController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    private void setImageButton(Button button, String path){
        InputStream inputStream=getClass().getResourceAsStream(path);
        Image img=new Image(inputStream, imgSize, imgSize, true, true);
        button.setGraphic(new ImageView(img));
    }
    
}
