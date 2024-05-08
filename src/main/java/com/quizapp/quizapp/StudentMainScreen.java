package com.quizapp.quizapp;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import listeners.NewScreenListener;
import models.Student;

import java.io.IOException;

public class StudentMainScreen {
    @javafx.fxml.FXML
    private Button backButton;
    @javafx.fxml.FXML
    private StackPane stackPanel;


    private Student student;

    public void setStudent(Student student) throws IOException {
        this.student = student;
        addQuizListScreen();
    }

    @javafx.fxml.FXML
    public void initialize() throws IOException {

    }

    private void addScreenToStackPane(Node node) {
        this.stackPanel.getChildren().add(node);

    }

    private void addQuizListScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("QuizListFXML.fxml"));

        try {
            Node node = loader.load();
            QuizListFXML controller = loader.getController();
            controller.setStudent(this.student);
            controller.setNewScreenListener(new NewScreenListener() {
                @Override
                public void changeScreen(Node node) {
                    addScreenToStackPane(node);
                }

                @Override
                public void handle(Event event) {

                }
            });
            controller.setCards();
            stackPanel.getChildren().add(node);
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    @javafx.fxml.FXML
    public void back(ActionEvent actionEvent) {
        ObservableList<Node> nodes = this.stackPanel.getChildren();

        if (nodes.size() == 1) {
            return;
        }
        this.stackPanel.getChildren().remove(this.stackPanel.getChildren().size() - 1);
    }
}