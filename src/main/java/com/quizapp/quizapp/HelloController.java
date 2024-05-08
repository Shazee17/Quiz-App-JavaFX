package com.quizapp.quizapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Student;
import org.controlsfx.control.Notifications;

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
        System.out.println("controllers.AdminController.loginStudent");
        Student s = new Student(studentEmail.getText(), studentPassword.getText());

        try {
            s.login();
            System.out.println(s);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentMainScreenFXML.fxml"));
                Parent root = loader.load();

                StudentMainScreen controller = loader.getController();
                controller.setStudent(s);

                Stage stage = (Stage) studentEmail.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setMaximized(true);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        } catch (Exception e) {
            if (e instanceof exceptions.LoginException) {
                Notifications.create()
                        .title("Login Error")
                        .darkStyle().position(Pos.CENTER)
                        .text("Invalid email or password")
                        .showError();
            }
        }
    }
}
