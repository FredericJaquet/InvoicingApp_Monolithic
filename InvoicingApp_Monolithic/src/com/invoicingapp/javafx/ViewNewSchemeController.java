/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import invoicingapp_monolithic.Scheme;
import invoicingapp_monolithic.SchemeLine;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewNewSchemeController implements Initializable {
    private Scheme scheme;
    private ObservableList<SchemeLine> schemeLines=FXCollections.observableArrayList();
    private SchemeLine line=new SchemeLine();
    
    @FXML private TextField fieldSchemeName, fieldPrice,fieldSourceLanguage,fieldTargetLanguage,fieldFieldName, fieldUnits,fieldDescription,fieldDiscount;
    @FXML private TableView<SchemeLine> tableSchemeLine;
    @FXML private TableColumn<SchemeLine,String>columnDescription;
    @FXML private TableColumn<SchemeLine,Double>columnDiscount;
    @FXML private VBox paneNewScheme;
    
    protected void initData(Scheme scheme){
        this.scheme=scheme;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        createTableSchemeLines();
    }    
    
    @FXML protected void onClicCancel(){
        Stage stage = (Stage) paneNewScheme.getScene().getWindow();
        stage.close();
    }
    
    @FXML protected void onClicSave(){
        double price=0;
        boolean control=true;
        
        scheme.setName(fieldSchemeName.getText());
        try{
                price=Double.parseDouble(fieldPrice.getText());
                scheme.setPrice(price);
            }catch(NumberFormatException ex){
                fieldPrice.getStyleClass().add("error");
                control=false;
            }
        scheme.setUnits(fieldUnits.getText());
        scheme.setField(fieldFieldName.getText());
        scheme.setSourceLanguage(fieldSourceLanguage.getText());
        scheme.setTargetLanguage(fieldTargetLanguage.getText());
        
        if(control){
            Stage stage = (Stage) paneNewScheme.getScene().getWindow();
            stage.close();
        }
    }
    
    @FXML protected void commitLine(KeyEvent event) {
        double discount=0;
        boolean control=true;
        
        if (event.getCode() == KeyCode.ENTER) {
            try{
                discount=Double.parseDouble(fieldDiscount.getText());
            }catch(NumberFormatException ex){
                fieldDiscount.getStyleClass().add("error");
                control=false;
            }
            if(control){
                line=new SchemeLine(fieldDescription.getText(),discount);
                schemeLines.add(line);
                scheme.addLine(line);
                
                fieldDescription.clear();
                fieldDiscount.clear();
                fieldDiscount.getStyleClass().remove("error");
                fieldDescription.requestFocus();
            } 
        }
    }
    
    private void createTableSchemeLines(){
        columnDescription.setCellValueFactory(new PropertyValueFactory<SchemeLine, String>("description"));
        columnDiscount.setCellValueFactory(new PropertyValueFactory<SchemeLine,Double>("discount"));
        tableSchemeLine.setItems(schemeLines);
    }
    
}
