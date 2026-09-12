/*
 * Decompiled with CFR 0.152.
 */
package com.cim.carinventory;

import com.cim.carinventory.DB;
import com.cim.carinventory.Funcs_Class;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;

public class CarList {
    Funcs_Class func = new Funcs_Class();
    private int id;
    private String vin;
    private String brand;
    private String model;
    private Date date_created;
    private String transmission;
    private String car_type;
    private byte[] car_image;

    public CarList() {
    }

    public CarList(int id, String vin, String brand, String model, Date date_created, String transmission, String car_type, byte[] car_image) {
        this.id = id;
        this.vin = vin;
        this.brand = brand;
        this.model = model;
        this.date_created = date_created;
        this.transmission = transmission;
        this.car_type = car_type;
        this.car_image = car_image;
    }

    public ArrayList<CarList> carList(String query) {
        ArrayList<CarList> cList = new ArrayList<CarList>();
        try {
            if (query.equals("")) {
                query = "SELECT * FROM `cars`";
            }
            ResultSet rs = this.func.getData(query);
            while (rs.next()) {
                CarList carList = new CarList(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDate(5), rs.getString(6), rs.getString(7), rs.getBytes(8));
                cList.add(carList);
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(CarList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cList;
    }

    public boolean isVinExists(String vin) {
        String query = "SELECT * FROM `cars` WHERE `vin` = '" + vin + "'";
        ResultSet rs = this.func.getData(query);
        try {
            return rs.next();
        }
        catch (SQLException ex) {
            Logger.getLogger(CarList.class.getName()).log(Level.SEVERE, null, ex);
            return true;
        }
    }

    public void addCar(String vin, String brand, String model, String dateString, String transString, String carTypeString, byte[] image) {
        String insertQuery = "INSERT INTO cars (vin, brand, model, date_created, transmission, car_type, car_image) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = DB.getConnection().prepareStatement(insertQuery);){
            ps.setString(1, vin);
            ps.setString(2, brand);
            ps.setString(3, model);
            ps.setString(4, dateString);
            ps.setString(5, transString);
            ps.setString(6, carTypeString);
            ps.setBytes(7, image);
            if (ps.executeUpdate() != 0) {
                this.func.alertDialog("Add Car", "Car Added", Alert.AlertType.INFORMATION);
            } else {
                this.func.alertDialog("Add Car", "Car Not Added", Alert.AlertType.INFORMATION);
            }
        }
        catch (DB.SQLNotInstalledException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void editCar(String vin, String brand, String model, String dateString, String transString, String carTypeString, byte[] image) {
        String updateQuery = image != null ? "UPDATE cars SET brand = ?, model = ?, date_created = ?, transmission = ?, car_type = ?, car_image = ? WHERE vin = ?" : "UPDATE cars SET brand = ?, model = ?, date_created = ?, transmission = ?, car_type = ? WHERE vin = ?";
        try (PreparedStatement ps = DB.getConnection().prepareStatement(updateQuery);){
            ps.setString(1, brand);
            ps.setString(2, model);
            ps.setString(3, dateString);
            ps.setString(4, transString);
            ps.setString(5, carTypeString);
            if (image != null) {
                ps.setBytes(6, image);
                ps.setString(7, vin);
            } else {
                ps.setString(6, vin);
            }
            if (ps.executeUpdate() != 0) {
                this.func.alertDialog("Edit Car", "Car Edited", Alert.AlertType.INFORMATION);
            } else {
                this.func.alertDialog("Edit Car", "Car Not Edited", Alert.AlertType.INFORMATION);
            }
        }
        catch (DB.SQLNotInstalledException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteCar(String vin) {
        String removeQuery = "DELETE FROM `cars` WHERE `vin` = ?";
        try (PreparedStatement ps = DB.getConnection().prepareStatement(removeQuery);){
            ps.setString(1, vin);
            if (ps.executeUpdate() != 0) {
                this.func.alertDialog("Delete Car", "Car Deleted", Alert.AlertType.INFORMATION);
            } else {
                this.func.alertDialog("Delete Car", "Car Not Deleted", Alert.AlertType.INFORMATION);
            }
        }
        catch (DB.SQLNotInstalledException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CarList getCarByVIN(String vin) throws SQLException, DB.SQLNotInstalledException {
        String query = "SELECT * FROM cars WHERE vin = ?";
        try (Connection connection = DB.getConnection();){
            CarList carList;
            block16: {
                PreparedStatement stmt;
                block14: {
                    CarList carList2;
                    block15: {
                        stmt = connection.prepareStatement(query);
                        try {
                            stmt.setString(1, vin);
                            ResultSet rs = stmt.executeQuery();
                            if (!rs.next()) break block14;
                            carList2 = new CarList(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDate(5), rs.getString(6), rs.getString(7), rs.getBytes(8));
                            if (stmt == null) break block15;
                        }
                        catch (Throwable throwable) {
                            if (stmt != null) {
                                try {
                                    stmt.close();
                                }
                                catch (Throwable throwable2) {
                                    throwable.addSuppressed(throwable2);
                                }
                            }
                            throw throwable;
                        }
                        stmt.close();
                    }
                    return carList2;
                }
                carList = null;
                if (stmt == null) break block16;
                stmt.close();
            }
            return carList;
        }
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVin() {
        return this.vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getBrand() {
        return this.brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Date getDate_created() {
        return this.date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public String getTransmission() {
        return this.transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getCar_type() {
        return this.car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public byte[] getCar_image() {
        return this.car_image;
    }

    public void setCar_image(byte[] car_image) {
        this.car_image = car_image;
    }
}
