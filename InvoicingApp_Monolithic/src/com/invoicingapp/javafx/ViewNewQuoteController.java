/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.config.Configuration;
import com.invoicingapp.config.Translations;
import invoicingapp_monolithic.BankAccount;
import invoicingapp_monolithic.ChangeRate;
import invoicingapp_monolithic.Currency;
import invoicingapp_monolithic.CustomProv;
import invoicingapp_monolithic.Customer;
import invoicingapp_monolithic.InvoiceCustomer;
import invoicingapp_monolithic.Orders;
import invoicingapp_monolithic.Quotes;
import invoicingapp_monolithic.Users;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewNewQuoteController implements Initializable {

    private ArrayList<Orders> pendingOrders=new ArrayList();
    private ArrayList<Customer> companies=new ArrayList();
    private Customer customer;
    private Users user=new Users();
    private Quotes quote=new Quotes();
    private Configuration config;
    private String currency;
    private String logoPath;
    private String errorDate="Falta la fecha.";
    private String errorNumber="Falta el numero de presupuesto.";
    private String errorNoOrders="El presupuesto no tiene pedidos asignados.";
    private String quoteSaved="El presupuesto se ha guardado correctamente.";
    private String quoteNotSaved="El presupuesto no se ha guardado, es necesario guadar el presupuesto antes de poder verlo.";
    private int imgSize=175;
    private int language=1;
    private boolean saved=false;
    
    @FXML private ScrollPane paneNewQuote;
    @FXML private VBox vbItems;
    @FXML private ComboBox<Customer> cbCustomers;  
    @FXML private ComboBox<Currency> cbCurrency;
    @FXML private ChoiceBox<String> cbLanguages;
    @FXML private Label lbVATNumber,lbStreet,lbCityCp,lbCountry,lbEmail,lbWeb,lbLegalName,
            lbCustLegalName,lbCustVATNumber,lbCustStreet,lbCustCPCity,lbCustStateCountry,lbCustEmail,lbCustWeb,
            lbTotalNet,lbVAT,lbTotal,lbLastQuote,lbTitleSelectAll,
            lbTitleName,lbTitleVATNumber,lbTitleAddress,lbTitleCPCity,lbTitleCountry,lbTitleEmail,lbTitleWeb,
            lbTitleQuote,lbTitleNumber,lbTitleDate,lbTitlePayment,lbTitleDelivery,
            lbTitleTotalNet,lbTitleVAT,lbTitleTotal,
            labelError,lbTitleLastQuote;
    @FXML private ImageView ivLogo;
    @FXML private CheckBox cbSelectAll;
    @FXML private DatePicker dpDocDate;
    @FXML private TextField tfDocNumber,tfPayment,tfDelivery;
    
    public void initData(Customer customer){
        this.customer=customer;
        pendingOrders=customer.getOrdersFromDB(CustomProv.NOTBILLED);
        
        for(int i=0;i<companies.size();i++){
            if(companies.get(i).getIdCustomer()==this.customer.getIdCustomer()){
                cbCustomers.getSelectionModel().select(i);
            }
        }
        language=getLanguage(customer.getDefaultLanguage());
        cbLanguages.getSelectionModel().select(language);
        
        updateData();
        getOrders();
        setTitles();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        InputStream isImage=null;
        File img=null;
        
        config=Configuration.getConfiguration();
        logoPath=config.getLogoPath();
        user.getFromDB(config.getIdUser());
        companies=Customer.getAllCustomersFromDB(CustomProv.ENABLED);
        
        populateCbCustomers();
        populateCbLanguages();
        populateCbCurrency();
        cbCurrency.getSelectionModel().select(0);
        currency=cbCurrency.getSelectionModel().getSelectedItem().getSymbol();
        
        lbLastQuote.setText(Quotes.getLastQuoteNumber());
        lbLegalName.setText(user.getLegalName());
        lbVATNumber.setText(user.getVatNumber());
        lbStreet.setText(user.getAddress().getStreet()+" "+user.getAddress().getStNumber()+" "+user.getAddress().getApt());
        lbCityCp.setText(user.getAddress().getCp()+" / "+user.getAddress().getCity());
        lbCountry.setText(user.getAddress().getCountry());
        lbEmail.setText(user.getEmail());
        lbWeb.setText(user.getWeb());
        try {
            img=new File(logoPath);
            isImage = (InputStream) new FileInputStream(img);
            ivLogo.setImage(new Image(isImage));
            ivLogo.setFitWidth(imgSize);
            ivLogo.setFitHeight(imgSize);
            ivLogo.setPreserveRatio(true);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ViewNewQuoteController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                isImage.close();
            } catch (IOException ex) {
                Logger.getLogger(ViewNewQuoteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @FXML protected void getSelectionCBCustomers(){
        customer=cbCustomers.getSelectionModel().getSelectedItem();
        language=getLanguage(customer.getDefaultLanguage());
        cbLanguages.getSelectionModel().select(language);
        pendingOrders=customer.getOrdersFromDB(CustomProv.NOTBILLED);
        setTitles();
        updateData();
        getOrders();
        quote.setCustomer(customer);
    }
    
    @FXML protected void selectAllOrders(){
        for(int i=0;i<pendingOrders.size();i++){
            pendingOrders.get(i).setSelected(cbSelectAll.isSelected());
        }
        getOrders();
    }
    
    /*@FXML protected void onClicSee(){
        labelError.setVisible(false);
        if(saved){
            FXMLLoader loader=new FXMLLoader();
            Parent quoteView=null;
            ViewQuoteController controller=null;
            BorderPane home=(BorderPane)paneNewQuote.getParent();
        
            loader.setLocation(getClass().getResource("viewQuote.fxml"));
            try {
                quoteView=loader.load();
            } catch (IOException ex) {
                Logger.getLogger(ViewNewQuoteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
            controller=loader.getController();
            controller.initData(quote);
            home.setCenter(quoteView);
        }else{
            labelError.setText(quoteNotSaved);
            labelError.setVisible(true);
        }
    }*/
    
    @FXML protected void getSelectionCurrency(){
        currency=cbCurrency.getSelectionModel().getSelectedItem().getSymbol();
        updateTotals();
    }
    
    @FXML protected void save(){
        boolean control=true;
        labelError.setVisible(false);
        
        quote.setCustomer(customer);
        if(dpDocDate.getValue()!=null){
            quote.setDocDate(dpDocDate.getValue());
        }else{
            labelError.setText(errorDate);
            labelError.setVisible(true);
            control=false;
        }
        if(!tfDocNumber.getText().isEmpty()){
            quote.setDocNumber(tfDocNumber.getText());
        }else{
            labelError.setText(errorNumber);
            labelError.setVisible(true);
            control=false;
        }
        quote.setAccepted(false);
        quote.setLanguage(Translations.languages[language]);
        quote.setNotePayment(tfPayment.getText());
        quote.setNoteDelivery(tfDelivery.getText());
        quote.setUser(user);
        quote.setVat(customer.getDefaultVAT());
        quote.setCurrency(currency);
        for(int i=0;i<pendingOrders.size();i++){
            if(pendingOrders.get(i).isSelected()){
                quote.addOrder(pendingOrders.get(i));
            }
        }
        if(quote.getOrders().size()<1){
            labelError.setText(errorNoOrders);
            labelError.setVisible(true);
            control=false;
        }
        if(control){
            quote.addToDB();
            saved=true;
            labelError.setText(quoteSaved);
            labelError.setVisible(true);
        }
    }
    
    @FXML protected void cancel(){
        if(!saved){
            ConfirmationDialog.show("¿Está seguro de querer volver sin guardar?", this::backToViewDetailsCustomer, () -> {});
        }else{
            backToViewDetailsCustomer();
        }
    }
    
    private void populateCbCustomers(){
        ObservableList<Customer> list =FXCollections.observableArrayList(companies);
        cbCustomers.setItems(list);
        
        cbCustomers.setCellFactory(new Callback<ListView<Customer>, ListCell<Customer>>() {
            @Override
            public ListCell<Customer> call(ListView<Customer> p) {
                return new ListCell<Customer>() {
                    @Override
                    protected void updateItem(Customer item, boolean empty) {
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
        
        cbCustomers.setButtonCell(new ListCell<Customer>() {
            @Override
            protected void updateItem(Customer item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.getComName());
                } else {
                    setText(null);
                }
            }
        });
    }
    
    private void populateCbCurrency(){
        ObservableList<Currency> currenciesObs =FXCollections.observableArrayList(Translations.currencies);
        cbCurrency.setItems(currenciesObs);
        
        cbCurrency.setCellFactory(new Callback<ListView<Currency>, ListCell<Currency>>() {
            @Override
            public ListCell<Currency> call(ListView<Currency> p) {
                return new ListCell<Currency>() {
                    @Override
                    protected void updateItem(Currency item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.toString());
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });
        
        cbCurrency.setButtonCell(new ListCell<Currency>() {
            @Override
            protected void updateItem(Currency item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.toString());
                } else {
                    setText(null);
                }
            }
        });
    }
    
    private void populateCbLanguages(){
        cbLanguages.getItems().addAll( Translations.languages);
        cbLanguages.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            getSelectionCBLanguages(newValue);
        });
    }
    
    private void getSelectionCBLanguages(String newValue){
        language=getLanguage(newValue);
        setTitles();
        quote.setLanguage(newValue);
    }
    
    private void getOrders(){
        FXMLLoader loader;
        Parent ordersListView=null;
        ViewOrdersListController controller=null;
        
        vbItems.getChildren().clear();
        for (int j=0;j<pendingOrders.size();j++){
            loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("viewOrdersList.fxml"));
            try {
                ordersListView=loader.load();
            } catch (IOException ex) {
                Logger.getLogger(ViewNewQuoteController.class.getName()).log(Level.SEVERE, null, ex);
            }
            controller=loader.getController();
            controller.initData(pendingOrders.get(j),this);
            vbItems.getChildren().add(ordersListView);
        }
        updateTotals();
    }
    
    private void updateData(){
        lbCustLegalName.setText(customer.getLegalName());
        lbCustVATNumber.setText(customer.getVatNumber());
        lbCustStreet.setText(customer.getAddress().getStreet()+" "+customer.getAddress().getStNumber()+" "+customer.getAddress().getApt());
        lbCustCPCity.setText(customer.getAddress().getCp()+" / "+customer.getAddress().getCity());
        lbCustStateCountry.setText(customer.getAddress().getState()+" / "+customer.getAddress().getCountry());
        lbCustEmail.setText(customer.getEmail());
        lbCustWeb.setText(customer.getWeb());
   }
    
    protected void updateTotals(){
        double totalNet=0;
        double totalVAT=0;
        double totalQuote=0;
        
        for(int i=0;i<pendingOrders.size();i++){
            if(pendingOrders.get(i).isSelected()){
                totalNet=totalNet+pendingOrders.get(i).getTotalOrder();
                totalVAT=totalVAT+(pendingOrders.get(i).getTotalOrder()*(customer.getDefaultVAT()/100));
            }
            totalQuote=totalNet+totalVAT;
        }
        lbTotalNet.setText(String.format("%.2f"+currency,totalNet));
        lbVAT.setText(String.format("%.2f%%",customer.getDefaultVAT()));
        lbTotal.setText(String.format("%.2f"+currency,totalQuote));
    }
    
    private int getLanguage(String language){
        int l=0;
        switch (language) {
            case "English":
                l=0;
                break;
            case "Español":
                l=1;
                break;
            case "Français":
                l=2;
                break;
            default:
                break;
        }
        return l;
    }
    
    private void setTitles(){
        
        lbTitleLastQuote.setText(Translations.titleLastQuote[language]);
        lbTitleName.setText(Translations.titleName[language]);
        lbTitleVATNumber.setText(Translations.titleVATNumber[language]);
        lbTitleAddress.setText(Translations.titleAddress[language]);
        lbTitleCPCity.setText(Translations.titleCPCity[language]);
        lbTitleCountry.setText(Translations.titleCountry[language]);
        lbTitleEmail.setText(Translations.titleEmail[language]);
        lbTitleWeb.setText(Translations.titleWeb[language]);
        lbTitleQuote.setText(Translations.titleQuote[language]);
        lbTitleNumber.setText(Translations.titleNumber[language]);
        lbTitleDate.setText(Translations.titleDate[language]);
        lbTitleSelectAll.setText(Translations.titleSelectAll[language]);
        lbTitlePayment.setText(Translations.titlePayment[language]);
        lbTitleDelivery.setText(Translations.titleDelivery[language]);
        lbTitleTotalNet.setText(Translations.titleTotalNet[language]);
        lbTitleVAT.setText(Translations.titleVAT[language]);
        lbTitleTotal.setText(Translations.titleTotalQuote[language]);
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
            Logger.getLogger(ViewNewInvoiceCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(customer);
        home=(BorderPane)paneNewQuote.getParent();
        home.setCenter(detailsCustomerView);
    }
    
    
}
