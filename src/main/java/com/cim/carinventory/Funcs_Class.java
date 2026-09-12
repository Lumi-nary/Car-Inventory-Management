/*
 * Decompiled with CFR 0.152.
 */
package com.cim.carinventory;

import com.cim.carinventory.DB;
import com.cim.carinventory.carDetailsController;
import com.cim.carinventory.carListController;
import com.cim.carinventory.editCarController;
import com.cim.carinventory.mainMenuController;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.BoxBlur;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.kordamp.ikonli.javafx.FontIcon;

public class Funcs_Class {
    private static String username;
    private static String userState;

    public void stageSwitcher(FontIcon FontBTN, Button BTN, String fileName, Class<?> targetControllerClass, boolean overLay, String extraState) throws IOException {
        Stage stage = BTN == null ? (Stage)FontBTN.getScene().getWindow() : (Stage)BTN.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource(fileName));
        Parent root = (Parent)loader.load();
        Object targetController = loader.getController();
        if (targetControllerClass.isInstance(targetController)) {
            if (targetController instanceof mainMenuController) {
                mainMenuController mainMenu = (mainMenuController)targetController;
                mainMenu.userState(username, userState);
            } else if (targetController instanceof carListController) {
                carListController carList = (carListController)targetController;
                carList.userState(userState);
            } else if (targetController instanceof carDetailsController) {
                carDetailsController carDetails = (carDetailsController)targetController;
                carDetails.setCarDetails(extraState);
            } else if (targetController instanceof editCarController) {
                editCarController editCar = (editCarController)targetController;
                editCar.setCarDetails(extraState);
            }
            loader.setController(targetController);
        }
        if (overLay) {
            Stage overlayStage = new Stage();
            overlayStage.initOwner(stage);
            overlayStage.initStyle(StageStyle.UNDECORATED);
            overlayStage.setScene(new Scene(root));
            stage.getScene().getRoot().setDisable(true);
            stage.getScene().getRoot().setEffect(new BoxBlur());
            stage.getScene().getRoot().setOpacity(0.7);
            overlayStage.centerOnScreen();
            overlayStage.show();
        } else {
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
        }
    }

    public void switchStageAndRefreshTable(Stage currentStage, String fxmlFile, Class<?> targetControllerClass, boolean refreshTable) {
        try {
            Stage parentStage = (Stage)currentStage.getOwner();
            if (parentStage != null) {
                parentStage.getScene().getRoot().setDisable(false);
                parentStage.getScene().getRoot().setEffect(null);
                parentStage.getScene().getRoot().setOpacity(1.0);
                FXMLLoader loader = new FXMLLoader(Funcs_Class.class.getResource(fxmlFile));
                Parent root = (Parent)loader.load();
                Object targetController = loader.getController();
                if (targetControllerClass.isInstance(targetController) && refreshTable) {
                    Method refreshTableMethod = targetControllerClass.getDeclaredMethod("refreshTable", new Class[0]);
                    refreshTableMethod.invoke(targetController, new Object[0]);
                    if (targetController instanceof carListController) {
                        carListController carList = (carListController)targetController;
                        carList.userState(userState);
                    }
                }
                Scene scene = new Scene(root);
                parentStage.setScene(scene);
                parentStage.centerOnScreen();
            }
        }
        catch (IOException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void alertDialog(String title, String content, Alert.AlertType alertType) {
        String css = Objects.requireNonNull(this.getClass().getResource("cim-style.css")).toExternalForm();
        Alert alert = new Alert(alertType);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.getDialogPane().getStylesheets().add(css);
        alert.getDialogPane().getStyleClass().add("alert-dialog-pane");
        alert.getDialogPane().setMinHeight(Double.NEGATIVE_INFINITY);
        alert.showAndWait();
    }

    public boolean showConfirmationDialog(String title, String message) {
        String css = Objects.requireNonNull(this.getClass().getResource("cim-style.css")).toExternalForm();
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.initStyle(StageStyle.UNDECORATED);
        confirmationDialog.setTitle(title);
        confirmationDialog.setHeaderText(null);
        confirmationDialog.setContentText(message);
        confirmationDialog.getDialogPane().getStylesheets().add(css);
        confirmationDialog.getDialogPane().getStyleClass().add("alert-dialog-pane");
        ButtonType buttonYes = new ButtonType("Yes");
        ButtonType buttonNo = new ButtonType("No");
        confirmationDialog.getButtonTypes().setAll((ButtonType[])new ButtonType[]{buttonYes, buttonNo});
        Optional result = confirmationDialog.showAndWait();
        return result.isPresent() && result.get() == buttonYes;
    }

    public ResultSet getData(String query) {
        ResultSet rs = null;
        try {
            PreparedStatement ps = DB.getConnection().prepareStatement(query);
            rs = ps.executeQuery();
        }
        catch (DB.SQLNotInstalledException | SQLException ex) {
            Logger.getLogger(Funcs_Class.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Funcs_Class.username = username;
    }

    public static String getUserState() {
        return userState;
    }

    public static void setUserState(String userState) {
        Funcs_Class.userState = userState;
    }
}
