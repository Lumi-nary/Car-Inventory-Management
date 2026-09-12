/*
 * Decompiled with CFR 0.152.
 */
package com.cim.carinventory;

import com.cim.carinventory.CarList;
import com.cim.carinventory.DB;
import com.cim.carinventory.Funcs_Class;
import com.cim.carinventory.carListController;
import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class carDetailsController {
    @FXML
    private TextField vinField;
    @FXML
    private TextField brandField;
    @FXML
    private TextField modelField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField transField;
    @FXML
    private TextField carTypesField;
    @FXML
    private ImageView carImgView;
    @FXML
    private Button closeBTN;
    CarList carList = new CarList();
    Funcs_Class func = new Funcs_Class();

    @FXML
    protected void onCloseBTNClick() {
        Stage currentStage = (Stage)this.closeBTN.getScene().getWindow();
        currentStage.close();
        this.func.switchStageAndRefreshTable(currentStage, "carList-view.fxml", carListController.class, true);
    }

    public void setCarDetails(String vin) {
        try {
            CarList selectedCar = this.carList.getCarByVIN(vin);
            if (selectedCar != null) {
                this.vinField.setText(selectedCar.getVin());
                this.brandField.setText(selectedCar.getBrand());
                this.modelField.setText(selectedCar.getModel());
                this.yearField.setText(selectedCar.getDate_created().toString());
                this.transField.setText(selectedCar.getTransmission());
                this.carTypesField.setText(selectedCar.getCar_type());
                byte[] imageBytes = selectedCar.getCar_image();
                if (imageBytes != null) {
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
                    Image carImage = new Image(inputStream);
                    this.carImgView.setImage(carImage);
                }
            }
        }
        catch (SQLException e) {
            Logger.getLogger(carDetailsController.class.getName()).log(Level.SEVERE, null, e);
        }
        catch (DB.SQLNotInstalledException e) {
            throw new RuntimeException(e);
        }
    }
}
