/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.tools.Validations;
import invoicingapp_monolithic.BankAccount;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewNewBankAccountController implements Initializable {

    private BankAccount account;
    private boolean control=true;
    private final String errorEmpty="Falta un dato obligatorio.";
    
    @FXML private Label labelError;
    @FXML private VBox paneNewBankAccount;
    @FXML private TextField tfIban,tfSwift,tfHolder,tfBranch;
    
    public void initData(BankAccount account){
        this.account=account;
        if(account.getIban()!=null){
            tfIban.setText(account.getIban());
            tfSwift.setText(account.getSwift());
            tfHolder.setText(account.getHolder());
            tfBranch.setText(account.getBranch());
        }
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        labelError.setText(errorEmpty);
    }
    
    @FXML protected void onClicCancel(){
        closeWindow();
    }
    
    @FXML protected void onClicSave(){
        if(Validations.isNotEmpty(tfIban,labelError,errorEmpty)&&Validations.isNotEmpty(tfHolder,labelError,errorEmpty)){
            account.setIban(tfIban.getText());
            account.setSwift(tfSwift.getText());
            account.setHolder(tfHolder.getText());
            account.setBranch(tfBranch.getText());
            closeWindow();
        }
    }
    
    private void closeWindow(){
        Stage stage = (Stage) paneNewBankAccount.getScene().getWindow();
        stage.close();
    }
}
