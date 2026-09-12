/*
 * Decompiled with CFR 0.152.
 */
package com.cim.carinventory;

import com.cim.carinventory.Funcs_Class;
import com.cim.carinventory.LoginController;
import com.cim.carinventory.UserList;
import com.cim.carinventory.carListController;
import com.cim.carinventory.userManageController;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

public class mainMenuController {
    @FXML
    private FontIcon backMenu;
    @FXML
    private FontIcon showMenu;
    @FXML
    private AnchorPane sliderPane;
    @FXML
    private FontIcon minimizeBTN;
    @FXML
    private Label userNameLabel;
    @FXML
    private Button settingsBTN;
    @FXML
    private Button carListBTN;
    @FXML
    private ImageView profileIMGView;
    @FXML
    private Button logoutBTN;
    Funcs_Class func = new Funcs_Class();
    UserList userList = new UserList();
    String userStateStr;
    String userNameStr;

    public void userState(String userName, String userState) {
        byte[] imageBytes;
        this.userNameLabel.setText(userName);
        this.userNameStr = userName;
        this.userStateStr = userState;
        if (Objects.equals(userState, "Manager")) {
            this.settingsBTN.setDisable(true);
            this.settingsBTN.setOpacity(0.0);
        }
        if ((imageBytes = this.userList.getUserProfileImage(userName)) != null) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
            Image profileImage = new Image(inputStream);
            this.profileIMGView.setImage(profileImage);
        } else {
            this.profileIMGView.setImage(null);
        }
    }

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
    protected void onBackMenuBTNClick() {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(this.sliderPane);
        FadeTransition fade = new FadeTransition(Duration.seconds(0.4), this.showMenu);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
        this.showMenu.setDisable(false);
        slide.setByX(250.0);
        this.sliderPane.setTranslateX(0.0);
        slide.play();
    }

    @FXML
    protected void onShowMenuBTNClick() {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(this.sliderPane);
        FadeTransition fade = new FadeTransition(Duration.seconds(0.4), this.showMenu);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.play();
        this.showMenu.setDisable(true);
        slide.setByX(-250.0);
        this.sliderPane.setTranslateX(250.0);
        slide.play();
    }

    @FXML
    protected void onCarListBTNClick() throws IOException {
        this.func.stageSwitcher(null, this.carListBTN, "carList-view.fxml", carListController.class, false, null);
    }

    @FXML
    protected void onUserManageBTNClick() throws IOException {
        this.func.stageSwitcher(null, this.carListBTN, "userManage-view.fxml", userManageController.class, false, null);
    }

    @FXML
    protected void onLogOutBTNClick() throws IOException {
        this.func.stageSwitcher(null, this.logoutBTN, "login-view.fxml", LoginController.class, false, null);
    }
}
