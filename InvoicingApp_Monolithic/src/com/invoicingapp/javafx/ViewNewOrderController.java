/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.tools.DoubleStringConverter;
import com.invoicingapp.tools.Validations;
import invoicingapp_monolithic.CustomProv;
import invoicingapp_monolithic.Customer;
import invoicingapp_monolithic.Scheme;
import invoicingapp_monolithic.SchemeLine;
import invoicingapp_monolithic.Item;
import invoicingapp_monolithic.Orders;
import invoicingapp_monolithic.Provider;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewNewOrderController implements Initializable {
    
    private Orders order=new Orders();
    private Item newLine=new Item();
    private CustomProv company;
    private Scheme scheme;
    private ArrayList<SchemeLine> lines=new ArrayList();
    private ArrayList<CustomProv> companies=new ArrayList();
    private int iLine=0;
    private int lastView;
    private double updatedTotal=0;
    private boolean control=true;
    private boolean controlLines=true;
    private boolean changes=false;
    private final String errorFormat="Uno de los datos introducidos no es correcto.";
    private final String errorEmpty="Falta un dato obligatorio.";
    private final String errorDateOrder="Falta la Fecha.";
    private final String errorNoLines="El pedido no tiene artículos.";
    
    @FXML ComboBox cbCustomProvs, cbSchemes;
    @FXML TextField tfDescription,tfLineDescription, tfDiscount,tfQuantity,tfPrice,tfUnits,tfFieldName,tfSourceLanguage,tfTargetLanguage;
    @FXML Label lbLegalName,lbVATNumber,lbUpdatedTotal,labelError;
    @FXML DatePicker dpDateOrder;
    @FXML TableView<Item> tvItems;
    @FXML TableColumn<Item,String> columnDescription;
    @FXML TableColumn<Item,Double> columnDiscount,columnQuantity;
    @FXML private GridPane paneNewOrder;
    
    public void initData(Customer customer, int lastView){
        this.company=customer;
        this.lastView=lastView;
        updateData();
        for(int i=0;i<companies.size();i++){
            if(companies.get(i).getIdCustomProv()==company.getIdCustomProv()){
                cbCustomProvs.getSelectionModel().select(i);
            }
        }
        popuplateCbSchemes();
    }
    
    public void initData(Provider provider, int lastView){
        this.company=provider;
        this.lastView=lastView;
        updateData();
        for(int i=0;i<companies.size();i++){
            if(companies.get(i).getIdCustomProv()==company.getIdCustomProv()){
                cbCustomProvs.getSelectionModel().select(i);
            }
        }
        popuplateCbSchemes();
    }
    
    public void initData(int lastView){
        this.lastView=lastView;
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        companies=CustomProv.getAllCustomProvFromDB(CustomProv.ENABLED);
        populateCbCustomProvs();
        lbUpdatedTotal.setText(String.valueOf(0.00)+"€");
        dpDateOrder.setValue(LocalDate.now());
        makeTableViewEditable();
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
        newLine=new Item();
        
        controlLines=true;
        labelError.setVisible(false);
        tfLineDescription.getStyleClass().remove("error");
        tfQuantity.getStyleClass().remove("error");
        tfDiscount.getStyleClass().remove("error");
        if(event.getCode()==KeyCode.ENTER) {
            if(Validations.isNotEmpty(tfLineDescription,labelError,errorEmpty)&&Validations.isNotEmpty(tfQuantity,labelError,errorEmpty)&&Validations.isNotEmpty(tfDiscount,labelError,errorEmpty)&&Validations.isDouble(tfDiscount, labelError, errorFormat)&&Validations.isDouble(tfQuantity, labelError, errorFormat)){
                newLine=new Item(tfLineDescription.getText(),Double.parseDouble(tfQuantity.getText()),Double.parseDouble(tfDiscount.getText()));
                if(newLine.getQuantity()>0){
                    order.addItem(newLine);
                    changes=true;
                }
                createTableItems();
                
                tfLineDescription.clear();
                tfDiscount.clear();
                tfQuantity.clear();
                tfLineDescription.requestFocus();
                updateSchemeLine();
                updateTotalOrder();
            }else{
                controlLines=false;
            }
        }
    }
    
    @FXML protected void onClicCancel(){
        if(changes){
            ConfirmationDialog.show("Hay cambios sin guardar. ¿Está seguro de querer volver sin guardar los cambios?", this::backToPrevious, () -> {});
        }else{
            backToPrevious();
        }
    }
    
    @FXML protected void onClicSave(){
        saveData();
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
        }else{
            tfLineDescription.clear();
            tfDiscount.clear();
        }
    }
    
    private void updateTotalOrder(){
        double price=0;
        boolean controlTotal=true;
        updatedTotal=0;
        tfPrice.getStyleClass().remove("error");
        
        try{
            price=Double.parseDouble(tfPrice.getText());
        }catch(NumberFormatException ex){
            tfPrice.getStyleClass().add("error");
            controlTotal=false;
        }
        if(controlTotal){
            for(int i=0;i<order.getItems().size();i++){
                updatedTotal=updatedTotal+price*order.getItems().get(i).getQuantity()*(100-order.getItems().get(i).getDiscount())/100;
                lbUpdatedTotal.setText(String.format("%.2f€", updatedTotal));
            }
        }
    }
    
    private void makeTableViewEditable(){
        labelError.setVisible(false);
        labelError.setText(errorFormat);
        
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
                updateTotalOrder();
            } catch (NumberFormatException ex) {
                labelError.setVisible(true);
            }
        });

        columnQuantity.setOnEditCommit(event -> {
            Item item = event.getRowValue();
            try {
                double quantity = event.getNewValue();
                item.setQuantity(quantity);
                updateTotalOrder();
            } catch (NumberFormatException ex) {
                labelError.setVisible(true);
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
        lbUpdatedTotal.setText("0,00€");
        dpDateOrder.setValue(LocalDate.now());
        cbCustomProvs.setValue(null);
        cbSchemes.setValue(null);
        
        createTableItems();
        
        tfDescription.getStyleClass().remove("error");
        tfPrice.getStyleClass().remove("error");
    }
    
    private void saveData(){        
        control=true;
        labelError.setVisible(false);
        tfDescription.getStyleClass().remove("error");
        tfPrice.getStyleClass().remove("error");
        dpDateOrder.getStyleClass().remove("error");
        
        if(!Validations.isNotEmpty(tfDescription, labelError, errorEmpty)){
            control=false;
        }
        if(control&&!Validations.isNotEmpty(tfPrice, labelError, errorEmpty)){
            control=false;
        }
        if(control&&!Validations.isDouble(tfPrice, labelError, errorFormat)){
            control=false;
        }
        if(control&&!Validations.isNotNull(dpDateOrder,labelError,errorDateOrder)){
            control=false;
        }
        if(control&&order.getItems().isEmpty()){
            control=false;
            labelError.setText(errorNoLines);
            labelError.setVisible(true);
        }
        if(control&&controlLines){
            order.setBilled(false);
            order.setDateOrder(dpDateOrder.getValue());
            order.setDescription(tfDescription.getText());
            order.setFieldName(tfFieldName.getText());
            order.setIdCustomProv(company.getIdCustomProv());
            order.setPricePerUnit(Double.parseDouble(tfPrice.getText()));
            order.setSourceLanguage(tfSourceLanguage.getText());
            order.setTargetLanguage(tfTargetLanguage.getText());
            order.setUnits(tfUnits.getText());
            order.addToDB();
            clearAll();
            changes=false;
        }
    }
    
    private void backToPrevious(){
        if(company==null){
            lastView=3;
        }
        switch(lastView){
            case(1):backToViewDetailsCustomer();break;
            case(2):backToViewDetailsProvider();break;
            case(3):backToViewOrders();break;
        }
    }
    
    private void backToViewDetailsCustomer(){
        FXMLLoader loader=new FXMLLoader();
        Parent detailsCustomerView=null;
        ViewDetailsCustomerController controller=null;
        BorderPane home=null;
        
        loader.setLocation(getClass().getResource("viewDetailsCustomer.fxml"));
        try {
            detailsCustomerView=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewNewOrderController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData((Customer)company);
        home=(BorderPane)paneNewOrder.getParent();
        home.setCenter(detailsCustomerView);
    }
    
    private void backToViewDetailsProvider(){
        FXMLLoader loader=new FXMLLoader();
        Parent detailsCustomerView=null;
        ViewDetailsProviderController controller=null;
        BorderPane home=null;
        
        loader.setLocation(getClass().getResource("viewDetailsProvider.fxml"));
        try {
            detailsCustomerView=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewNewOrderController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData((Provider)company);
        home=(BorderPane)paneNewOrder.getParent();
        home.setCenter(detailsCustomerView);
    }
    
    private void backToViewOrders(){
        Parent ordersView=null;
        BorderPane home=null;
        try {
            ordersView=FXMLLoader.load(getClass().getResource("viewOrders.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(ViewNewOrderController.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        home=(BorderPane)paneNewOrder.getParent();
        home.setCenter(ordersView);
        
    }
}
