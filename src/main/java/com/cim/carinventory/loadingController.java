/*
 * Decompiled with CFR 0.152.
 */
package com.cim.carinventory;

import com.cim.carinventory.DB;
import com.cim.carinventory.Funcs_Class;
import java.io.IOException;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class loadingController {
    @FXML
    private ImageView loadingImage;
    static Funcs_Class func = new Funcs_Class();

    public void initialize() {
        Task<Void> loadingTask = new Task<Void>(){

            @Override
            protected Void call() throws Exception {
                Thread.sleep(9500L);
                if (!DB.isSQLInstalled()) {
                    throw new DB.SQLNotInstalledException("SQL is not installed.");
                }
                return null;
            }
        };
        loadingTask.setOnSucceeded(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(this.getClass().getResource("login-view.fxml"));
                Parent root = (Parent)loader.load();
                Scene loginScene = new Scene(root);
                Stage primaryStage = (Stage)this.loadingImage.getScene().getWindow();
                primaryStage.setScene(loginScene);
                primaryStage.centerOnScreen();
                primaryStage.show();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });
        loadingTask.setOnFailed(event -> {
            System.out.println("An error occurred during loading: " + loadingTask.getException().getMessage());
            if (loadingTask.getException() instanceof DB.SQLNotInstalledException) {
                String errorMessage = "SQL Database Xampp is not installed. Please install and Configure Xampp";
                func.alertDialog("SQL Not Installed", errorMessage, Alert.AlertType.ERROR);
                this.initialize();
            }
        });
        new Thread(loadingTask).start();
    }
}
