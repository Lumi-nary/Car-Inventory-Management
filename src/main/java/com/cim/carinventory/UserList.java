/*
 * Decompiled with CFR 0.152.
 */
package com.cim.carinventory;

import com.cim.carinventory.DB;
import com.cim.carinventory.Funcs_Class;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Alert;

public class UserList {
    Funcs_Class func = new Funcs_Class();
    private int id;
    private String name;
    private String role;
    private String password;
    private byte[] profile_image;

    public UserList() {
    }

    public UserList(int id, String name, String password, String role, byte[] profile_image) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
        this.profile_image = profile_image;
    }

    public ArrayList<UserList> userList(String query) {
        ArrayList<UserList> uList = new ArrayList<UserList>();
        try {
            if (query.equals("")) {
                query = "SELECT * FROM `users`";
            }
            ResultSet rs = this.func.getData(query);
            while (rs.next()) {
                UserList userList = new UserList(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getBytes(5));
                uList.add(userList);
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(UserList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return uList;
    }

    public boolean isIDExists(String id) {
        String query = "SELECT * FROM `users` WHERE `id` = '" + id + "'";
        ResultSet rs = this.func.getData(query);
        try {
            return rs.next();
        }
        catch (SQLException ex) {
            Logger.getLogger(UserList.class.getName()).log(Level.SEVERE, null, ex);
            return true;
        }
    }

    public boolean isNameExists(String name) {
        String query = "SELECT * FROM `users` WHERE `username` = '" + name + "'";
        ResultSet rs = this.func.getData(query);
        try {
            return rs.next();
        }
        catch (SQLException ex) {
            Logger.getLogger(UserList.class.getName()).log(Level.SEVERE, null, ex);
            return true;
        }
    }

    public void addUser(String username, String password, String roleString, byte[] image) {
        String insertQuery = "INSERT INTO users (username, password, usertype, profile_image) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = DB.getConnection().prepareStatement(insertQuery);){
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, roleString);
            ps.setBytes(4, image);
            if (ps.executeUpdate() != 0) {
                this.func.alertDialog("Add User", "User Added", Alert.AlertType.INFORMATION);
            } else {
                this.func.alertDialog("Add User", "User Not Added", Alert.AlertType.INFORMATION);
            }
        }
        catch (DB.SQLNotInstalledException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void editUser(int id, String username, String password, String usertype, byte[] image) {
        String originalUsername = this.getUserUsername(id);
        String updateQuery = image != null ? "UPDATE users SET username = ?, password = ?, usertype = ?, profile_image = ? WHERE id = ?" : "UPDATE users SET username = ?, password = ?, usertype = ? WHERE id = ?";
        try (PreparedStatement ps = DB.getConnection().prepareStatement(updateQuery);){
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, usertype);
            if (image != null) {
                ps.setBytes(4, image);
                ps.setInt(5, id);
            } else {
                ps.setInt(4, id);
            }
            if (ps.executeUpdate() != 0) {
                this.func.alertDialog("Edit User", "User Edited", Alert.AlertType.INFORMATION);
                String currentUser = Funcs_Class.getUsername();
                if (currentUser != null && currentUser.equals(originalUsername)) {
                    this.func.alertDialog("User Edited", "Your own account has been changed\nExiting Program", Alert.AlertType.INFORMATION);
                    Platform.exit();
                }
            } else {
                this.func.alertDialog("Edit User", "User Not Edited", Alert.AlertType.INFORMATION);
            }
        }
        catch (DB.SQLNotInstalledException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUser(int id) {
        String removeQuery = "DELETE FROM `users` WHERE `id` = ?";
        String originalUsername = this.getUserUsername(id);
        try (PreparedStatement ps = DB.getConnection().prepareStatement(removeQuery);){
            ps.setInt(1, id);
            if (ps.executeUpdate() != 0) {
                this.func.alertDialog("Delete User", "User Deleted", Alert.AlertType.INFORMATION);
                String currentUser = Funcs_Class.getUsername();
                if (currentUser != null && currentUser.equals(originalUsername)) {
                    this.func.alertDialog("User Deleted", "Your own account has been Deleted\nExiting Program", Alert.AlertType.INFORMATION);
                    Platform.exit();
                }
            } else {
                this.func.alertDialog("Delete User", "User Not Deleted", Alert.AlertType.INFORMATION);
            }
        }
        catch (DB.SQLNotInstalledException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] getUserProfileImage(String userName) {
        String query = "SELECT * FROM `users` WHERE `username` = ?";
        try (PreparedStatement ps = DB.getConnection().prepareStatement(query);){
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;
            byte[] byArray = rs.getBytes("profile_image");
            return byArray;
        }
        catch (DB.SQLNotInstalledException | SQLException e) {
            Logger.getLogger(UserList.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private String getUserUsername(int id) {
        String query = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement ps = DB.getConnection().prepareStatement(query);){
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery();){
                if (!rs.next()) return null;
                String string = rs.getString("username");
                return string;
            }
        }
        catch (DB.SQLNotInstalledException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public byte[] getProfile_image() {
        return this.profile_image;
    }

    public void setProfile_image(byte[] profile_image) {
        this.profile_image = profile_image;
    }
}
