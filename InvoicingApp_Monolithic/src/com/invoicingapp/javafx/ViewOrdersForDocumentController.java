/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.ChangeRate;
import invoicingapp_monolithic.Orders;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewOrdersForDocumentController implements Initializable {

    private String currency;
    private Orders order;
    private ChangeRate changeRate;
    
    @FXML private VBox paneItems;
    @FXML private Label lbDescription,lbDate,lbQuantity,lbTotal;
    
    public void initData(Orders order, ChangeRate changeRate){
        this.order=order;
        this.changeRate=changeRate;
        setData();
        setItems();
    }
    
    public void initData(Orders order, String currency){
        this.order=order;
        this.currency=currency;
        setData();
        setItems();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    private void setItems(){
        FXMLLoader loader;
        Parent itemsListView=null;
        ViewItemsForDocumentController controller=null;
        
        paneItems.getChildren().clear();
        for (int i=0;i<order.getItems().size();i++) {
            loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("viewItemsForDocument.fxml"));
            try {
                itemsListView=loader.load();
            } catch (IOException ex) {
                Logger.getLogger(ViewOrdersForDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
            controller=loader.getController();
            controller.initData(order.getItems().get(i),changeRate,order.getPricePerUnit());
            paneItems.getChildren().add(itemsListView);
        }
    }
    
    private void setData(){
        lbDescription.setText(order.getDescription());
        lbDate.setText(order.getDateOrder().toString());
        lbQuantity.setText(String.format("%.2f",order.getQuantity()));
        if(changeRate!=null){
            lbTotal.setText(String.format("%.2f"+changeRate.getCurrency1(),order.getTotalOrder()));
        }else{
            lbTotal.setText(String.format("%.2f"+currency,order.getTotalOrder()));
        }
    }
    
    protected int getLinesNumber(){
        return order.getItems().size()+1;
    }
}
