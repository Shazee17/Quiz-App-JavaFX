package com.quizapp.quizapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import listeners.NewScreenListener;
import models.Quiz;
import models.Student;

public class QuizCardLayoutFXML
{
    @javafx.fxml.FXML
    private Label noq;
    @javafx.fxml.FXML
    private Button startButton;
    @javafx.fxml.FXML
    private Label title;

    private Student student;

    public void setStudent(Student student) {
        this.student = student;
    }

    private Quiz quiz;

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
        this.title.setText(this.quiz.getQuizTitle());
    }

    private NewScreenListener newScreenListener;

    public void setNewScreenListener(NewScreenListener newScreenListener) {
        this.newScreenListener = newScreenListener;
    }

    @javafx.fxml.FXML
    public void initialize() {

    }

    public void setNoq(String noq) {
        this.noq.setText(noq);
    }


    @javafx.fxml.FXML
    public void startQuiz(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("QuestionScreenFXML.fxml"));


        try
        {
            Node node = loader.load();
            QuestionsScreenController controller = loader.getController();
            controller.setStudent(this.student);
            controller.setQuiz(this.quiz);
            this.newScreenListener.changeScreen(node);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}