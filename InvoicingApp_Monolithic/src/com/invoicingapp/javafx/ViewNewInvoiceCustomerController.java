/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.config.Configuration;
import com.invoicingapp.config.PathNames;
import com.invoicingapp.config.Translations;
import com.invoicingapp.tools.Validations;
import invoicingapp_monolithic.BankAccount;
import invoicingapp_monolithic.ChangeRate;
import invoicingapp_monolithic.CustomProv;
import invoicingapp_monolithic.Customer;
import invoicingapp_monolithic.InvoiceCustomer;
import invoicingapp_monolithic.Orders;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewNewInvoiceCustomerController implements Initializable {

    private ArrayList<Orders> pendingOrders=new ArrayList();
    private ArrayList<Customer> companies=new ArrayList();
    private ArrayList<ChangeRate> changeRates=new ArrayList();
    private ArrayList<BankAccount> accounts=new ArrayList();
    private ArrayList<ViewOrdersListController> OrderControllers=new ArrayList();
    private Customer customer;
    private ChangeRate changeRate=new ChangeRate();
    private BankAccount bankAccount=new BankAccount();
    private Users user=new Users();
    private InvoiceCustomer invoice=new InvoiceCustomer();
    private Configuration config;
    private final String errorDate="Falta la fecha.";
    private final String errorNumber="Falta el numero de factura.";
    private final String errorNoOrders="La factura no tiene pedidos asignados.";
    private final String invoiceSaved="La factura se ha guardado correctamente.";
    private final String invoiceNotSaved="La factura no se ha guardado, es necesario guadar la factura antes de poder verla.";
    private final int imgSize=150;
    private String logoPath;
    private int language=1;
    private int lastView;
    private boolean saved=false;
    
    @FXML private ScrollPane paneNewInvoice;
    @FXML private VBox vbItems;
    @FXML private ComboBox<Customer> cbCustomers;
    @FXML private ComboBox<ChangeRate> cbChangeRates;
    @FXML private ComboBox<BankAccount> cbBankAccounts;    
    @FXML private ChoiceBox<String> cbLanguages;
    @FXML private Label lbLastInvoice,lbVATNumber,lbStreet,lbCityCp,lbCountry,lbEmail,lbWeb,lbLegalName,
            lbCustLegalName,lbCustVATNumber,lbCustStreet,lbCustCPCity,lbCustStateCountry,lbCustEmail,lbCustWeb,
            lbHolder,lbBranch,lbPayMethod,
            lbTotalNet,lbVAT,lbTotalVAT,lbWithholding,lbTotalWithholding,lbTotalInvoice,lbTotalToPay,lbTitleSelectAll,lbTotalInvoice2,lbTotalToPay2,
            lbTitleName,lbTitleVATNumber,lbTitleAddress,lbTitleCPCity,lbTitleCountry,lbTitleEmail,lbTitleWeb,
            lbTitleInvoice,lbTitleNumber,lbTitleDate,lbTitleBankDetails,lbTitlePayMethod,lbTitleHolder,lbTitleBranch,
            lbTitleTotalNet,lbTitleVAT,lbTitleTotalVAT,lbTitleWithholding,lbTitleTotalWithholding,lbTitleTotalInvoice,lbTitleTotalToPay,lbTitleTotalInvoice2,lbTitleTotalToPay2,
            labelError,lbTitleLastInvoice;
    @FXML private ImageView ivLogo;
    @FXML private CheckBox cbSelectAll;
    @FXML private DatePicker dpDocDate;
    @FXML private TextField tfDocNumber;
    
    public void initData(Customer customer, int prev){
        this.customer=customer;
        this.lastView=prev;
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
    
    public void initData(int prev){
        this.lastView=prev;
    }
    
    /**
     * Initializes the OrderController class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        InputStream isImage=null;
        File img=null;
        
        config=Configuration.loadConfiguration();
        logoPath=config.getLogoPath();
        user.getFromDB(config.getIdUser());
        accounts=user.getBankAccounts();
        companies=Customer.getAllCustomersFromDB(CustomProv.ENABLED);
        changeRates=ChangeRate.getAllChangeRatesFromDB();
        
        populateCbCustomers();
        populateCbBankAccounts();
        getSelectionCBBankAccounts();
        populateCbLanguages();
        populateCbChangeRates();
        cbChangeRates.getSelectionModel().select(0);
        changeRate.getFromDB(cbChangeRates.getSelectionModel().getSelectedItem().getIdChangeRate());
        invoice.setChangeRate(changeRate);
        invoice.setCurrency(changeRate.getCurrency1());
        
        lbLastInvoice.setText(InvoiceCustomer.getLastInvoiceNumber());
        lbLegalName.setText(user.getLegalName());
        lbVATNumber.setText(user.getVatNumber());
        lbStreet.setText(user.getAddress().getStreet()+" "+user.getAddress().getStNumber()+" "+user.getAddress().getApt());
        lbCityCp.setText(user.getAddress().getCp()+" / "+user.getAddress().getCity());
        lbCountry.setText(user.getAddress().getCountry());
        lbEmail.setText(user.getEmail());
        lbWeb.setText(user.getWeb());
        
        try {
            isImage = new FileInputStream(new File(logoPath));
            ivLogo.setImage(new Image(isImage));
            ivLogo.setFitWidth(imgSize);
            ivLogo.setFitHeight(imgSize);
            ivLogo.setPreserveRatio(true);
        } catch (FileNotFoundException ex) {
                Logger.getLogger(ViewNewInvoiceCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(isImage!=null){
            try {
                isImage.close();
            } catch (IOException ex) {
                Logger.getLogger(ViewNewInvoiceCustomerController.class.getName()).log(Level.SEVERE, null, ex);
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
        invoice.setCustomer(customer);
    }
    
    @FXML protected void getSelectionCBBankAccounts(){
        bankAccount=cbBankAccounts.getSelectionModel().getSelectedItem();
        updateBankData();
        invoice.setBankAccount(bankAccount);
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
            Logger.getLogger(ViewNewInvoiceCustomerController.class.getName()).log(Level.SEVERE, null, ex);
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
            Parent invoiceCustomerView=null;
            ViewInvoiceCustomerController controller=null;
            BorderPane home=(BorderPane)paneNewInvoice.getParent();
        
            loader.setLocation(getClass().getResource("viewInvoiceCustomer.fxml"));
            try {
                invoiceCustomerView=loader.load();
            } catch (IOException ex) {
                Logger.getLogger(ViewNewInvoiceCustomerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
            controller=loader.getController();
            controller.initData(invoice,lastView);
            home.setCenter(invoiceCustomerView);
        }else{
            labelError.setText(invoiceNotSaved);
            labelError.setVisible(true);
        }
    }
    
    @FXML protected void save(){
        boolean control=true;
        labelError.setVisible(false);
        
        invoice.setCustomer(customer);
        if(Validations.isNotNull(dpDocDate,labelError,errorDate)){
            invoice.setDocDate(dpDocDate.getValue());
            invoice.setDuedate(dpDocDate.getValue().plusDays(customer.getDuedate()));
        }else{
            control=false;
        }
        if(Validations.isNotEmpty(tfDocNumber,labelError,errorNumber)){
            invoice.setDocNumber(tfDocNumber.getText());
        }else{
            control=false;
        }
        invoice.setPaid(false);
        invoice.setLanguage(Translations.languages[language]);
        invoice.setUser(user);
        invoice.setVat(customer.getDefaultVAT());
        invoice.setWithholding(customer.getDefaultWithholding());
        invoice.setBankAccount(bankAccount);
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
    
    private void populateCbBankAccounts(){
        ObservableList<BankAccount> list=FXCollections.observableArrayList(accounts);
        cbBankAccounts.setItems(list);
        cbBankAccounts.setCellFactory(new Callback<ListView<BankAccount>, ListCell<BankAccount>>() {
            @Override
            public ListCell<BankAccount> call(ListView<BankAccount> p) {
                return new ListCell<BankAccount>() {
                    @Override
                    protected void updateItem(BankAccount item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getIban());
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });
        
        cbBankAccounts.setButtonCell(new ListCell<BankAccount>() {
            @Override
            protected void updateItem(BankAccount item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.getIban());
                } else {
                    setText(null);
                }
            }
        });
        
        cbBankAccounts.getSelectionModel().select(0);
        updateBankData();
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
        lbCustLegalName.setText(customer.getLegalName());
        lbCustVATNumber.setText(customer.getVatNumber());
        lbCustStreet.setText(customer.getAddress().getStreet()+" "+customer.getAddress().getStNumber()+" "+customer.getAddress().getApt());
        lbCustCPCity.setText(customer.getAddress().getCp()+" / "+customer.getAddress().getCity());
        lbCustStateCountry.setText(customer.getAddress().getState()+" / "+customer.getAddress().getCountry());
        lbCustEmail.setText(customer.getEmail());
        lbCustWeb.setText(customer.getWeb());
        lbPayMethod.setText(customer.getPayMethod());
        updateBankData();
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
                totalVAT=totalVAT+(pendingOrders.get(i).getTotalOrder()*(customer.getDefaultVAT()/100));
                totalWithholding=totalWithholding+(pendingOrders.get(i).getTotalOrder()*(customer.getDefaultWithholding()/100));
            }
            totalInvoice=totalNet+totalVAT;
            totalToPay=totalNet+totalVAT-totalWithholding;
        }
        
        lbTotalNet.setText(String.format("%.2f"+changeRate.getCurrency1(),totalNet));
        lbVAT.setText(String.format("%.2f%%",customer.getDefaultVAT()));
        lbTotalVAT.setText(String.format("%.2f"+changeRate.getCurrency1(),totalVAT));
        lbWithholding.setText(String.format("%.2f%%",customer.getDefaultWithholding()));
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
    
    protected void updateBankData(){
        lbHolder.setText(bankAccount.getHolder());
        lbBranch.setText(bankAccount.getBranch());
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
        
        lbTitleLastInvoice.setText(Translations.titleLastInvoice[language]);
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
        lbTitleBankDetails.setText(Translations.titleBankDetails[language]);
        lbTitlePayMethod.setText(Translations.titlePayMethod[language]);
        lbTitleHolder.setText(Translations.titleHolder[language]);
        lbTitleBranch.setText(Translations.titleBranch[language]);
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
        if(customer==null){
            lastView=2;
        }
        switch(lastView){
            case(1):backToViewDetailsCustomer();break;
            case(2):backToInvoicesCustomer();break;
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
            Logger.getLogger(ViewNewInvoiceCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller=loader.getController();
        controller.initData(customer);
        home=(BorderPane)paneNewInvoice.getParent();
        home.setCenter(detailsCustomerView);
    }
    
    private void backToInvoicesCustomer(){        
        Parent invoicesCustomerView=null;
        BorderPane home=null;
        try {
            invoicesCustomerView=FXMLLoader.load(getClass().getResource("viewInvoicesCustomer.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(ViewNewInvoiceCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        home=(BorderPane)paneNewInvoice.getParent();
        home.setCenter(invoicesCustomerView);
    }
    
    private void setCurrencyToOrders(){
        for(int i=0;i<OrderControllers.size();i++){
            OrderControllers.get(i).setCurrency(invoice.getCurrency());
        }
    }
    
}
