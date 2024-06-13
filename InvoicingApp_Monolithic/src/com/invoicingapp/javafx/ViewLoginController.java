/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.config.Configuration;
import invoicingapp_monolithic.Users;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ViewLoginController implements Initializable {

    private Configuration config=Configuration.getConfiguration();
    
    @FXML private VBox vBoxRight;
    @FXML private TextField fieldUser;
    @FXML private PasswordField fieldPasswd;
    @FXML private CheckBox checkRemindme;
    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Users user=new Users();
        user.getFromDB(config.getIdUser());
        checkRemindme.setSelected(config.isReminder());
        
        if(config.isReminder()){
            fieldUser.setText(user.getUserName());
            fieldPasswd.setText(user.getPasswd().getHashed());
        }

        vBoxRight.setBackground(new Background(new BackgroundFill(Color.web("#FFF5E2"),CornerRadii.EMPTY,Insets.EMPTY)));
    }
    
    @FXML protected void onActionEnter(){
        Map<String,Integer> mapUsers=Users.getMap();
        
        config.setIdUser(mapUsers.get(fieldUser.getText()));
        config.setReminder(checkRemindme.isSelected());
        config.save();
    }
    
    @FXML protected void onActionCreate(){
        
    }
    
    @FXML protected void onPressedSeePW(){
        fieldPasswd.setVisible(true);
    }
    @FXML protected void onReleaseSeePW(){
        fieldPasswd.setVisible(false);
    }
    
}
