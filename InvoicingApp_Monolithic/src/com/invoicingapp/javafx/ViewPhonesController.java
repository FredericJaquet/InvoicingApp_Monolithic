/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.tools.LabelFeatures;
import invoicingapp_monolithic.Phone;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ViewPhonesController implements Initializable {

    private ArrayList<String> query=new ArrayList();
    private Phone phone;
    private LabelFeatures lbFeatures=new LabelFeatures();
    
    @FXML private Label lb_PhoneNumber, lb_Kind;
    @FXML private TextField fieldPhoneNumber, fieldKind;
    
    public void initData(Phone phone){
        this.phone=phone;
        lb_PhoneNumber.setText(phone.getPhoneNumber());
        lb_Kind.setText(phone.getKind());
        lbFeatures.makeLabelEditable(lb_PhoneNumber,fieldPhoneNumber,"Phone","phoneNumber",phone.getPhoneNumber());
        lbFeatures.makeLabelEditable(lb_Kind,fieldKind,"Phone","kind",phone.getPhoneNumber());
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public Phone getPhone(){
        return phone;
    }
    
    public ArrayList<String> getQuery(){
        query=lbFeatures.getQuery();
        return query;
    }
    
    public void resetQuery(){
        query.clear();
    }
    
}
