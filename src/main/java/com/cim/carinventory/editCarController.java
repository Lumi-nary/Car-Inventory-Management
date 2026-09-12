/*
 * Decompiled with CFR 0.152.
 */
package com.cim.carinventory;

import com.cim.carinventory.CarList;
import com.cim.carinventory.DB;
import com.cim.carinventory.Funcs_Class;
import com.cim.carinventory.carDetailsController;
import com.cim.carinventory.carListController;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
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

public class editCarController {
    @FXML
    private Button closeBTN;
    @FXML
    private Button saveBTN;
    @FXML
    private Button deleteBTN;
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
    private String vin;
    Funcs_Class func = new Funcs_Class();
    CarList carList = new CarList();

    @FXML
    public void initialize() {
        this.transBox.getItems().addAll((String[])this.transmission);
        this.carTypesBox.getItems().addAll((String[])this.carTypes);
    }

    public void setCarDetails(String vin) {
        try {
            this.vin = vin;
            CarList selectedCar = this.carList.getCarByVIN(vin);
            if (selectedCar != null) {
                this.vinField.setText(selectedCar.getVin());
                this.brandField.setText(selectedCar.getBrand());
                this.modelField.setText(selectedCar.getModel());
                LocalDate date = selectedCar.getDate_created().toLocalDate();
                this.datePicker.setValue(date);
                String transmission = selectedCar.getTransmission();
                this.transBox.setValue(transmission);
                String carType = selectedCar.getCar_type();
                this.carTypesBox.setValue(carType);
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
                byte[] image = null;
                if (this.selectedFile != null) {
                    image = Files.readAllBytes(this.selectedFile.toPath());
                }
                this.carList.editCar(vin, brand, model, dateString, transString, carTypeString, image);
                this.onCloseBTNClick();
            }
            catch (IOException e) {
                this.func.alertDialog("Error", "Unable to read the selected image file", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    protected void onDeleteBTNClick() {
        try {
            if (this.func.showConfirmationDialog("Confirmation", "Do you want to delete this?")) {
                this.carList.deleteCar(this.vin);
                Stage currentStage = (Stage)this.deleteBTN.getScene().getWindow();
                currentStage.close();
                this.func.switchStageAndRefreshTable(currentStage, "carList-view.fxml", carListController.class, true);
            }
        }
        catch (Exception ex) {
            this.func.alertDialog("Error Data", "Invalid VIN", Alert.AlertType.ERROR);
        }
    }

    public boolean verify() {
        return !this.vinField.getText().isEmpty() && !this.brandField.getText().isEmpty() && !this.modelField.getText().isEmpty() && this.datePicker.getValue() != null && this.transBox.getValue() != null && this.carTypesBox.getValue() != null;
    }
}
