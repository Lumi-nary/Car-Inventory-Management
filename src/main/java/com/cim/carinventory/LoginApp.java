/*
 * Decompiled with CFR 0.152.
 */
package com.cim.carinventory;

import java.io.IOException;
import java.util.Objects;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginApp
extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApp.class.getResource("loading-view.fxml"));
        Image icon = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("pictures/logo.png")));
        Scene scene = new Scene((Parent)fxmlLoader.load());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(icon);
        stage.setTitle("Car Inventory Management");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    public static void main(String[] args) {
        LoginApp.launch(new String[0]);
    }
}
