/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.config.Translations;
import invoicingapp_monolithic.ChangeRate;
import invoicingapp_monolithic.Currency;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewNewChangeRateController implements Initializable {

    private ChangeRate changeRate;
    private final String errorFormat="El formato del tipo de cambio no es correcto.";
    
    @FXML VBox paneChangeRate;
    @FXML ComboBox<Currency> cbCurrency1,cbCurrency2;
    @FXML TextField tfRate;
    @FXML Label labelError;
    
    public void initData(ChangeRate changeRate){
        this.changeRate=changeRate;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateComboBox(cbCurrency1);
        populateComboBox(cbCurrency2);
    }
    
    @FXML protected void save(){
        boolean control=true;
        Stage stage;
        
        tfRate.getStyleClass().remove("error");
        labelError.setVisible(false);
        try{
            changeRate.setRate(Double.parseDouble(tfRate.getText()));
        }catch(NumberFormatException ex){
            tfRate.getStyleClass().add("error");
            labelError.setText(errorFormat);
            labelError.setVisible(true);
            control=false;
        }
        
        if(control){
            changeRate.setCurrency1(cbCurrency1.getSelectionModel().getSelectedItem().getSymbol());
            changeRate.setCurrency2(cbCurrency2.getSelectionModel().getSelectedItem().getSymbol());
            changeRate.addToDB();
            stage=(Stage)paneChangeRate.getScene().getWindow();
            stage.close();
        }
    }
    
    @FXML protected void cancel(){
        
        Stage stage=(Stage)paneChangeRate.getScene().getWindow();
        stage.close();
    }
    
    private void populateComboBox(ComboBox<Currency> cbCurrency){
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
    
}
