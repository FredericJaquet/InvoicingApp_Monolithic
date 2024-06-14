/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.config.Configuration;
import invoicingapp_monolithic.Password;
import invoicingapp_monolithic.Users;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewLoginController implements Initializable {

    private Configuration config=Configuration.getConfiguration();
    private boolean control=false;
    private Users user=new Users();
    
    @FXML private TextField fieldUser;
    @FXML private TextField textFieldPW;
    @FXML private PasswordField fieldPasswd;
    @FXML private CheckBox checkRemindme;
    @FXML private Label labelInfo;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        textFieldPW.setVisible(false);
        user.getFromDB(config.getIdUser());
        checkRemindme.setSelected(config.isReminder());
        
        if(config.isReminder()){
            fieldUser.setText(user.getUserName());
            fieldPasswd.setText("This is not a Password");
            control=true;
        }
    }
    
    @FXML protected void onActionEnter(){
        Map<String,Integer> mapUsers=Users.getMap();
        String passwd;
        int idUser=-1;
        
        if(mapUsers.get(fieldUser.getText())!=null){
            idUser=mapUsers.get(fieldUser.getText());
            user.getFromDB(idUser);
            config.setIdUser(idUser);
            passwd=fieldPasswd.getText();
            try {
                System.out.println(user.getPasswd().getHashed());
                if(Password.checkPassword(passwd,user.getPasswd().getHashed())){
                    labelInfo.setText("Password is correct");
                    control=true;
                }else{
                    labelInfo.setText("Password is incorrect");
                }
            } catch (Exception ex) {
                Logger.getLogger(ViewLoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            labelInfo.setText("User does not exist");
        }
        
        if(control){
            labelInfo.setText("Next Window");
        }
        
        config.setReminder(checkRemindme.isSelected());
        config.save();
    }
    
    @FXML protected void onActionCreate(){
        labelInfo.setText("Create New User");
        //InvoicingApp.setRoot("");
    }
    
    @FXML protected void onPressedSeePW(){
        textFieldPW.setText(fieldPasswd.getText());
        fieldPasswd.setVisible(false);
        textFieldPW.setVisible(true);
        
    }
    @FXML protected void onReleaseSeePW(){
        textFieldPW.setVisible(false);
        fieldPasswd.setVisible(true);
    }
    
}
