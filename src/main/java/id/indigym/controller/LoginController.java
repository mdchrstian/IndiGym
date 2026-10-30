package id.indigym.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController{
    @FXML 
    private TextField usernameField;
    @FXML 
    private PasswordField passwordField;    
    @FXML 
    private Label messageLabel;
    @FXML 
    private void initialize(){
        messageLabel.setText("Silahkan Masuk Menggunakan Akun Petugas");
    }
    @FXML 
    private void handleLogin(){
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        //Validasi Username
        if(username.isEmpty()){
            messageLabel.setText("Username wajib diisi!");
            usernameField.requestFocus();
            return;
        }
        //Validasi Password
        if(password == null|| password.isBlank()){
            messageLabel.setText("Password Wajib Diisi!");
            passwordField.requestFocus();
            return ;
        }
        //Integrasi AuthService akan ditambahkan nanti//
        messageLabel.setText("Form Berhasil Divalidasi. Login Service Belum Dihubungkan");
    }
}