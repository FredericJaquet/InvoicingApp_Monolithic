/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import com.invoicingapp.config.Configuration;
import invoicingapp_monolithic.Password;
import invoicingapp_monolithic.Users;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

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
    @FXML private HBox paneMain;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        textFieldPW.setVisible(false);
        user.getFromDB(config.getIdUser());
        
        if(config.isReminder()&&user.getUserName()!=null){
            fieldUser.setText(user.getUserName());
            fieldPasswd.setText("This is not a Password");
            control=true;
        }else{
            config.setReminder(false);
        }
        checkRemindme.setSelected(config.isReminder());
    }
    
    @FXML protected void onActionEnter(){
        Map<String,Integer> mapUsers=Users.getMap();
        String passwd;
        int idUser;
        
        if(mapUsers.get(fieldUser.getText())!=null){
            idUser=mapUsers.get(fieldUser.getText());
            user.getFromDB(idUser);
            config.setIdUser(idUser);
            passwd=fieldPasswd.getText();
            try {
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
            fieldPasswd.clear();
            checkRemindme.setSelected(false);
            control=false;
        }
        
        if(control){
            config.setReminder(checkRemindme.isSelected());
            config.save();
            
            Parent root=null;
            try {
                root = FXMLLoader.load(getClass().getResource("viewHome.fxml"));
            } catch (IOException ex) {
                Logger.getLogger(ViewLoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
            Scene scene=new Scene(root);
            Stage stageHome=new Stage();
            stageHome.getIcons().add(new Image(getClass().getResourceAsStream("../img/Icon.png"))); 
            stageHome.setScene(scene);
            stageHome.setMaximized(true);
            stageHome.show();
            
            Stage stageLogin = (Stage) paneMain.getScene().getWindow();
            stageLogin.close();
        }
    }
    
    @FXML protected void onActionCreate(){
        Parent root=null;
        try {
            root = FXMLLoader.load(getClass().getResource("viewCreateUser.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(ViewLoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scene scene=new Scene(root);
        Stage stage=new Stage();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("../img/Icon.png"))); 
        stage.setScene(scene);
        
        stage.setOnHiding(event -> {
                paneMain.setDisable(false);
            });
        
        stage.show();
        paneMain.setDisable(true);
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
