/*
 * Decompiled with CFR 0.152.
 */
package com.cim.carinventory;

import com.cim.carinventory.DB;
import com.cim.carinventory.Funcs_Class;
import com.cim.carinventory.aboutController;
import com.cim.carinventory.mainMenuController;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

public class LoginController {
    @FXML
    private FontIcon minimizeBTN;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginBTN;
    @FXML
    private Button aboutBTN;
    Funcs_Class funcs = new Funcs_Class();

    @FXML
    protected void onExitBTNClick() {
        Platform.exit();
    }

    @FXML
    protected void onMinimizeBTNClick() {
        Stage stage = (Stage)this.minimizeBTN.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    protected void onLoginBTNClick() {
        String username = this.usernameField.getText();
        String password = String.valueOf(this.passwordField.getText());
        String query = "SELECT * FROM `users` WHERE `username` = ? AND `password` = ?";
        if (username.trim().equals("") || password.trim().equals("")) {
            this.funcs.alertDialog("Fields Empty", "Fields are Empty", Alert.AlertType.WARNING);
        } else {
            try {
                PreparedStatement ps = DB.getConnection().prepareStatement(query);
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String userType = rs.getString("usertype");
                    Funcs_Class.setUsername(username);
                    Funcs_Class.setUserState(userType);
                    if (userType.equalsIgnoreCase("manager")) {
                        this.funcs.stageSwitcher(null, this.loginBTN, "mainMenu-view.fxml", mainMenuController.class, false, null);
                    } else {
                        this.funcs.stageSwitcher(null, this.loginBTN, "mainMenu-view.fxml", mainMenuController.class, false, null);
                    }
                } else {
                    this.funcs.alertDialog("Invalid User", "Wrong Password/Username", Alert.AlertType.WARNING);
                }
            }
            catch (SQLException e) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
            }
            catch (DB.SQLNotInstalledException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    protected void onLoginKeyPressed(KeyEvent key) {
        if (key.getCode() == KeyCode.ENTER) {
            this.onLoginBTNClick();
        }
    }

    @FXML
    protected void onAboutBTNClick() throws IOException {
        this.funcs.stageSwitcher(null, this.aboutBTN, "about-view.fxml", aboutController.class, true, null);
    }
}
