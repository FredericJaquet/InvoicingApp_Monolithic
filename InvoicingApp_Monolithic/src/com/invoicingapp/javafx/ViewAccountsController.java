/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.tools.LabelFeatures;
import invoicingapp_monolithic.BankAccount;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class ViewAccountsController implements Initializable {

    private ArrayList<String> query=new ArrayList();
    private BankAccount account;
    private LabelFeatures lbFeatures=new LabelFeatures();
    
    @FXML private Label lb_Iban,lb_Swift,lb_Holder,lb_Branch;
    @FXML private TextField fieldIban,fieldSwift,fieldHolder,fieldBranch;
       
    public void initData(BankAccount account){
        this.account=account;
        lb_Iban.setText(account.getIban());
        lb_Swift.setText(account.getSwift());
        lb_Holder.setText(account.getHolder());
        lb_Branch.setText(account.getBranch());
        lbFeatures.makeLabelEditable(lb_Iban,fieldIban,"BankAccount","iban",account.getIdBankAccount());
        lbFeatures.makeLabelEditable(lb_Swift,fieldSwift,"BankAccount","swift",account.getIdBankAccount());
        lbFeatures.makeLabelEditable(lb_Holder,fieldHolder,"BankAccount","holder",account.getIdBankAccount());
        lbFeatures.makeLabelEditable(lb_Branch,fieldBranch,"BankAccount","branch",account.getIdBankAccount());
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    public BankAccount getBankAccount(){
        return account;
    }
    
    public ArrayList<String> getQuery(){
        query=lbFeatures.getQuery();
        return query;
    }
    
    public void resetQuery(){
        query.clear();
    }
    
}
