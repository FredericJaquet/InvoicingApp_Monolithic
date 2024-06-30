/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.invoicingapp.javafx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author frede
 */
public class ConfirmationDialogController implements Initializable {

    @FXML private Label lblMessage;
    @FXML private VBox paneConfirmation;

    private Runnable onConfirmAction,onCancelAction;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    @FXML protected void onClicConfirm(){
        if (onConfirmAction != null) {
            onConfirmAction.run();
        }
        closeWindow();
    }
    
    @FXML protected void onClicCancel(){
        if (onCancelAction != null) {
            onCancelAction.run();
        }
        closeWindow();
    }
    
    private void closeWindow() {
        Stage stage = (Stage) paneConfirmation.getScene().getWindow();
        stage.close();
    }

    public void setMessage(String message) {
        lblMessage.setText(message);
    }

    public void setOnConfirmAction(Runnable onConfirmAction) {
        this.onConfirmAction = onConfirmAction;
    }

    public void setOnCancelAction(Runnable onCancelAction) {
        this.onCancelAction = onCancelAction;
    }
    
}
