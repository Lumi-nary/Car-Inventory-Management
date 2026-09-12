/*
 * Decompiled with CFR 0.152.
 */
package com.cim.carinventory;

import com.cim.carinventory.Funcs_Class;
import com.cim.carinventory.UserList;
import com.cim.carinventory.mainMenuController;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Objects;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

public class userManageController {
    @FXML
    private Button homeBTN;
    @FXML
    private FontIcon minimizeBTN;
    @FXML
    private TextField searchField;
    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ChoiceBox<String> roleBox;
    @FXML
    private ImageView profileImgView;
    @FXML
    private Pane profilePane;
    @FXML
    private VBox fieldPane;
    @FXML
    private HBox buttonPane;
    @FXML
    private TableView<UserList> userListTable;
    @FXML
    private TableColumn<UserList, String> nameCol;
    @FXML
    private TableColumn<UserList, String> roleCol;
    private final String[] roleTypes = new String[]{"Admin", "Manager"};
    private File selectedFile;
    Funcs_Class func = new Funcs_Class();
    UserList userList = new UserList();
    private boolean isUploadPhotoClicked = false;

    @FXML
    public void initialize() {
        this.loadTable("");
        this.roleBox.getItems().addAll((String[])this.roleTypes);
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
    protected void onHomeBTNClick() throws IOException {
        this.func.stageSwitcher(null, this.homeBTN, "mainMenu-view.fxml", mainMenuController.class, false, null);
    }

    @FXML
    protected void onSearchFieldKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String value = this.searchField.getText();
            String query = "SELECT * FROM `users` WHERE `username` LIKE '%" + value + "%'";
            this.loadTable(query);
        }
    }

    @FXML
    protected void onUploadIMGClick() {
        this.isUploadPhotoClicked = true;
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        this.selectedFile = fileChooser.showOpenDialog(null);
        if (this.selectedFile != null) {
            try {
                Image image = new Image(this.selectedFile.toURI().toString());
                this.profileImgView.setImage(image);
            }
            catch (Exception e) {
                this.func.alertDialog("Error Image", "Unable to load the Image", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void onAddUserBTNClick() {
        String id = this.idField.getText();
        String username = this.nameField.getText();
        if (this.verify()) {
            this.func.alertDialog("Fields are Empty", "Fields are Empty", Alert.AlertType.WARNING);
        } else if (this.userList.isIDExists(id)) {
            this.func.alertDialog("ID Already Exists", "ID Already Exists", Alert.AlertType.WARNING);
        } else if (this.userList.isNameExists(username)) {
            this.func.alertDialog("Name Already Exists", "Name Already Exists", Alert.AlertType.WARNING);
        } else {
            try {
                String password = this.passwordField.getText();
                String roleString = this.roleBox.getValue();
                byte[] image = Files.readAllBytes(this.selectedFile.toPath());
                this.userList.addUser(username, password, roleString, image);
                this.refreshTable();
                this.clearFields();
            }
            catch (IOException e) {
                this.func.alertDialog("Error", "Unable to read the selected image file", Alert.AlertType.ERROR);
            }
            catch (NullPointerException e) {
                this.func.alertDialog("No Image", "No Image. Upload picture.", Alert.AlertType.WARNING);
            }
        }
    }

    @FXML
    protected void onEditUserBTNClick() {
        String username = this.nameField.getText();
        if (this.verify()) {
            this.func.alertDialog("Fields are Empty", "Fields are Empty", Alert.AlertType.WARNING);
        } else if (!username.equals(((UserList)this.userListTable.getSelectionModel().getSelectedItem()).getName()) && this.userList.isNameExists(username)) {
            this.func.alertDialog("Username already Exists", "This Username already Exists", Alert.AlertType.WARNING);
        } else {
            try {
                int id = Integer.parseInt(this.idField.getText());
                String password = this.passwordField.getText();
                String roleString = this.roleBox.getValue();
                byte[] image = null;
                if (this.selectedFile != null) {
                    image = Files.readAllBytes(this.selectedFile.toPath());
                }
                this.userList.editUser(id, username, password, roleString, image);
                this.refreshTable();
                this.clearFields();
            }
            catch (IOException e) {
                this.func.alertDialog("Error", "Unable to read the selected image file", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    protected void onDeleteUserBTNClick() {
        try {
            if (this.func.showConfirmationDialog("Confirmation", "Do you want to delete this?")) {
                int id = Integer.parseInt(this.idField.getText());
                this.userList.deleteUser(id);
                this.refreshTable();
                this.clearFields();
            }
        }
        catch (Exception ex) {
            this.func.alertDialog("Error Data", "Invalid ID", Alert.AlertType.ERROR);
        }
    }

    @FXML
    protected void onUserDetailSelection() {
        UserList selectedUser = (UserList)this.userListTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            this.idField.setText(String.valueOf(selectedUser.getId()));
            this.nameField.setText(selectedUser.getName());
            this.passwordField.setText(selectedUser.getPassword());
            this.roleBox.setValue(selectedUser.getRole());
            byte[] imageBytes = selectedUser.getProfile_image();
            if (imageBytes != null) {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
                Image carImage = new Image(inputStream);
                this.profileImgView.setImage(carImage);
            }
        }
    }

    @FXML
    protected void onUnFocusedDetailSelection(MouseEvent event) {
        Node eventSource = (Node)event.getTarget();
        if (!(this.isUploadPhotoClicked || eventSource.equals(this.profilePane) || eventSource.equals(this.fieldPane) || eventSource.equals(this.buttonPane) || this.userListTable.getSelectionModel().getSelectedItem() == null)) {
            this.userListTable.getSelectionModel().clearSelection();
            this.userListTable.getParent().requestFocus();
            this.clearFields();
        }
        this.isUploadPhotoClicked = false;
    }

    public boolean verify() {
        return this.nameField.getText().isEmpty() || this.passwordField.getText().isEmpty() || this.roleBox.getValue() == null;
    }

    public void clearFields() {
        Image defaultImage = new Image(Objects.requireNonNull(this.getClass().getResource("pictures/default_profile.png")).toExternalForm());
        this.idField.clear();
        this.nameField.clear();
        this.passwordField.clear();
        this.roleBox.setValue(null);
        this.profileImgView.setImage(defaultImage);
    }

    private void loadTable(String query) {
        ArrayList<UserList> userLists = this.userList.userList(query);
        this.nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        this.roleCol.setCellValueFactory(new PropertyValueFactory("role"));
        ObservableList<UserList> observableList = FXCollections.observableList(userLists);
        this.userListTable.setItems(observableList);
    }

    public void refreshTable() {
        this.loadTable("");
    }
}
