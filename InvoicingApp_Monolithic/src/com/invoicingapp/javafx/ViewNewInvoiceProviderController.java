/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.config.Configuration;
import com.invoicingapp.config.Translations;
import com.invoicingapp.tools.Validations;
import invoicingapp_monolithic.ChangeRate;
import invoicingapp_monolithic.CustomProv;
import invoicingapp_monolithic.InvoiceProvider;
import invoicingapp_monolithic.Orders;
import invoicingapp_monolithic.Provider;
import invoicingapp_monolithic.Users;
import java.io.IOException;
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
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewNewInvoiceProviderController implements Initializable {

    private ArrayList<Orders> pendingOrders=new ArrayList();
    private ArrayList<Provider> companies=new ArrayList();
    private ArrayList<ChangeRate> changeRates=new ArrayList();
    private ArrayList<ViewOrdersListController> OrderControllers=new ArrayList();
    private Provider provider;
    private ChangeRate changeRate=new ChangeRate();
    private Users user=new Users();
    private InvoiceProvider invoice=new InvoiceProvider();
    private Configuration config;
    private String logoPath;
    private final String errorDate="Falta la fecha.";
    private final String errorNumber="Falta el numero de factura.";
    private final String errorNoOrders="La factura no tiene pedidos asignados.";
    private final String invoiceSaved="La factura se ha guardado correctamente.";
    private final String invoiceNotSaved="La factura no se ha guardado, es necesario guadar la factura antes de poder verla.";
    private final int imgSize=175;
    private int language=1;
    private int lastView;
    private boolean saved=false;
    
    @FXML private ScrollPane paneNewInvoice;
    @FXML private VBox vbItems;
    @FXML private ComboBox<Provider> cbProviders;
    @FXML private ComboBox<ChangeRate> cbChangeRates; 
    @FXML private ChoiceBox<String> cbLanguages;
    @FXML private Label lbVATNumber,lbStreet,lbCityCp,lbCountry,lbEmail,lbWeb,lbLegalName,
            lbProvLegalName,lbProvVATNumber,lbProvStreet,lbProvCPCity,lbProvStateCountry,lbProvEmail,lbProvWeb,
            lbTotalNet,lbVAT,lbTotalVAT,lbWithholding,lbTotalWithholding,lbTotalInvoice,lbTotalToPay,lbTitleSelectAll,lbTotalInvoice2,lbTotalToPay2,
            lbTitleName,lbTitleVATNumber,lbTitleAddress,lbTitleCPCity,lbTitleCountry,lbTitleEmail,lbTitleWeb,
            lbTitleInvoice,lbTitleNumber,lbTitleDate,
            lbTitleTotalNet,lbTitleVAT,lbTitleTotalVAT,lbTitleWithholding,lbTitleTotalWithholding,lbTitleTotalInvoice,lbTitleTotalToPay,lbTitleTotalInvoice2,lbTitleTotalToPay2,
            labelError;
    @FXML private CheckBox cbSelectAll;
    @FXML private DatePicker dpDocDate;
    @FXML private TextField tfDocNumber;
    
    public void initData(Provider provider, int prev){
        this.provider=provider;
        this.lastView=prev;
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
    
    public void initData(int prev){
        this.lastView=prev;
    }
    
    /**
     * Initializes the OrderController class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        config=Configuration.getConfiguration();
        logoPath=config.getLogoPath();
        user.getFromDB(config.getIdUser());
        companies=Provider.getAllProvidersFromDB(CustomProv.ENABLED);
        changeRates=ChangeRate.getAllChangeRatesFromDB();
        
        populateCbProviders();
        populateCbLanguages();
        populateCbChangeRates();
        cbChangeRates.getSelectionModel().select(0);
        changeRate.getFromDB(cbChangeRates.getSelectionModel().getSelectedItem().getIdChangeRate());
        invoice.setChangeRate(changeRate);
        invoice.setCurrency(changeRate.getCurrency1());
        
        lbLegalName.setText(user.getLegalName());
        lbVATNumber.setText(user.getVatNumber());
        lbStreet.setText(user.getAddress().getStreet()+" "+user.getAddress().getStNumber()+" "+user.getAddress().getApt());
        lbCityCp.setText(user.getAddress().getCp()+" / "+user.getAddress().getCity());
        lbCountry.setText(user.getAddress().getCountry());
        lbEmail.setText(user.getEmail());
        lbWeb.setText(user.getWeb());
    }
    
    @FXML protected void getSelectionCBCustomers(){
        provider=cbProviders.getSelectionModel().getSelectedItem();
        language=getLanguage(provider.getDefaultLanguage());
        cbLanguages.getSelectionModel().select(language);
        pendingOrders=provider.getOrdersFromDB(CustomProv.NOTBILLED);
        setTitles();
        updateData();
        getOrders();
        invoice.setProvider(provider);
    }
    
    @FXML protected void getSelectionCBChangeRates(){
        changeRate=cbChangeRates.getSelectionModel().getSelectedItem();
        invoice.setChangeRate(changeRate);
        try{
            invoice.setCurrency(changeRate.getCurrency1());
            updateTotals();
            setTitles();
            setCurrencyToOrders();
        }catch(NullPointerException e){}
    }
    
    @FXML protected void selectAllOrders(){
        for(int i=0;i<pendingOrders.size();i++){
            pendingOrders.get(i).setSelected(cbSelectAll.isSelected());
        }
        getOrders();
        setCurrencyToOrders();
    }
    
    @FXML protected void onClicAddChangeRate(){
        ChangeRate chgRate=new ChangeRate();
        FXMLLoader loader=new FXMLLoader();
        Parent root=null;
        Stage viewNewChangeRate=new Stage();
        Scene scene;
        ViewNewChangeRateController controller=null;
        
        loader.setLocation(getClass().getResource("viewNewChangeRate.fxml"));
        try {
            root=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ViewNewInvoiceProviderController.class.getName()).log(Level.SEVERE, null, ex);
        }
        controller=loader.getController();
        controller.initData(chgRate);
        scene=new Scene(root);
        viewNewChangeRate.setScene(scene);
        viewNewChangeRate.show();
        
        viewNewChangeRate.setOnHiding(event -> {
                paneNewInvoice.getParent().setDisable(false);
                this.changeRate=chgRate;
                changeRates.add(chgRate);
                invoice.setChangeRate(chgRate);
                invoice.setCurrency(changeRate.getCurrency1());
                populateCbChangeRates();
                cbChangeRates.getSelectionModel().selectLast();
                try{
                    updateTotals();
                    setTitles();
                    setCurrencyToOrders();
                }catch(NullPointerException e){}
                
                updateTotals();
                setCurrencyToOrders();
            });
        paneNewInvoice.getParent().setDisable(true);
    }
    
    @FXML protected void onClicSee(){
        labelError.setVisible(false);
        if(saved){
            FXMLLoader loader=new FXMLLoader();
            Parent invoiceProviderView=null;
            ViewInvoiceProviderController controller=null;
            BorderPane home=(BorderPane)paneNewInvoice.getParent();
        
            loader.setLocation(getClass().getResource("viewInvoiceProvider.fxml"));
            try {
                invoiceProviderView=loader.load();
            } catch (IOException ex) {
                Logger.getLogger(ViewNewInvoiceProviderController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
            controller=loader.getController();
            controller.initData(invoice,lastView);
            home.setCenter(invoiceProviderView);
        }else{
            labelError.setText(invoiceNotSaved);
            labelError.setVisible(true);
        }
    }
    
    @FXML protected void save(){
        boolean control=true;
        labelError.setVisible(false);
        
        invoice.setProvider(provider);
        if(Validations.isNotNull(dpDocDate,labelError,errorDate)){
            invoice.setDocDate(dpDocDate.getValue());
        }else{
            control=false;
        }
        if(Validations.isNotEmpty(tfDocNumber,labelError,errorNumber)){
            invoice.setDocNumber(tfDocNumber.getText());
        }else{
            control=false;
        }
        invoice.setLanguage(Translations.languages[language]);
        invoice.setUser(user);
        invoice.setVat(provider.getDefaultVAT());
        invoice.setWithholding(provider.getDefaultWithholding());
        invoice.setChangeRate(changeRate);
        for(int i=0;i<pendingOrders.size();i++){
            if(pendingOrders.get(i).isSelected()){
                invoice.addOrder(pendingOrders.get(i));
            }
        }
        if(invoice.getOrders().size()<1){
            labelError.setText(errorNoOrders);
            labelError.setVisible(true);
            control=false;
        }
        if(control){
            invoice.addToDB();
            saved=true;
            labelError.setText(invoiceSaved);
            labelError.setVisible(true);
        }
    }
    
    @FXML protected void cancel(){
        if(!saved){
            ConfirmationDialog.show("¿Está seguro de querer volver sin guardar?", this::backToPrevious, () -> {});
        }else{
            backToPrevious();
        }
    }
    
    private void populateCbChangeRates(){
        ObservableList<ChangeRate> list =FXCollections.observableArrayList(changeRates);
        
        cbChangeRates.setItems(list);
        
        cbChangeRates.setCellFactory(new Callback<ListView<ChangeRate>, ListCell<ChangeRate>>() {
            @Override
            public ListCell<ChangeRate> call(ListView<ChangeRate> p) {
                return new ListCell<ChangeRate>() {
                    @Override
                    protected void updateItem(ChangeRate item, boolean empty) {
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
        
        cbChangeRates.setButtonCell(new ListCell<ChangeRate>() {
            @Override
            protected void updateItem(ChangeRate item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.toString());
                } else {
                    setText(null);
                }
            }
        });
    }
    
    private void populateCbProviders(){
        ObservableList<Provider> list =FXCollections.observableArrayList(companies);
        cbProviders.setItems(list);
        
        cbProviders.setCellFactory(new Callback<ListView<Provider>, ListCell<Provider>>() {
            @Override
            public ListCell<Provider> call(ListView<Provider> p) {
                return new ListCell<Provider>() {
                    @Override
                    protected void updateItem(Provider item, boolean empty) {
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
    
    private void populateCbLanguages(){
        cbLanguages.getItems().addAll( Translations.languages);
        cbLanguages.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            getSelectionCBLanguages(newValue);
        });
    }
    
    private void getSelectionCBLanguages(String newValue){
        language=getLanguage(newValue);
        setTitles();
        invoice.setLanguage(newValue);
    }
    
    private void getOrders(){
        FXMLLoader loader;
        Parent ordersListView=null;
        ViewOrdersListController OrderController=null;
        
        vbItems.getChildren().clear();
        for (int j=0;j<pendingOrders.size();j++) {
            loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("viewOrdersList.fxml"));
            try {
                ordersListView=loader.load();
            } catch (IOException ex) {
                Logger.getLogger(ViewNewInvoiceCustomerController.class.getName()).log(Level.SEVERE, null, ex);
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
        double totalWithholding=0;
        double totalInvoice=0;
        double totalToPay=0;
        
        lbTotalInvoice2.setVisible(false);
        lbTotalToPay2.setVisible(false);
        
        for(int i=0;i<pendingOrders.size();i++){
            if(pendingOrders.get(i).isSelected()){
                totalNet=totalNet+pendingOrders.get(i).getTotalOrder();
                totalVAT=totalVAT+(pendingOrders.get(i).getTotalOrder()*(provider.getDefaultVAT()/100));
                totalWithholding=totalWithholding+(pendingOrders.get(i).getTotalOrder()*(provider.getDefaultWithholding()/100));
            }
            totalInvoice=totalNet+totalVAT;
            totalToPay=totalNet+totalVAT-totalWithholding;
        }
        
        lbTotalNet.setText(String.format("%.2f"+changeRate.getCurrency1(),totalNet));
        lbVAT.setText(String.format("%.2f%%",provider.getDefaultVAT()));
        lbTotalVAT.setText(String.format("%.2f"+changeRate.getCurrency1(),totalVAT));
        lbWithholding.setText(String.format("%.2f%%",provider.getDefaultWithholding()));
        lbTotalWithholding.setText(String.format("%.2f"+changeRate.getCurrency1(),totalWithholding));
        lbTotalInvoice.setText(String.format("%.2f"+changeRate.getCurrency1(),totalInvoice));
        lbTotalToPay.setText(String.format("%.2f"+changeRate.getCurrency1(),totalToPay));
        if(!(changeRate.getCurrency1().equals(changeRate.getCurrency2()))){
            lbTotalInvoice2.setVisible(true);
            lbTotalToPay2.setVisible(true);
            lbTotalInvoice2.setText(String.format("%.2f"+changeRate.getCurrency2(),totalInvoice*changeRate.getRate()));
            lbTotalToPay2.setText(String.format("%.2f"+changeRate.getCurrency2(),totalToPay*changeRate.getRate()));
        }
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
        lbTitleTotalInvoice2.setVisible(false);
        lbTitleTotalToPay2.setVisible(false);
        
        lbTitleName.setText(Translations.titleName[language]);
        lbTitleVATNumber.setText(Translations.titleVATNumber[language]);
        lbTitleAddress.setText(Translations.titleAddress[language]);
        lbTitleCPCity.setText(Translations.titleCPCity[language]);
        lbTitleCountry.setText(Translations.titleCountry[language]);
        lbTitleEmail.setText(Translations.titleEmail[language]);
        lbTitleWeb.setText(Translations.titleWeb[language]);
        lbTitleInvoice.setText(Translations.titleInvoice[language]);
        lbTitleNumber.setText(Translations.titleNumber[language]);
        lbTitleDate.setText(Translations.titleDate[language]);
        lbTitleSelectAll.setText(Translations.titleSelectAll[language]);
        lbTitleTotalNet.setText(Translations.titleTotalNet[language]);
        lbTitleVAT.setText(Translations.titleVAT[language]);
        lbTitleTotalVAT.setText(Translations.titleTotalVAT[language]);
        lbTitleWithholding.setText(Translations.titleWithholding[language]);
        lbTitleTotalWithholding.setText(Translations.titleTotalWithholding[language]);
        lbTitleTotalInvoice.setText(Translations.titleTotalInvoice[language]);
        lbTitleTotalToPay.setText(Translations.titleTotalToPay[language]);
        if(!(changeRate.getCurrency1().equals(changeRate.getCurrency2()))){
            lbTitleTotalInvoice2.setVisible(true);
            lbTitleTotalToPay2.setVisible(true);
            lbTitleTotalInvoice2.setText(Translations.titleTotalInvoice[language]);
            lbTitleTotalToPay2.setText(Translations.titleTotalToPay[language]);
        }
    }
    
    private void backToPrevious(){
        if(provider==null){
            lastView=2;
        }
        switch(lastView){
            case(1):backToViewDetailsProvider();break;
            case(2):backToInvoicesProvider();break;
        }
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
            Logger.getLogger(ViewNewInvoiceProviderController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(provider);
        home=(BorderPane)paneNewInvoice.getParent();
        home.setCenter(detailsProviderView);
    }
    
    private void backToInvoicesProvider(){        
        Parent invoicesProviderView=null;
        BorderPane home=null;
        try {
            invoicesProviderView=FXMLLoader.load(getClass().getResource("viewInvoicesProvider.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(ViewNewInvoiceProviderController.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        home=(BorderPane)paneNewInvoice.getParent();
        home.setCenter(invoicesProviderView);
    }
    
    private void setCurrencyToOrders(){
        for(int i=0;i<OrderControllers.size();i++){
            OrderControllers.get(i).setCurrency(invoice.getCurrency());
        }
    } 
    
}
