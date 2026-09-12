/*
 * Decompiled with CFR 0.152.
 */
package com.cim.carinventory;

import com.cim.carinventory.Funcs_Class;
import com.cim.carinventory.LoginController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class aboutController {
    @FXML
    private Button closeBTN;
    @FXML
    private ScrollPane scrollPane;
    Funcs_Class func = new Funcs_Class();

    @FXML
    protected void onCloseBTNClick() {
        Stage currentStage = (Stage)this.closeBTN.getScene().getWindow();
        currentStage.close();
        this.func.switchStageAndRefreshTable(currentStage, "login-view.fxml", LoginController.class, false);
    }
}
