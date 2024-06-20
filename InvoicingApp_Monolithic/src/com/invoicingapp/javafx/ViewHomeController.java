/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewHomeController implements Initializable {

    private int imgSize=80;
    
    @FXML private GridPane paneCentral;
    @FXML private Button btnCreateCustomer,btnCreateProvider,btnCreateOrder,
            btnCreateInvoiceCustomer,btnCreateInvoiceProvider,btnCreateQuote, 
            btnCreatePo,btnReportIncomes,btnReportOutcomes,
            btnReportPendings,btnGrafIncomes,btnGrafOutcomes;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        paneCentral.setAlignment(Pos.CENTER);
        InputStream inputStream;
        Image img;
        
        inputStream=getClass().getResourceAsStream("../img/customers.png");
        img=new Image(inputStream, imgSize, imgSize, true, true);
        btnCreateCustomer.setGraphic(new ImageView(img));
        
        inputStream=getClass().getResourceAsStream("../img/providers.png");
        img=new Image(inputStream, imgSize, imgSize, true, true);        
        btnCreateProvider.setGraphic(new ImageView(img));
        
        inputStream=getClass().getResourceAsStream("../img/orders.png");
        img=new Image(inputStream, imgSize, imgSize, true, true);
        btnCreateOrder.setGraphic(new ImageView(img));
        
        inputStream=getClass().getResourceAsStream("../img/invoiceCustomers.png");
        img=new Image(inputStream, imgSize, imgSize, true, true);
        btnCreateInvoiceCustomer.setGraphic(new ImageView(img));
        
        inputStream=getClass().getResourceAsStream("../img/invoiceProviders.png");
        img=new Image(inputStream, imgSize, imgSize, true, true);
        btnCreateInvoiceProvider.setGraphic(new ImageView(img));
        
        inputStream=getClass().getResourceAsStream("../img/quotes.png");
        img=new Image(inputStream, imgSize, imgSize, true, true);
        btnCreateQuote.setGraphic(new ImageView(img));
        
        inputStream=getClass().getResourceAsStream("../img/po.png");
        img=new Image(inputStream, imgSize, imgSize, true, true);
        btnCreatePo.setGraphic(new ImageView(img));
        
        inputStream=getClass().getResourceAsStream("../img/reportIncomes.png");
        img=new Image(inputStream, imgSize, imgSize, true, true);
        btnReportIncomes.setGraphic(new ImageView(img));
        
        inputStream=getClass().getResourceAsStream("../img/reportOutcomes.png");
        img=new Image(inputStream, imgSize, imgSize, true, true);
        btnReportOutcomes.setGraphic(new ImageView(img));
        
        inputStream=getClass().getResourceAsStream("../img/reportPendings.png");
        img=new Image(inputStream, imgSize, imgSize, true, true);
        btnReportPendings.setGraphic(new ImageView(img));
        
        inputStream=getClass().getResourceAsStream("../img/grafIncomes.png");
        img=new Image(inputStream, imgSize, imgSize, true, true);
        btnGrafIncomes.setGraphic(new ImageView(img));
        
        inputStream=getClass().getResourceAsStream("../img/grafOutcomes.png");
        img=new Image(inputStream, imgSize, imgSize, true, true);
        btnGrafOutcomes.setGraphic(new ImageView(img));
        
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
    
}
