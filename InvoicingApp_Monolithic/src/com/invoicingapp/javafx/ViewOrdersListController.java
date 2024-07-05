/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.Orders;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewOrdersListController implements Initializable {

    private Orders order;
    
    @FXML Label lbDescription,lbDate,lbTotal;
    @FXML CheckBox cbSelected;
    
    public void initData(Orders order){
        this.order=order;
        lbDescription.setText(order.getDescription());
        lbDate.setText(order.getDateOrder().toString());
        lbTotal.setText(String.format("%.2f€",order.getTotalOrder()));
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML protected void getSelection(){
        order.setSelected(cbSelected.isSelected());
    }
    
}
