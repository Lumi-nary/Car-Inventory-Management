/*
 * Decompiled with CFR 0.152.
 */
package com.cim.carinventory;

import com.cim.carinventory.CarList;
import com.cim.carinventory.Funcs_Class;
import com.cim.carinventory.addCarController;
import com.cim.carinventory.carDetailsController;
import com.cim.carinventory.editCarController;
import com.cim.carinventory.mainMenuController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

public class carListController {
    @FXML
    private Button addBTN;
    @FXML
    private Button detailsBTN;
    @FXML
    private Button editdelBTN;
    @FXML
    private VBox vBoxPane;
    @FXML
    private FontIcon minimizeBTN;
    @FXML
    private Button homeBTN;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<CarList> carlistTable;
    @FXML
    private TableColumn<CarList, String> vinCol;
    @FXML
    private TableColumn<CarList, String> brandCol;
    @FXML
    private TableColumn<CarList, String> modelCol;
    @FXML
    private TableColumn<CarList, String> yearCol;
    @FXML
    private TableColumn<CarList, String> transmCol;
    @FXML
    private TableColumn<CarList, String> cartypeCol;
    Funcs_Class func = new Funcs_Class();
    CarList carList = new CarList();

    @FXML
    public void initialize() {
        this.loadTable("");
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

    public void userState(String userState) {
        if (Objects.equals(userState, "Manager")) {
            this.vBoxPane.getChildren().remove(this.addBTN);
            this.vBoxPane.getChildren().remove(this.editdelBTN);
            this.detailsBTN.setId("defBTNPrio");
            this.detailsBTN.setTextFill(Color.web("#ffffff"));
        }
    }

    @FXML
    protected void onHomeBTNClick() throws IOException {
        this.func.stageSwitcher(null, this.homeBTN, "mainMenu-view.fxml", mainMenuController.class, false, null);
    }

    @FXML
    protected void onSearchFieldKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String value = this.searchField.getText();
            String query = "SELECT * FROM `cars` WHERE `brand` LIKE '%" + value + "%' OR `model` LIKE '%" + value + "%' or `vin` LIKE '%" + value + "%'";
            this.loadTable(query);
        }
    }

    @FXML
    protected void onAddCarBTNClick() throws IOException {
        this.func.stageSwitcher(null, this.addBTN, "addCar-view.fxml", addCarController.class, true, null);
    }

    @FXML
    protected void onDetailCarBTNClick() throws IOException {
        CarList selectedCar = (CarList)this.carlistTable.getSelectionModel().getSelectedItem();
        if (selectedCar != null) {
            String vin = selectedCar.getVin();
            this.func.stageSwitcher(null, this.detailsBTN, "carDetails-view.fxml", carDetailsController.class, true, vin);
        }
    }

    @FXML
    protected void onEditDeleteCarBTNClick() throws IOException {
        CarList selectedCar = (CarList)this.carlistTable.getSelectionModel().getSelectedItem();
        if (selectedCar != null) {
            String vin = selectedCar.getVin();
            this.func.stageSwitcher(null, this.detailsBTN, "editCar-view.fxml", editCarController.class, true, vin);
        }
    }

    @FXML
    protected void onUnFocusedDetailSelection() {
        this.carlistTable.getSelectionModel().clearSelection();
        this.carlistTable.getParent().requestFocus();
    }

    private void loadTable(String query) {
        ArrayList<CarList> carLists = this.carList.carList(query);
        this.vinCol.setCellValueFactory(new PropertyValueFactory("vin"));
        this.brandCol.setCellValueFactory(new PropertyValueFactory("brand"));
        this.modelCol.setCellValueFactory(new PropertyValueFactory("model"));
        this.yearCol.setCellValueFactory(new PropertyValueFactory("date_created"));
        this.transmCol.setCellValueFactory(new PropertyValueFactory("transmission"));
        this.cartypeCol.setCellValueFactory(new PropertyValueFactory("car_type"));
        ObservableList<CarList> observableList = FXCollections.observableList(carLists);
        this.carlistTable.setItems(observableList);
    }

    public void refreshTable() {
        this.loadTable("");
    }
}
