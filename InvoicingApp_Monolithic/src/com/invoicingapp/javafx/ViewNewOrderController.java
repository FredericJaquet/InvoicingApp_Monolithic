/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.CustomProv;
import invoicingapp_monolithic.Scheme;
import invoicingapp_monolithic.SchemeLine;
import invoicingapp_monolithic.Item;
import invoicingapp_monolithic.Orders;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewNewOrderController implements Initializable {

    private Orders order=new Orders();
    private CustomProv company;
    private Scheme scheme;
    private ArrayList<SchemeLine> lines=new ArrayList();
    private ArrayList<CustomProv> companies=new ArrayList();
    private int iLine=0;
    private double updatedTotal=0;
    private final String errorFormat="Uno de los datos introducidos no es correcto.";
    private final String errorEmpty="Falta un dato obligatorio.";
    
    @FXML ComboBox cbCustomProvs, cbSchemes;
    @FXML TextField tfDescription,tfLineDescription, tfDiscount,tfQuantity,tfPrice,tfUnits,tfFieldName,tfSourceLanguage,tfTargetLanguage;
    @FXML Label lbLegalName,lbVATNumber,lbUpdatedTotal;
    @FXML DatePicker dpDateOrder;
    @FXML TableView<Item> tvItems;
    @FXML TableColumn<Item,String> columnDescription;
    @FXML TableColumn<Item,Double> columnDiscount,columnQuantity;
    
    public void initData(CustomProv company){
        this.company=company;
        updateData();
        for(int i=0;i<companies.size();i++){
            if(companies.get(i).getIdCustomProv()==company.getIdCustomProv()){
                cbCustomProvs.getSelectionModel().select(i);
            }
        }
        popuplateCbSchemes();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        companies=CustomProv.getAllCustomProvFromDB(CustomProv.ENABLED);
        populateCbCustomProvs();
        lbUpdatedTotal.setText(String.valueOf(0.00)+"€");
        dpDateOrder.setValue(LocalDate.now());
    }
    
    @FXML protected void getSelectionCBCustomProvs(){
        company=(CustomProv) cbCustomProvs.getSelectionModel().getSelectedItem();
        if(company!=null){
            popuplateCbSchemes();
            updateData();
        }
    }
    
    @FXML protected void getSelectionCBSchemes(){
        scheme=(Scheme) cbSchemes.getSelectionModel().getSelectedItem();
        updateData();
        iLine=0;
        updateSchemeLine();
    }
    
    @FXML protected void onCommitLine(KeyEvent event){
        Item newLine=new Item();
        boolean control=true;
        
        if (event.getCode()==KeyCode.ENTER) {
            newLine.setDescription(tfLineDescription.getText());
            if(tfLineDescription.getText().equals("")){
                tfLineDescription.getStyleClass().add("error");
                control=false;
            }
            try{
                newLine.setDiscount(Double.parseDouble(tfDiscount.getText()));
            }catch(NumberFormatException ex){
                tfDiscount.getStyleClass().add("error");
                control=false;
            }
            try{
                newLine.setQuantity(Double.parseDouble(tfQuantity.getText()));
            }catch(NumberFormatException ex){
                tfQuantity.getStyleClass().add("error");
                control=false;
            }
            if(control){
                if(newLine.getQuantity()>0){
                    order.addItem(newLine);
                }
                createTableSchemeLines();
                control=true;
                tfLineDescription.getStyleClass().remove("error");
                tfQuantity.getStyleClass().remove("error");
                tfDiscount.getStyleClass().remove("error");
                tfLineDescription.clear();
                tfDiscount.clear();
                tfQuantity.clear();
                updateSchemeLine();
                updateTotalOrder(newLine);
            }
        }
    }
    
    @FXML protected void onClicSave(){
        boolean control=true;
        
        order.setBilled(false);
        order.setDateOrder(dpDateOrder.getValue());
        if(!tfDescription.getText().isEmpty()){
            order.setDescription(tfDescription.getText());
        }else{
            control=false;
            tfDescription.getStyleClass().add("error");
        }
        order.setFieldName(tfFieldName.getText());
        order.setIdCustomProv(company.getIdCustomProv());
        try{
            order.setPricePerUnit(Double.parseDouble(tfPrice.getText()));
        }catch(NumberFormatException ex){
            tfPrice.getStyleClass().add("error");
            control=false;
        }
        order.setSourceLanguage(tfSourceLanguage.getText());
        order.setTargetLanguage(tfTargetLanguage.getText());
        order.setUnits(tfUnits.getText());
        
        if(control){
            order.addToDB();
            clearAll();
        }
    }
    
    private void popuplateCbSchemes(){
        ObservableList<Scheme> schemeObs=FXCollections.observableArrayList(company.getSchemes());
        cbSchemes.setItems(schemeObs);
        
        cbSchemes.setCellFactory(new Callback<ListView<Scheme>, ListCell<Scheme>>() {
            @Override
            public ListCell<Scheme> call(ListView<Scheme> p) {
                return new ListCell<Scheme>() {
                    @Override
                    protected void updateItem(Scheme item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getName());
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });
        
        cbSchemes.setButtonCell(new ListCell<Scheme>() {
            @Override
            protected void updateItem(Scheme item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.getName());
                } else {
                    setText(null);
                }
            }
        });
    }
    
    private void populateCbCustomProvs(){
        ObservableList<CustomProv> companyObs =FXCollections.observableArrayList(companies);
        cbCustomProvs.setItems(companyObs);
        
        cbCustomProvs.setCellFactory(new Callback<ListView<CustomProv>, ListCell<CustomProv>>() {
            @Override
            public ListCell<CustomProv> call(ListView<CustomProv> p) {
                return new ListCell<CustomProv>() {
                    @Override
                    protected void updateItem(CustomProv item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getComName());
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });
        
        cbCustomProvs.setButtonCell(new ListCell<CustomProv>() {
            @Override
            protected void updateItem(CustomProv item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.getComName());
                } else {
                    setText(null);
                }
            }
        });
    }
    
    private void updateData(){
        if(company!=null){
            lbLegalName.setText(company.getLegalName());
            lbVATNumber.setText(company.getVatNumber());
        }
        if(scheme!=null){
            lines=scheme.getLines();
            tfPrice.setText(String.valueOf(scheme.getPrice()));
            tfUnits.setText(scheme.getUnits());
            tfFieldName.setText(scheme.getField());
            tfSourceLanguage.setText(scheme.getSourceLanguage());
            tfTargetLanguage.setText(scheme.getTargetLanguage());
        }
    }

    private void updateSchemeLine(){
        if(iLine<lines.size()){
            tfLineDescription.setText(lines.get(iLine).getDescription());
            tfDiscount.setText(String.valueOf(lines.get(iLine).getDiscount()));
            iLine++;
        }
    }
    
    private void updateTotalOrder(Item item){
        double price=0;
        boolean control=true;
        
        try{
            price=Double.parseDouble(tfPrice.getText());
        }catch(NumberFormatException ex){
            tfPrice.getStyleClass().add("error");
            control=false;
        }
        
        if(control){
            updatedTotal=updatedTotal+price*item.getQuantity()*(100-item.getDiscount())/100;
            lbUpdatedTotal.setText(String.format("%.2f€", updatedTotal));
            tfPrice.getStyleClass().remove("error");
        }
    }

    private void createTableSchemeLines(){
        ObservableList<Item> items=FXCollections.observableArrayList(order.getItems());
        
        columnDescription.setCellValueFactory(new PropertyValueFactory<Item, String>("description"));
        columnDiscount.setCellValueFactory(new PropertyValueFactory<Item,Double>("discount"));
        columnQuantity.setCellValueFactory(new PropertyValueFactory<Item,Double>("quantity"));
                
        tvItems.setItems(items);
    }

    private void clearAll(){
        order=new Orders();
        company=new CustomProv();
        scheme=new Scheme();
        iLine=0;
        updatedTotal=0;
        tfDescription.clear();
        tfLineDescription.clear();
        tfDiscount.clear();
        tfQuantity.clear();
        tfPrice.clear();
        tfUnits.clear();
        tfFieldName.clear();
        tfSourceLanguage.clear();
        tfTargetLanguage.clear();
        lbLegalName.setText("");
        lbVATNumber.setText("");
        lbUpdatedTotal.setText(",00€");
        dpDateOrder.setValue(LocalDate.now());
        cbCustomProvs.setValue(null);
        cbSchemes.setValue(null);
        createTableSchemeLines();
        
        tfDescription.getStyleClass().remove("error");
        tfPrice.getStyleClass().remove("error");
    }
}
