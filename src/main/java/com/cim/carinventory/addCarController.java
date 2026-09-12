/*
 * Decompiled with CFR 0.152.
 */
package com.cim.carinventory;

import com.cim.carinventory.CarList;
import com.cim.carinventory.Funcs_Class;
import com.cim.carinventory.carListController;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class addCarController {
    @FXML
    private Button closeBTN;
    @FXML
    private Button saveBTN;
    @FXML
    private TextField vinField;
    @FXML
    private TextField brandField;
    @FXML
    private TextField modelField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ChoiceBox<String> transBox;
    @FXML
    private ChoiceBox<String> carTypesBox;
    @FXML
    private ImageView carImgView;
    private final String[] transmission = new String[]{"Automatic", "Manual", "CVT"};
    private final String[] carTypes = new String[]{"SUV", "Coupe", "Hatchback", "Sedan", "MPV", "Convertible", "Wagon", "Luxury", "Sports Car", "Supercar", "Muscle Car"};
    private File selectedFile;
    Funcs_Class func = new Funcs_Class();
    CarList carList = new CarList();

    @FXML
    public void initialize() {
        this.transBox.getItems().addAll((String[])this.transmission);
        this.carTypesBox.getItems().addAll((String[])this.carTypes);
    }

    @FXML
    protected void onCloseBTNClick() {
        Stage currentStage = (Stage)this.closeBTN.getScene().getWindow();
        currentStage.close();
        this.func.switchStageAndRefreshTable(currentStage, "carList-view.fxml", carListController.class, true);
    }

    @FXML
    protected void onUploadIMGClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        this.selectedFile = fileChooser.showOpenDialog(null);
        if (this.selectedFile != null) {
            try {
                Image image = new Image(this.selectedFile.toURI().toString());
                this.carImgView.setImage(image);
            }
            catch (Exception e) {
                this.func.alertDialog("Error Image", "Unable to load the Image", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void onSaveBTNClick() {
        String vin = this.vinField.getText();
        if (!this.verify()) {
            this.func.alertDialog("Fields are Empty", "Fields are Empty", Alert.AlertType.WARNING);
        } else if (this.carList.isVinExists(vin)) {
            this.func.alertDialog("VIN Already Exists", "VIN Already Exists", Alert.AlertType.WARNING);
        } else {
            try {
                String brand = this.brandField.getText();
                String model = this.modelField.getText();
                LocalDate date = (LocalDate)this.datePicker.getValue();
                String dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalDate currentDate = LocalDate.now();
                if (date.isAfter(currentDate)) {
                    this.func.alertDialog("Invalid Date", "The selected date must not be in the future.", Alert.AlertType.WARNING);
                    return;
                }
                String transString = this.transBox.getValue();
                String carTypeString = this.carTypesBox.getValue();
                byte[] image = Files.readAllBytes(this.selectedFile.toPath());
                this.carList.addCar(vin, brand, model, dateString, transString, carTypeString, image);
                this.onCloseBTNClick();
            }
            catch (IOException e) {
                this.func.alertDialog("Error", "Unable to read the selected image file", Alert.AlertType.ERROR);
            }
            catch (NullPointerException e) {
                this.func.alertDialog("No Image", "No Image. Upload picture.", Alert.AlertType.WARNING);
            }
        }
    }

    public boolean verify() {
        return !this.vinField.getText().isEmpty() && !this.brandField.getText().isEmpty() && !this.modelField.getText().isEmpty() && this.datePicker.getValue() != null && this.transBox.getValue() != null && this.carTypesBox.getValue() != null;
    }
}
