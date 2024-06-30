package com.invoicingapp.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ConfirmationDialog {

    public static void show(String message, Runnable onConfirmAction, Runnable onCancelAction) {
        try {
            FXMLLoader loader = new FXMLLoader(ConfirmationDialog.class.getResource("confirmationDialog.fxml"));
            Parent root = loader.load();
            
            ConfirmationDialogController controller = loader.getController();
            controller.setMessage(message);
            controller.setOnConfirmAction(onConfirmAction);
            controller.setOnCancelAction(onCancelAction);
            
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Confirmation");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
