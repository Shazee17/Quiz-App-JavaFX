package com.quizapp.quizapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import listeners.NewScreenListener;
import models.Quiz;
import models.Student;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class QuizListFXML
{
    @javafx.fxml.FXML
    private FlowPane quizListContainer;

    Map<Quiz, Integer> quizzes = null;
    private Set<Quiz> keys;

    private NewScreenListener newScreenListener;

    private Student student;

    public void setStudent(Student student) {
        this.student = student;
    }


    public void setNewScreenListener(NewScreenListener newScreenListener) {
        this.newScreenListener = newScreenListener;

    }

    public void setCards(){
        for (Quiz key : keys) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("QuizCardLayoutFXML.fxml"));

            try {
                Node node = loader.load();
                QuizCardLayoutFXML controller = loader.getController();
                controller.setQuiz(key);
                controller.setStudent(this.student);
                controller.setNoq(String.valueOf(quizzes.get(key)));
                controller.setNewScreenListener(this.newScreenListener);
                quizListContainer.getChildren().add(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @javafx.fxml.FXML
    public void initialize() {

        quizzes = Quiz.getAllWithQuestionCount();
         keys = quizzes.keySet();



    }

}