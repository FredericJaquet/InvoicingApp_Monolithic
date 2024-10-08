/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.tools.ButtonFeatures;
import invoicingapp_monolithic.Customer;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewHomeController implements Initializable {

    private final int imgSize=100;
    
    @FXML private GridPane paneCentral;
    @FXML private BorderPane mainPane;
    @FXML private Button btnCustomers,btnProviders,btnOrders,
            btnInvoicesCustomer,btnInvoicesProvider,btnQuotes, 
            btnPOs,btnReportIncomes,btnReportOutcomes,btnHome,
            btnReportPending,btnGrafIncomes,btnGrafOutcomes;
    @FXML TextField fieldCompany;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ButtonFeatures.makeButtonFocusable(btnCustomers,"Clientes","/com/invoicingapp/img/Customers.png","/com/invoicingapp/img/Customers_Focused.png");
        ButtonFeatures.makeButtonFocusable(btnProviders,"Proveedores","/com/invoicingapp/img/Providers.png","/com/invoicingapp/img/Providers_Focused.png");
        ButtonFeatures.makeButtonFocusable(btnOrders,"Pedidos","/com/invoicingapp/img/NewOrder.png","/com/invoicingapp/img/NewOrder_Focused.png");
        ButtonFeatures.makeButtonFocusable(btnInvoicesCustomer,"Facturas Clientes","/com/invoicingapp/img/NewInvoiceCustomer.png","/com/invoicingapp/img/NewInvoiceCustomer_Focused.png");
        ButtonFeatures.makeButtonFocusable(btnInvoicesProvider,"Facturas Proveedores","/com/invoicingapp/img/NewInvoiceProvider.png","/com/invoicingapp/img/NewInvoiceProvider_Focused.png");
        ButtonFeatures.makeButtonFocusable(btnQuotes,"Presupuesto","/com/invoicingapp/img/NewQuote.png","/com/invoicingapp/img/NewQuote_Focused.png");
        ButtonFeatures.makeButtonFocusable(btnPOs,"Orden de Pedido","/com/invoicingapp/img/NewPo.png","/com/invoicingapp/img/NewPo_Focused.png");
        ButtonFeatures.makeButtonFocusable(btnReportIncomes,"Informe Ingresos","/com/invoicingapp/img/ReportIncomes.png","/com/invoicingapp/img/ReportIncomes_Focused.png");
        ButtonFeatures.makeButtonFocusable(btnReportOutcomes,"Informe Gastos","/com/invoicingapp/img/ReportOutcomes.png","/com/invoicingapp/img/ReportOutcomes_Focused.png");
        ButtonFeatures.makeButtonFocusable(btnReportPending,"Informes Pagos Pendientes","/com/invoicingapp/img/ReportPendings.png","/com/invoicingapp/img/ReportPendings_Focused.png");
        ButtonFeatures.makeButtonFocusable(btnGrafIncomes,"Gráfico Ingresos","/com/invoicingapp/img/GrafIncomes.png","/com/invoicingapp/img/GrafIncomes_Focused.png");
        ButtonFeatures.makeButtonFocusable(btnGrafOutcomes,"Gráfico Gastos","/com/invoicingapp/img/GrafOutcomes.png","/com/invoicingapp/img/GrafOutcomes_Focused.png");
    }
    
    @FXML protected void onFocus(MouseEvent e){
        Button button =(Button) e.getSource();
        
        button.getStyleClass().clear();
        button.getStyleClass().add("btnFocused");
    }
    
    @FXML protected void onFocusHome(){
        btnHome.getStyleClass().clear();
        btnHome.getStyleClass().add("btnFocused");
        btnHome.getStyleClass().add("labelMenu");
    }
    
    @FXML protected void onExitHome(){
        btnHome.getStyleClass().clear();
        btnHome.getStyleClass().add("btnMenu");
        btnHome.getStyleClass().add("labelMenu");
    }
    
    @FXML protected void onExit(MouseEvent e){
        Button button =(Button) e.getSource();
        
        button.getStyleClass().clear();
        button.getStyleClass().add("btnMenu");
    }
    
    @FXML protected void onHome(){
        mainPane.setCenter(paneCentral);
    }
    
    @FXML protected void onItemCentralMenu(ActionEvent e){
        Button button=(Button) e.getSource();
        String idBtn=button.getId().substring(3);
        String window="view"+idBtn+".fxml";
        Parent view;
        try {
            view=FXMLLoader.load(getClass().getResource(window));
            mainPane.setCenter(view);
        } catch (IOException ex) {
            Logger.getLogger(ViewHomeController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    @FXML protected void onItemLatMenu(ActionEvent e){
        Button button=(Button) e.getSource();
        String idBtn=button.getId().substring(1);
        String window="view"+idBtn+".fxml";
        Parent customersView;
        try {
            customersView=FXMLLoader.load(getClass().getResource(window));
            mainPane.setCenter(customersView);
        } catch (IOException ex) {
            Logger.getLogger(ViewHomeController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    @FXML protected void onPressEnter(KeyEvent e){
        if(e.getCode()== KeyCode.ENTER){
            openViewDetailsCustomer(fieldCompany.getText());
        }
    }
    
    private void openViewDetailsCustomer(String input){
        ArrayList<Customer> customers=Customer.getAllCustomersFromDB(Customer.ENABLED);
        Customer customer=null;
        FXMLLoader loader=new FXMLLoader();
        Parent detailsCustomerView=null;
        ViewDetailsCustomerController controller=null;
        
        loader.setLocation(getClass().getResource("viewDetailsCustomer.fxml"));
        try {
            detailsCustomerView=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewCustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        for(int i=0;i<customers.size();i++){
            if(customers.get(i).getComName().toLowerCase().contains(input.toLowerCase())){
                customer=customers.get(i);
            }
        }
        controller.initData(customer);
        mainPane.setCenter(detailsCustomerView);
    }
    
}
