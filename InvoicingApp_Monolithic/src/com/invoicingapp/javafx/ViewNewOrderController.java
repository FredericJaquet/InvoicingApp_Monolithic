/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.Company;
import invoicingapp_monolithic.CustomProv;
import invoicingapp_monolithic.Scheme;
import invoicingapp_monolithic.SchemeLine;
import java.net.URL;
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
import javafx.scene.control.TextField;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewNewOrderController implements Initializable {

    private CustomProv company;
    private Scheme scheme;
    private ArrayList<SchemeLine> lines=new ArrayList();
    private ArrayList<CustomProv> companies=new ArrayList();
    
    @FXML ComboBox cbCustomProvs, cbSchemes;
    @FXML TextField tfDescription,tfPrice,tfUnits,tfFieldName,tfSourceLanguage,tfTargetLanguage;
    @FXML Label lbLegalName,lbVATNumber,lbUpdatedTotal;
    @FXML DatePicker dpDateOrder;
    
    public void initData(CustomProv company){
        this.company=company;
        updateData();
        popuplateCbSchemes();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        companies=CustomProv.getAllCustomProvFromDB(CustomProv.ENABLED);
        populateCbCustomProvs();
    }
    
    @FXML protected void getSelectionCBCustomProvs(){
        company=(CustomProv) cbCustomProvs.getSelectionModel().getSelectedItem();
        popuplateCbSchemes();
        updateData();
    }
    
    @FXML protected void getSelectionCBSchemes(){//Cuando cambiamos de Cliente en el CBCUstomProvs, salta un error, porque este metodo viene OnAction.
        scheme=(Scheme) cbSchemes.getSelectionModel().getSelectedItem();
        updateData();
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
}
