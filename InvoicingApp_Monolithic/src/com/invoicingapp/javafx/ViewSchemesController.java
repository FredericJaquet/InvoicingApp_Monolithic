/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.tools.LabelFeatures;
import invoicingapp_monolithic.Scheme;
import invoicingapp_monolithic.SchemeLine;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.cell.TextFieldTableCell;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewSchemesController implements Initializable {

    private ArrayList<String> query=new ArrayList();
    private Scheme scheme;
    private LabelFeatures lbFeatures=new LabelFeatures();
    
    @FXML private Label lb_SchemeName,lb_SourceLanguage,lb_TargetLanguage,lb_Price,lb_Units,lb_FieldName;
    @FXML private TextField fieldSchemeName,fieldSourceLanguage,fieldTargetLanguage,fieldPrice,fieldUnits,fieldFieldName;
    @FXML private TableView<SchemeLine> tableSchemeLines;
    @FXML private TableColumn<SchemeLine,String> columnDescription;
    @FXML private TableColumn<SchemeLine,Double> columnDiscount;
    
    public void initData(Scheme scheme){
        this.scheme=scheme;
        lb_SchemeName.setText(scheme.getName());
        lb_SourceLanguage.setText(scheme.getSourceLanguage());
        lb_TargetLanguage.setText(scheme.getTargetLanguage());
        lb_Price.setText(String.valueOf(scheme.getPrice()));
        lb_Units.setText(scheme.getUnits());
        lb_FieldName.setText(scheme.getField());
        lbFeatures.makeLabelEditable(lb_SchemeName,fieldSchemeName,"Scheme","schemeName",scheme.getIdScheme());
        lbFeatures.makeLabelEditable(lb_SourceLanguage,fieldSourceLanguage,"Scheme","sourceLanguage",scheme.getIdScheme());
        lbFeatures.makeLabelEditable(lb_TargetLanguage,fieldTargetLanguage,"Scheme","targetLanguage",scheme.getIdScheme());
        lbFeatures.makeLabelEditable(lb_Price,fieldPrice,"Scheme","price",scheme.getIdScheme());
        lbFeatures.makeLabelEditable(lb_Units,fieldUnits,"Scheme","units",scheme.getIdScheme());
        lbFeatures.makeLabelEditable(lb_FieldName,fieldFieldName,"Scheme","fieldName",scheme.getIdScheme());
        
        createTableSchemeLines();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    public Scheme getScheme(){
        return scheme;
    }
    
    public ArrayList<String> getQuery(){
        query.addAll(lbFeatures.getQuery());
        return query;
    }
    
    public void resetQuery(){
        query.clear();
    }
    
    private void createTableSchemeLines(){
        ObservableList<SchemeLine> lines=FXCollections.observableArrayList(scheme.getLines());
        
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        
        columnDescription.setCellFactory(TextFieldTableCell.forTableColumn());
        columnDescription.setOnEditCommit(
                (TableColumn.CellEditEvent<SchemeLine, String> t) -> {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setDescription(t.getNewValue());
                    query.add("UPDATE SchemeLine SET descrip = '"+t.getNewValue()+"' WHERE idSchemeLine="+t.getTableView().getItems().get(t.getTablePosition().getRow()).getIdSchemeLine());
                });
        
        columnDiscount.setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.DoubleStringConverter()));
        columnDiscount.setOnEditCommit(
                (TableColumn.CellEditEvent<SchemeLine, Double> t) -> {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setDiscount(t.getNewValue());
                    query.add("UPDATE SchemeLine SET discount = "+t.getNewValue()+" WHERE idSchemeLine="+t.getTableView().getItems().get(t.getTablePosition().getRow()).getIdSchemeLine());
                });

        
        tableSchemeLines.setEditable(true);
        tableSchemeLines.setItems(lines);
    }
    
}
