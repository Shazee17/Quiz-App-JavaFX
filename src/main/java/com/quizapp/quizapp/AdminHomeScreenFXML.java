package com.quizapp.quizapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.Objects;

public class AdminHomeScreenFXML
{
    @javafx.fxml.FXML
    private Tab addStudentTab;
    @javafx.fxml.FXML
    private TabPane adminTabPane;
    @javafx.fxml.FXML
    private Tab addQuizTab;

    @javafx.fxml.FXML
    public void initialize() {
        try {
            Parent node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AddQuizFXML.fxml")));
            addQuizTab.setContent(node);

            Parent StudentTabNode = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AdminStudentTab.fxml")));
            addStudentTab.setContent(StudentTabNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
