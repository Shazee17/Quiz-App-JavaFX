package com.quizapp.quizapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

public class HelloController {

    @FXML
    private TextField adminEmail;

    @FXML
    private PasswordField adminPassword;

    @FXML
    private TextField studentEmail;

    @FXML
    private PasswordField studentPassword;

    @FXML
    private Button adminLoginButton;

    @FXML
    private Button studentLoginButton;

    @FXML
    protected void loginAdmin() {
        String email = adminEmail.getText();
        String password = adminPassword.getText();

        if (email.equalsIgnoreCase(constants.AdminEmailPassword.EMAIL) && password.equalsIgnoreCase(constants.AdminEmailPassword.PASSWORD)) {
            try {
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AdminHomeScreenFXML.fxml")));
                Stage stage = (Stage) adminEmail.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setMaximized(true);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }

            System.out.println("Admin Login Successful");
        } else {
            System.out.println("Invalid email or password");
        }
    }


    @FXML
    protected void loginStudent() {
        String email = studentEmail.getText();
        String password = studentPassword.getText();

        System.out.println("Student Login Successful");

        // Implement student login logic here
        // Example:
        // if (isValidStudent(email, password)) {
        //     // Navigate to student home screen
        // } else {
        //     // Show error message
        // }
    }
}
