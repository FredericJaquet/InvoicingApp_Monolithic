/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.config.Configuration;
import com.invoicingapp.config.Translations;
import com.invoicingapp.tools.Validations;
import invoicingapp_monolithic.Currency;
import invoicingapp_monolithic.CustomProv;
import invoicingapp_monolithic.Orders;
import invoicingapp_monolithic.Provider;
import invoicingapp_monolithic.PurchaseOrder;
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

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewNewPOController implements Initializable {

    private ArrayList<Orders> pendingOrders=new ArrayList();
    private ArrayList<Provider> companies=new ArrayList();
    private ArrayList<ViewOrdersListController> OrderControllers=new ArrayList();
    private Provider provider;
    private Users user=new Users();
    private PurchaseOrder po=new PurchaseOrder();
    private Configuration config;
    private String currency;
    private String logoPath;
    private int language=1;
    private boolean saved=false;
    private final int imgSize=175;
    private final String errorDate="Falta la fecha.";
    private final String errorNumber="Falta el numero de Orden de Pedido.";
    private final String errorNoOrders="La Orden de Pedido no tiene pedidos asignados.";
    private final String quoteSaved="La Orden de Pedido se ha guardado correctamente.";
    private final String poNotSaved="La Orden de Pedido no se ha guardado, es necesario guadar la Orden antes de poder verlo.";
    
    @FXML private ScrollPane paneNewPO;
    @FXML private VBox vbItems;
    @FXML private ComboBox<Provider> cbProviders;  
    @FXML private ComboBox<Currency> cbCurrency;
    @FXML private ChoiceBox<String> cbLanguages;
    @FXML private Label lbVATNumber,lbStreet,lbCityCp,lbCountry,lbEmail,lbWeb,lbLegalName,
            lbProvLegalName,lbProvVATNumber,lbProvStreet,lbProvCPCity,lbProvStateCountry,lbProvEmail,lbProvWeb,
            lbTotalNet,lbVAT,lbTotal,lbLastPO,lbTitleSelectAll,
            lbTitleName,lbTitleVATNumber,lbTitleAddress,lbTitleCPCity,lbTitleCountry,lbTitleEmail,lbTitleWeb,
            lbTitlePO,lbTitleNumber,lbTitleDate,lbTitleDelivery,
            lbTitleTotalNet,lbTitleVAT,lbTitleTotal,
            labelError,lbTitleLastPO;
    @FXML private ImageView ivLogo;
    @FXML private CheckBox cbSelectAll;
    @FXML private DatePicker dpDocDate,dpDeadline;
    @FXML private TextField tfDocNumber;
    
    public void initData(Provider provider){
        this.provider=provider;
        pendingOrders=provider.getOrdersFromDB(CustomProv.NOTBILLED);
        
        for(int i=0;i<companies.size();i++){
            if(companies.get(i).getIdProvider()==this.provider.getIdProvider()){
                cbProviders.getSelectionModel().select(i);
            }
        }
        language=getLanguage(provider.getDefaultLanguage());
        cbLanguages.getSelectionModel().select(language);
        
        updateData();
        getOrders();
        setTitles();
    }
    
    /**
     * Initializes the NewQuoteController class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        InputStream isImage=null;
        File img=null;
        
        config=Configuration.getConfiguration();
        logoPath=config.getLogoPath();
        user.getFromDB(config.getIdUser());
        companies=Provider.getAllProvidersFromDB(CustomProv.ENABLED);
        
        populateCbCustomers();
        populateCbLanguages();
        populateCbCurrency();
        cbCurrency.getSelectionModel().select(0);
        currency=cbCurrency.getSelectionModel().getSelectedItem().getSymbol();
        
        lbLastPO.setText(PurchaseOrder.getLastPONumber());
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
    
    @FXML protected void getSelectionCBProviders(){
        provider=cbProviders.getSelectionModel().getSelectedItem();
        language=getLanguage(provider.getDefaultLanguage());
        cbLanguages.getSelectionModel().select(language);
        pendingOrders=provider.getOrdersFromDB(CustomProv.NOTBILLED);
        setTitles();
        updateData();
        getOrders();
        po.setProvider(provider);
    }
    
    @FXML protected void selectAllOrders(){
        for(int i=0;i<pendingOrders.size();i++){
            pendingOrders.get(i).setSelected(cbSelectAll.isSelected());
        }
        getOrders();
    }
    
    @FXML protected void onClicSee(){
        labelError.setVisible(false);
        if(saved){
            FXMLLoader loader=new FXMLLoader();
            Parent poView=null;
            ViewPOController controller=null;
            BorderPane home=null;
        
            loader.setLocation(getClass().getResource("viewPo.fxml"));
            try {
                poView=loader.load();
            } catch (IOException ex) {
                Logger.getLogger(ViewNewPOController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
            controller=loader.getController();
            controller.initData(po);
            home=(BorderPane)paneNewPO.getParent();
            home.setCenter(poView);
        }else{
            labelError.setText(poNotSaved);
            labelError.setVisible(true);
        }
    }
    
    @FXML protected void getSelectionCurrency(){
        currency=cbCurrency.getSelectionModel().getSelectedItem().getSymbol();
        updateTotals();
        for(int i=0;i<OrderControllers.size();i++){
            OrderControllers.get(i).setCurrency(currency);
        }
    }
    
    @FXML protected void save(){
        boolean control=true;
        labelError.setVisible(false);
        
        po.setProvider(provider);
        if(Validations.isNotNull(dpDocDate,labelError,errorDate)){
            po.setDocDate(dpDocDate.getValue());
        }else{
            control=false;
        }
        if(Validations.isNotEmpty(tfDocNumber,labelError,errorNumber)){
            po.setDocNumber(tfDocNumber.getText());
        }else{
            control=false;
        }
        po.setDeadline(dpDeadline.getValue());
        po.setLanguage(Translations.languages[language]);
        po.setUser(user);
        po.setVat(provider.getDefaultVAT());
        po.setCurrency(currency);
        for(int i=0;i<pendingOrders.size();i++){
            if(pendingOrders.get(i).isSelected()){
                po.addOrder(pendingOrders.get(i));
            }
        }
        if(po.getOrders().size()<1){
            labelError.setText(errorNoOrders);
            labelError.setVisible(true);
            control=false;
        }
        if(control){
            po.addToDB();
            saved=true;
            labelError.setText(quoteSaved);
            labelError.setVisible(true);
        }
    }
    
    @FXML protected void cancel(){
        if(!saved){
            ConfirmationDialog.show("¿Está seguro de querer volver sin guardar?", this::backToViewDetailsProvider, () -> {});
        }else{
            backToViewDetailsProvider();
        }
    }
    
    private void populateCbCustomers(){
        ObservableList<Provider> list =FXCollections.observableArrayList(companies);
        cbProviders.setItems(list);
        
        cbProviders.setCellFactory((ListView<Provider> p) -> new ListCell<Provider>() {
            @Override
            protected void updateItem(Provider item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.getComName());
                } else {
                    setText(null);
                }
            }
        });
        
        cbProviders.setButtonCell(new ListCell<Provider>() {
            @Override
            protected void updateItem(Provider item, boolean empty) {
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
        
        cbCurrency.setCellFactory((ListView<Currency> p) -> new ListCell<Currency>() {
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
        po.setLanguage(newValue);
    }
    
    private void getOrders(){
        FXMLLoader loader;
        Parent ordersListView=null;
        ViewOrdersListController OrderController=null;
        
        vbItems.getChildren().clear();
        for (int j=0;j<pendingOrders.size();j++){
            loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("viewOrdersList.fxml"));
            try {
                ordersListView=loader.load();
            } catch (IOException ex) {
                Logger.getLogger(ViewNewQuoteController.class.getName()).log(Level.SEVERE, null, ex);
            }
            OrderController=loader.getController();
            OrderController.initData(pendingOrders.get(j),this);
            OrderControllers.add(OrderController);
            vbItems.getChildren().add(ordersListView);
        }
        updateTotals();
    }
    
    private void updateData(){
        lbProvLegalName.setText(provider.getLegalName());
        lbProvVATNumber.setText(provider.getVatNumber());
        lbProvStreet.setText(provider.getAddress().getStreet()+" "+provider.getAddress().getStNumber()+" "+provider.getAddress().getApt());
        lbProvCPCity.setText(provider.getAddress().getCp()+" / "+provider.getAddress().getCity());
        lbProvStateCountry.setText(provider.getAddress().getState()+" / "+provider.getAddress().getCountry());
        lbProvEmail.setText(provider.getEmail());
        lbProvWeb.setText(provider.getWeb());
   }
    
    protected void updateTotals(){
        double totalNet=0;
        double totalVAT=0;
        double totalQuote=0;
        
        for(int i=0;i<pendingOrders.size();i++){
            if(pendingOrders.get(i).isSelected()){
                totalNet=totalNet+pendingOrders.get(i).getTotalOrder();
                totalVAT=totalVAT+(pendingOrders.get(i).getTotalOrder()*(provider.getDefaultVAT()/100));
            }
            totalQuote=totalNet+totalVAT;
        }
        lbTotalNet.setText(String.format("%.2f"+currency,totalNet));
        lbVAT.setText(String.format("%.2f%%",provider.getDefaultVAT()));
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
        lbTitleLastPO.setText(Translations.titleLastQuote[language]);
        lbTitleName.setText(Translations.titleName[language]);
        lbTitleVATNumber.setText(Translations.titleVATNumber[language]);
        lbTitleAddress.setText(Translations.titleAddress[language]);
        lbTitleCPCity.setText(Translations.titleCPCity[language]);
        lbTitleCountry.setText(Translations.titleCountry[language]);
        lbTitleEmail.setText(Translations.titleEmail[language]);
        lbTitleWeb.setText(Translations.titleWeb[language]);
        lbTitlePO.setText(Translations.titlePO[language]);
        lbTitleNumber.setText(Translations.titleNumber[language]);
        lbTitleDate.setText(Translations.titleDate[language]);
        lbTitleSelectAll.setText(Translations.titleSelectAll[language]);
        lbTitleDelivery.setText(Translations.titleDelivery[language]);
        lbTitleTotalNet.setText(Translations.titleTotalNet[language]);
        lbTitleVAT.setText(Translations.titleVAT[language]);
        lbTitleTotal.setText(Translations.titleTotalQuote[language]);
    }
    
    private void backToViewDetailsProvider(){
        FXMLLoader loader=new FXMLLoader();
        Parent detailsProviderView=null;
        ViewDetailsProviderController controller=null;
        BorderPane home=null;
        
        loader.setLocation(getClass().getResource("viewDetailsProvider.fxml"));
        try {
            detailsProviderView=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewNewPOController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(provider);
        home=(BorderPane)paneNewPO.getParent();
        home.setCenter(detailsProviderView);
    }
    
}
