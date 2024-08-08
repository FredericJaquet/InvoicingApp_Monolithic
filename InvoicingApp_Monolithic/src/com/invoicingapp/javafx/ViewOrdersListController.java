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
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewOrdersListController implements Initializable {

    private Orders order;
    private ViewNewInvoiceCustomerController mainInvoiceCustomerController;
    private ViewNewInvoiceProviderController mainInvoiceProviderController;
    private ViewNewQuoteController mainQuoteController;
    private ViewNewPOController mainPOController;
    
    @FXML Label lbDescription,lbDate,lbTotal;
    @FXML CheckBox cbSelected;
    @FXML HBox hbOrders;
    
    public void initData(Orders order,ViewNewInvoiceCustomerController mainController){
        this.order=order;
        lbDescription.setText(order.getDescription());
        lbDate.setText(order.getDateOrder().toString());
        lbTotal.setText(String.format("%.2f€",order.getTotalOrder()));
        mainInvoiceCustomerController = mainController;
        cbSelected.setSelected(order.isSelected());
    }
    
    public void initData(Orders order,ViewNewQuoteController mainController){
        this.order=order;
        lbDescription.setText(order.getDescription());
        lbDate.setText(order.getDateOrder().toString());
        lbTotal.setText(String.format("%.2f€",order.getTotalOrder()));
        mainQuoteController = mainController;
        cbSelected.setSelected(order.isSelected());
    }
    
    public void initData(Orders order,ViewNewInvoiceProviderController mainController){
        this.order=order;
        lbDescription.setText(order.getDescription());
        lbDate.setText(order.getDateOrder().toString());
        lbTotal.setText(String.format("%.2f€",order.getTotalOrder()));
        mainInvoiceProviderController = mainController;
        cbSelected.setSelected(order.isSelected());
    }
    
    public void initData(Orders order,ViewNewPOController mainController){
        this.order=order;
        lbDescription.setText(order.getDescription());
        lbDate.setText(order.getDateOrder().toString());
        lbTotal.setText(String.format("%.2f€",order.getTotalOrder()));
        mainPOController = mainController;
        cbSelected.setSelected(order.isSelected());
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    @FXML protected void getSelection(){
        order.setSelected(cbSelected.isSelected());
        if(mainInvoiceCustomerController!=null){
            mainInvoiceCustomerController.updateTotals();
        }else if(mainQuoteController!=null){
            mainQuoteController.updateTotals();
        }else if(mainInvoiceProviderController!=null){
            mainInvoiceProviderController.updateTotals();
        }else if(mainPOController!=null){
            mainPOController.updateTotals();
        }
    }
    
    public void setCurrency(String currency){
        lbTotal.setText(String.format("%.2f"+currency,order.getTotalOrder()));
    }
    
}
