package com.quizapp.quizapp;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import models.Student;
import org.controlsfx.control.Notifications;

import java.util.List;

public class AdminStudentTab {

    @javafx.fxml.FXML
    private TextField lastName;
    @javafx.fxml.FXML
    private TableColumn<Student, String> firstNameColumn;
    @javafx.fxml.FXML
    private TableColumn<Student, String> studentIdColumn;
    @javafx.fxml.FXML
    private TableColumn<Student, Character> genderColumn;
    @javafx.fxml.FXML
    private TableColumn<Student, String> phoneNoColumn;
    @javafx.fxml.FXML
    private TextField phoneNo;
    @javafx.fxml.FXML
    private TableView<Student> studentTable;
    @javafx.fxml.FXML
    private TextField firstName;
    @javafx.fxml.FXML
    private VBox formContainer;
    @javafx.fxml.FXML
    private TableColumn<Student, String> lastNameColumn;
    @javafx.fxml.FXML
    private Button saveButton;

    private ToggleGroup radioGroup;
    @javafx.fxml.FXML
    private RadioButton female;
    @javafx.fxml.FXML
    private RadioButton male;
    @javafx.fxml.FXML
    private PasswordField password;
    @javafx.fxml.FXML
    private TextField email;
    @javafx.fxml.FXML
    private TableColumn<Student, String> passwordColumn;
    @javafx.fxml.FXML
    private TableColumn<Student, String> emailColumn;

    @javafx.fxml.FXML
    public void initialize() {
        radioButtonSetup();
        renderTable();
    }

    private void renderTable() {
        List<Student> students = Student.getAll();
        studentTable.getItems().clear();

        this.studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        this.firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        this.lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        this.phoneNoColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
        this.genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        this.emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        this.passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        studentTable.getItems().addAll(students);
    }



    private void radioButtonSetup() {
        radioGroup = new ToggleGroup();
        male.setToggleGroup(radioGroup);
        female.setToggleGroup(radioGroup);
    }

    private void resetForm() {
        this.firstName.clear();
        this.lastName.clear();
        this.phoneNo.clear();
        this.email.clear();
        this.password.clear();
    }

    @javafx.fxml.FXML
    public void saveStudent(ActionEvent actionEvent) {
        System.out.println("Save Student");

        String firstName = this.firstName.getText().trim();
        String lastName = this.lastName.getText().trim();
        String phoneNo = this.phoneNo.getText().trim();
        String email = this.email.getText().trim();
        String password = this.password.getText().trim();
        Character charGender = 'M';
        RadioButton gender = (RadioButton) radioGroup.getSelectedToggle();

        if (gender != null && gender == female) {
            charGender = 'F';
        }

        String message = validateInput(firstName, lastName, phoneNo, email, password, gender);

        if (!message.isEmpty()) {
            Notifications.create()
                    .title("Error")
                    .darkStyle().position(Pos.CENTER)
                    .text(message)
                    .showError();
        } else {
            Student student = new Student(firstName, lastName, phoneNo, charGender, email, password);
            System.out.println(student);

            Student saved = student.save();

            System.out.println(student);
            if (saved != null) {
                Notifications.create()
                        .title("Success")
                        .darkStyle().position(Pos.CENTER)
                        .text("Student registered successfully")
                        .showInformation();

                resetForm();

                studentTable.getItems().add(0, saved);
            } else {
                Notifications.create()
                        .title("Error")
                        .darkStyle().position(Pos.CENTER)
                        .text("Error registering student")
                        .showError();
            }
        }

        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Phone No: " + phoneNo);
        System.out.println("Email: " + email);
        if (gender != null) {
            System.out.println("Gender: " + gender.getText());
        }
    }

    private String validateInput(String firstName, String lastName, String phoneNo, String email, String password, RadioButton gender) {
        if (firstName.trim().isEmpty() || firstName.length() < 4) {
            return "First Name should be more than 4 characters";
        }
        if (lastName.trim().isEmpty() || lastName.length() < 4) {
            return "Last Name should be more than 4 characters";
        }
        if (phoneNo.trim().isEmpty() || phoneNo.length() < 10) {
            return "Phone No should be more than 10 characters";
        }
        if (gender == null) {
            return "Please select the gender";
        }
        if (!isValidEmail(email)) {
            return "Invalid email format";
        }
        if (password.trim().isEmpty() || password.length() < 8) {
            return "Password should be more than 8 characters";
        }
        return "";
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
}
