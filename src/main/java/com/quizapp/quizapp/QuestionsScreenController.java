package com.quizapp.quizapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import models.Question;
import models.Quiz;
import models.QuizResult;
import models.Student;
import org.controlsfx.control.Notifications;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionsScreenController {
    @FXML
    private Button next;
    @FXML
    private Label question;
    @FXML
    private Button submit;
    @FXML
    private ToggleGroup options;
    @FXML
    private RadioButton option3;
    @FXML
    private RadioButton option4;
    @FXML
    private RadioButton option1;
    @FXML
    private RadioButton option2;
    @FXML
    private Label title;

    private Quiz quiz;
    private List<Question> questionsList;
    private Question currentQuestion;
    private int currentQuestionIndex = 0;
    private Student student;
    private int rightAnswers = 0; // Initialize rightAnswers
    private Map<Question, String> studentAnswers = new HashMap<>();

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
        this.title.setText(this.quiz.getQuizTitle());
        this.getData();
        this.initialize(); // Call initialize after quiz is set
    }

    private void getData() {
        if (this.quiz != null) {
            this.questionsList = quiz.getQuestions();
            Collections.shuffle(this.questionsList);
            setNextQuestion();
        }
    }

    @FXML
    public void initialize() {
        this.hideSubmitButton();
        this.showNextQuestionButton();
        this.option1.setSelected(true);
    }

    @FXML
    public void submit(ActionEvent actionEvent) {
        // Create QuizResult object with right answers count
        QuizResult quizResult = new QuizResult(quiz, rightAnswers, student);

        // Save quiz result
        boolean result = quizResult.save(studentAnswers);

        // Display notification based on save result
        if (result) {
            Notifications.create().title("Quiz Result").text("Quiz Result Saved Successfully").showInformation();
        } else {
            Notifications.create().title("Quiz Result").text("Quiz Result Not Saved").showError();
        }
    }

    private void setNextQuestion() {
        if (this.currentQuestionIndex < this.questionsList.size()) {
            this.currentQuestion = this.questionsList.get(this.currentQuestionIndex);
            this.question.setText(this.currentQuestion.getQuestion());
            this.option1.setText(this.currentQuestion.getOption1());
            this.option2.setText(this.currentQuestion.getOption2());
            this.option3.setText(this.currentQuestion.getOption3());
            this.option4.setText(this.currentQuestion.getOption4());
            this.currentQuestionIndex++;
        } else {
            this.hideNextQuestionButton();
            this.showSubmitButton();
        }
    }

    private void hideNextQuestionButton() {
        this.next.setVisible(false);
    }

    private void showNextQuestionButton() {
        this.next.setVisible(true);
    }

    private void showSubmitButton() {
        this.submit.setVisible(true);
    }

    private void hideSubmitButton() {
        this.submit.setVisible(false);
    }

    @FXML
    public void nextQuestions(ActionEvent actionEvent) {
        RadioButton selectedRadioButton = (RadioButton) options.getSelectedToggle();
        String selectedOption = selectedRadioButton.getText();
        studentAnswers.put(currentQuestion, selectedOption);

        String correctAnswer = currentQuestion.getCorrectOption(); // Assuming you have a method to get correct answer
        if (selectedOption.equalsIgnoreCase(correctAnswer)) {
            Notifications.create().title("Correct Answer").position(Pos.TOP_CENTER).text("You got it right!").showInformation();
            rightAnswers++;
        } else {
            Notifications.create().title("Wrong Answer").position(Pos.TOP_CENTER).text("You got it wrong!").showError();
        }

        // Check if there is a next question
        if (this.currentQuestionIndex < this.questionsList.size()) {
            this.setNextQuestion();
        } else {
            this.hideNextQuestionButton();
            this.showSubmitButton();
        }
    }
}
