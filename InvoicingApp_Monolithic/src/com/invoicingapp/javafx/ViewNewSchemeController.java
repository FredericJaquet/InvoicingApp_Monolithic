/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.tools.Validations;
import invoicingapp_monolithic.Scheme;
import invoicingapp_monolithic.SchemeLine;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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
    private boolean control=true;
    private boolean controlLines=true;
    private final String errorFormat="Uno de los datos introducidos no es correcto.";
    private final String errorEmpty="Falta un dato obligatorio.";
   
    @FXML private Label labelError;    
    @FXML private TextField fieldSchemeName, fieldPrice,fieldSourceLanguage,fieldTargetLanguage,fieldFieldName,fieldUnits,fieldDescription,fieldDiscount;
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
        closeWindow();
    }
    
    @FXML protected void onClicSave(){
        saveData();
        if(control){
            closeWindow();
        }
    }
    
    @FXML protected void commitLine(KeyEvent event) {
        double discount=0;
        
        controlLines=true;
        labelError.setVisible(false);
        fieldDiscount.getStyleClass().remove("error");
        fieldDescription.getStyleClass().remove("error");
        if (event.getCode() == KeyCode.ENTER) {
            if(Validations.isNotEmpty(fieldDescription,labelError,errorEmpty)&&Validations.isDouble(fieldDiscount, labelError, errorFormat)){
                line=new SchemeLine(fieldDescription.getText(),discount);
                schemeLines.add(line);
                scheme.addLine(line);
                
                fieldDescription.clear();
                fieldDiscount.clear();
                fieldDescription.requestFocus();
            }else{
                controlLines=false;
            }
        }
    }
    
    private void createTableSchemeLines(){
        columnDescription.setCellValueFactory(new PropertyValueFactory<SchemeLine, String>("description"));
        columnDiscount.setCellValueFactory(new PropertyValueFactory<SchemeLine,Double>("discount"));
        tableSchemeLine.setItems(schemeLines);
    }
    
    private void saveData(){
        double price=0;
        
        control=true;
        labelError.setVisible(false);
        fieldSchemeName.getStyleClass().remove("error");
        fieldPrice.getStyleClass().remove("error");
        fieldDescription.getStyleClass().remove("error");
        fieldDiscount.getStyleClass().remove("error");
            
        if(!Validations.isNotEmpty(fieldSchemeName, labelError, errorEmpty)){
            control=false;
        }
        if(!Validations.isNotEmpty(fieldPrice, labelError, errorEmpty)){
            control=false;
        }
        if(!Validations.isDouble(fieldPrice, labelError, errorFormat)){
            control=false;
        }
        if(schemeLines.isEmpty()){
            labelError.setText(errorEmpty);
            labelError.setVisible(true);
            fieldDescription.getStyleClass().add("error");
            fieldDiscount.getStyleClass().add("error");
            control=false;
        }
        
        if(control&&controlLines){
            scheme.setName(fieldSchemeName.getText());
            scheme.setPrice(price);
            scheme.setUnits(fieldUnits.getText());
            scheme.setField(fieldFieldName.getText());
            scheme.setSourceLanguage(fieldSourceLanguage.getText());
            scheme.setTargetLanguage(fieldTargetLanguage.getText());
        }
    }
    
    private void closeWindow(){
        Stage stage = (Stage) paneNewScheme.getScene().getWindow();
        stage.close();
    }
    
}
