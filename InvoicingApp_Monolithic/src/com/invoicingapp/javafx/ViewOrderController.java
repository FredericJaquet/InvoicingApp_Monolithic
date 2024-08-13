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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewOrderController implements Initializable {
    
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
        updateData();
        createTableItems();
        lbFeatures.makeLabelEditable(lbDescription, tfDescription,"Orders","description",order.getIdOrders());
        lbFeatures.makeLabelEditable(lbDate,dpDateOrder,"Orders","dateOrder",order.getIdOrders());
        lbFeatures.makeLabelEditable(lbPrice, tfPrice,"Orders","price",order.getIdOrders());
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
    } 
    
    @FXML protected void onClicCancel(){
        System.out.println("ViewOrder linea 91 Hola"+ changes);changes no cambia de estado
        if(changes){
            ConfirmationDialog.show("Hay cambios sin guardar. ¿Está seguro de querer volver sin guardar los cambios?", this::closeWindow, () -> {});
        }else{
            closeWindow();
        }
    }
    
    @FXML protected void onClicSave(){
        if(changes){
            saveData();
        }
    }
    
    @FXML protected void onClicDelete(){
        ConfirmationDialog.show("Hay cambios sin guardar. ¿Está seguro de querer eliminar este pedido?", this::deleteOrder, () -> {});
    }
    
    private void updateData(){
        lbLegalName.setText(company.getLegalName());
        lbVATNumber.setText(company.getVatNumber());
        
        lbDescription.setText(order.getDescription());
        lbDate.setText(order.getDateOrder().format(formatter));
        lbPrice.setText(String.format("%.2f€",order.getPricePerUnit()));
        lbUnits.setText(order.getUnits());
        lbFieldName.setText(order.getFieldName());
        lbSourceLanguage.setText(order.getSourceLanguage());
        lbTargetLanguage.setText(order.getTargetLanguage());
        updateTotalOrder();
    }
    
    private void updateTotalOrder(){
        double price=0;
        boolean control=true;
        updatedTotal=0;
        tfPrice.getStyleClass().remove("error");
        
        try{
            price=Double.parseDouble(tfPrice.getText());
        }catch(NumberFormatException ex){
            tfPrice.getStyleClass().add("error");
            control=false;
        }
        if(control){
            for(int i=0;i<order.getItems().size();i++){
                updatedTotal=updatedTotal+price*order.getItems().get(i).getQuantity()*(100-order.getItems().get(i).getDiscount())/100;
                lbUpdatedTotal.setText(String.format("%.2f€", updatedTotal));
            }
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
        });

        columnDiscount.setOnEditCommit(event -> {
            Item item = event.getRowValue();
            try {
                double discount = event.getNewValue();
                item.setDiscount(discount);
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
        control=true;
        lbError.setVisible(false);
        tfDescription.getStyleClass().remove("error");
        tfPrice.getStyleClass().remove("error");
        dpDateOrder.getStyleClass().remove("error");
        
        if(!Validations.isNotEmpty(tfDescription, lbError, errorEmpty)){
            control=false;
        }
        if(!Validations.isNotEmpty(tfPrice, lbError, errorEmpty)){
            control=false;
        }
        if(!Validations.isDouble(tfPrice, lbError, errorFormat)){
            control=false;
        }
        if(!Validations.isNotNull(dpDateOrder,lbError,errorDateOrder)){
            control=false;
        }
        if(order.getItems().isEmpty()){
            control=false;
            lbError.setText(errorNoLines);
            lbError.setVisible(true);
        }
        if(control){
            ConnectionDB con=new ConnectionDB();
            ArrayList<String> query=lbFeatures.getQuery();
            
            con.openConnection();
            for(int i=0;i<query.size();i++){
                con.executeUpdate(query.get(i));
            }
            con.closeConnection();
            query.clear();
            
            changes=false;
        }
    }
    
    private void deleteOrder(){
        order.deleteFromDB();
        closeWindow();
    }
    
    private void closeWindow(){No funciona
        BorderPane home=(BorderPane)paneOrder.getParent();
        System.out.println("ViewOrder linea 233 Hola"+ changes);
        try {
            home=FXMLLoader.load(getClass().getResource("ViewHome.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(ViewOrderController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
