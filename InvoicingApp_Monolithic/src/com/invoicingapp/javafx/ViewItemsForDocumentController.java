/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.ChangeRate;
import invoicingapp_monolithic.Item;
import invoicingapp_monolithic.Orders;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable; 
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewItemsForDocumentController implements Initializable {

    private Item item;
    private ChangeRate changeRate;
    private double price;
    private String currency;

    @FXML Label lbDescription,lbQuantity,lbPrice,lbDiscount,lbTotal;
    
    public void initData(Item item,ChangeRate changeRate,double price){
        this.changeRate=changeRate;
        this.item=item;
        this.price=price;
        setData();
    }
    
    public void initData(Item item, String currency,double price){
        this.item=item;
        this.currency=currency;
        this.price=price;
        setData();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    private void setData(){
        double total=price*item.getQuantity()*(100-item.getDiscount())/100;
        
        lbDescription.setText(item.getDescription());
        lbQuantity.setText(String.format("%.2f",item.getQuantity()));
        lbDiscount.setText(String.format("%.2f%%",item.getDiscount()));
        if(changeRate!=null){
            lbPrice.setText(String.format("%.4f"+changeRate.getCurrency1(),price));
            lbTotal.setText(String.format("%.2f"+changeRate.getCurrency1(),total));
        }else{
            lbPrice.setText(String.format("%.4f"+currency,price));
            lbTotal.setText(String.format("%.2f"+currency,total));
        }
    }
    
}
