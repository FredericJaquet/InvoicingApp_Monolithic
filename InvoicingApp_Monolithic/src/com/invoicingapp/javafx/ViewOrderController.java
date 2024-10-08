/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.bbdd.ConnectionDB;
import com.invoicingapp.tools.DoubleStringConverter;
import com.invoicingapp.tools.LabelFeatures;
import com.invoicingapp.tools.Validations;
import invoicingapp_monolithic.CustomProv;
import invoicingapp_monolithic.Item;
import invoicingapp_monolithic.Orders;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewOrderController implements Initializable {
    private ArrayList<String> queries=new ArrayList();
    private Orders order=new Orders();
    private CustomProv company;
    private LabelFeatures lbFeatures=new LabelFeatures();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private double updatedTotal=0;
    private boolean control=true;
    private boolean changes=false;
    private final String errorFormat="Uno de los datos introducidos no es correcto.";
    private final String errorEmpty="Falta un dato obligatorio.";
    private final String errorDateOrder="Falta la Fecha.";
    private final String errorNoLines="El pedido no tiene artículos.";
    
    @FXML TextField tfDescription,tfPrice,tfUnits,tfFieldName,tfSourceLanguage,tfTargetLanguage;
    @FXML Label lbDescription,lbDate,lbPrice,lbUnits,lbFieldName,lbSourceLanguage,lbTargetLanguage;
    @FXML Label lbLegalName,lbVATNumber,lbUpdatedTotal,lbError;
    @FXML DatePicker dpDateOrder;
    @FXML TableView<Item> tvItems;
    @FXML TableColumn<Item,String> columnDescription;
    @FXML TableColumn<Item,Double> columnDiscount,columnQuantity;
    @FXML private GridPane paneOrder;
    
    
    public void initData(Orders order){
        this.order=order;
        company=order.getCompany();
        setData();
        createTableItems();
        lbFeatures.makeLabelEditable(lbDescription, tfDescription,"Orders","descrip",order.getIdOrders());
        lbFeatures.makeLabelEditable(lbDate,dpDateOrder,"Orders","dateOrder",order.getIdOrders());
        lbFeatures.makeLabelEditable(lbPrice, tfPrice,"Orders","pricePerUnit",order.getIdOrders());
        lbFeatures.makeLabelEditable(lbUnits, tfUnits,"Orders","units",order.getIdOrders());
        lbFeatures.makeLabelEditable(lbFieldName, tfFieldName,"Orders","fieldName",order.getIdOrders());
        lbFeatures.makeLabelEditable(lbSourceLanguage, tfSourceLanguage,"Orders","sourceLanguage",order.getIdOrders());
        lbFeatures.makeLabelEditable(lbTargetLanguage, tfTargetLanguage,"Orders","targetLanguage",order.getIdOrders());
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lbUpdatedTotal.setText(String.valueOf(0.00)+"€");
        makeTableViewEditable();
        tfPrice.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                updateTotalOrder();
            }
        });
    } 
    
    @FXML protected void onClicCancel(){
        queries.addAll(lbFeatures.getQuery());
        if(!queries.isEmpty()){
            ConfirmationDialog.show("Hay cambios sin guardar. ¿Está seguro de querer volver sin guardar los cambios?", this::closeWindow, () -> {});
        }else{
            closeWindow();
        }
    }
    
    @FXML protected void onClicSave(){
        queries.addAll(lbFeatures.getQuery());
        if(!queries.isEmpty()){
            saveData();
        }
    }
    
    @FXML protected void onClicDelete(){
        ConfirmationDialog.show("¿Está seguro de querer eliminar este pedido?", this::deleteOrder, () -> {});
    }
    
    private void setData(){
        lbLegalName.setText(company.getLegalName());
        lbVATNumber.setText(company.getVatNumber());
        lbDescription.setText(order.getDescription());
        lbDate.setText(order.getDateOrder().format(formatter));
        lbPrice.setText(String.format("%.2f€",order.getPricePerUnit()));
        lbUnits.setText(order.getUnits());
        lbFieldName.setText(order.getFieldName());
        lbSourceLanguage.setText(order.getSourceLanguage());
        lbTargetLanguage.setText(order.getTargetLanguage());
        setTotalOrder();
    }
    
    private void setTotalOrder(){
        for(int i=0;i<order.getItems().size();i++){
            updatedTotal=updatedTotal+order.getPricePerUnit()*order.getItems().get(i).getQuantity()*(100-order.getItems().get(i).getDiscount())/100;
        }
        lbUpdatedTotal.setText(String.format("%.2f€", updatedTotal));
    }
    
    private void updateTotalOrder(){
        double price=0;
        boolean controlTotal=true;
        updatedTotal=0;
        tfPrice.getStyleClass().remove("error");
        
        try{
            price=Double.parseDouble(lbPrice.getText().replace(",",".").replace("€",""));
        }catch(NumberFormatException ex){
            lbPrice.getStyleClass().add("error");
            controlTotal=false;
        }
        if(controlTotal){
            for(int i=0;i<order.getItems().size();i++){
                updatedTotal=updatedTotal+price*order.getItems().get(i).getQuantity()*(100-order.getItems().get(i).getDiscount())/100;
            }
            lbUpdatedTotal.setText(String.format("%.2f€", updatedTotal));
        }
    }
    
    private void makeTableViewEditable(){
        lbError.setVisible(false);
        lbError.setText(errorFormat);
        
        tvItems.setEditable(true);
        columnDescription.setCellFactory(TextFieldTableCell.forTableColumn());
        columnDiscount.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        columnQuantity.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        columnDescription.setOnEditCommit(event -> {
            Item item = event.getRowValue();
            item.setDescription(event.getNewValue());
            queries.add("UPDATE Item SET descrip='"+event.getNewValue()+"' WHERE idItem="+item.getIdItem());
        });

        columnDiscount.setOnEditCommit((var event) -> {
            Item item = event.getRowValue();
            try {
                double discount = event.getNewValue();
                item.setDiscount(discount);
                queries.add("UPDATE Item SET discount="+discount+" WHERE idItem="+item.getIdItem());
            } catch (NumberFormatException ex) {
                lbError.setVisible(true);
            }
        });

        columnQuantity.setOnEditCommit(event -> {
            Item item = event.getRowValue();
            try {
                double quantity = event.getNewValue();
                item.setQuantity(quantity);
                updateTotalOrder();
                queries.add("UPDATE Item SET qty="+quantity+" WHERE idItem="+item.getIdItem());
            } catch (NumberFormatException ex) {
                lbError.setVisible(true);
            }
        });
    }
    
    private void createTableItems(){
        ObservableList<Item> items=FXCollections.observableArrayList(order.getItems());
        
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        columnQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
                
        tvItems.setItems(items);
    }
    
    private void saveData(){        
        String price=lbPrice.getText();
        control=true;
        lbError.setVisible(false);
        tfDescription.getStyleClass().remove("error");
        tfPrice.getStyleClass().remove("error");
        dpDateOrder.getStyleClass().remove("error");
        
        if(!Validations.isNotEmpty(lbDescription, lbError, errorEmpty)){
            control=false;
        }
        if(control&&(!Validations.isNotEmpty(lbPrice, lbError, errorEmpty))){
            control=false;
        }
        lbPrice.setText(price.replace(",", ".").replace("€",""));
        if(control&&(!Validations.isDouble(lbPrice, lbError, errorFormat))){
            control=false;
        }
        lbPrice.setText(price);
        if(control&&(!Validations.isNotEmpty(lbDate,lbError,errorDateOrder))){
            control=false;
        }
        if(control&&order.getItems().isEmpty()){
            control=false;
            lbError.setText(errorNoLines);
            lbError.setVisible(true);
        }
        if(control){
            ConnectionDB con=new ConnectionDB();
            
            con.openConnection();
            for(int i=0;i<queries.size();i++){
                con.executeUpdate(queries.get(i));
            }
            con.closeConnection();
            queries.clear();
        }
    }
    
    private void deleteOrder(){
        order.deleteFromDB();
        closeWindow();
    }
    
    private void closeWindow(){
        BorderPane home=(BorderPane)paneOrder.getParent();
        Parent ordersView;
        try {
            ordersView=FXMLLoader.load(getClass().getResource("viewOrders.fxml"));
            home.setCenter(ordersView);
        } catch (IOException ex) {
            Logger.getLogger(ViewOrderController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
