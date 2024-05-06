package com.quizapp.quizapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import models.Question;
import models.Quiz;
import org.controlsfx.control.Notifications;

import java.util.*;

import static models.Quiz.getAll;

public class AddQuizFXML {

    @FXML
    private TreeView treeView;

    @FXML
    private RadioButton option1radio;
    @FXML
    private RadioButton option4radio;
    @FXML
    private Button addNextQuestion;
    @FXML
    private TextArea question;
    @FXML
    private RadioButton option3radio;
    @FXML
    private TextField option3;
    @FXML
    private Button submitQuiz;
    @FXML
    private TextField option4;
    @FXML
    private TextField option1;
    @FXML
    private TextField option2;
    @FXML
    private RadioButton option2radio;
    @FXML
    private TextField quizTitle;

    private ToggleGroup radioGroup;

    @FXML
    private Button setQuizTitleButton;

    //my Variable
    private Quiz quiz;
    private ArrayList<Question> questions = new ArrayList<>();

    @FXML
    public void initialize() {
        radioButtonSetup();
        renderTreeView();
    }

    private void renderTreeView(){
        Map<Quiz , List<Question>> data = Quiz.getAll();
        Set<Quiz> quizzes = data.keySet();

        TreeItem root = new TreeItem("Quizzes");
        for(Quiz q : quizzes){
            TreeItem quizTreeItem = new TreeItem(q.getQuizTitle());

            List<Question> questions = data.get(q);
            for(Question question : questions){
                String questionText = question.getQuestion() + "\n";
                questionText += "A: " + question.getOption1() + "\n";
                questionText += "B: " + question.getOption2() + "\n";
                questionText += "C: " + question.getOption3() + "\n";
                questionText += "D: " + question.getOption4() + "\n";
                questionText += "Ans: " + question.getCorrectOption();

                TreeItem questionTreeItem = new TreeItem(questionText);
                quizTreeItem.getChildren().add(questionTreeItem);
            }

            quizTreeItem.setExpanded(true);
            root.getChildren().add(quizTreeItem);
        }

        root.setExpanded(true);
        this.treeView.setRoot(root);
    }



    private void radioButtonSetup() {
        radioGroup = new ToggleGroup();
        option1radio.setToggleGroup(radioGroup);
        option2radio.setToggleGroup(radioGroup);
        option3radio.setToggleGroup(radioGroup);
        option4radio.setToggleGroup(radioGroup);
    }

    @FXML
    public void setQuizTitle(ActionEvent actionEvent) {
        String title = quizTitle.getText();
        if (title.trim().isEmpty()){
            System.out.println("Please enter a title for the quiz");
        } else {
            System.out.println("Quiz Title set successfully");
            System.out.println("Quiz Title: " + title);
            quizTitle.setEditable(false);
            this.quiz = new Quiz(title);
        }
    }

    @FXML
    public void addNextQuestion(ActionEvent actionEvent) {
        addQuestions();

    }

    private boolean addQuestions() {
        boolean valid = validateFields();
        Question question = new Question(); // Create a new Question object

        if (valid) {
            // Populate the question object
            question.setQuestion(this.question.getText().trim());
            question.setOption1(option1.getText().trim());
            question.setOption2(option2.getText().trim());
            question.setOption3(option3.getText().trim());
            question.setOption4(option4.getText().trim());

            // Determine correct option
            Toggle selectedRadio = radioGroup.getSelectedToggle();
            String correctOption = "";
            if (selectedRadio == option1radio) {
                correctOption = option1.getText().trim();
            } else if (selectedRadio == option2radio) {
                correctOption = option2.getText().trim();
            } else if (selectedRadio == option3radio) {
                correctOption = option3.getText().trim();
            } else if (selectedRadio == option4radio) {
                correctOption = option4.getText().trim();
            }
            question.setCorrectOption(correctOption);

            // Clear input fields
            this.question.clear();
            option1.clear();
            option2.clear();
            option3.clear();
            option4.clear();
            radioGroup.selectToggle(null);

            // Add question to the list
            questions.add(question);
            question.setQuiz(quiz);

            System.out.println("Question added successfully");
            System.out.println(questions);
            System.out.println(quiz);
        }
        return valid;
    }




    @FXML
    public void submitQuiz(ActionEvent actionEvent) {
        boolean flag = addQuestions();

        if (flag) {
            flag = quiz.save(questions);
            if (flag) {
                Notifications.create()
                        .title("Success").position(Pos.CENTER)
                        .darkStyle()
                        .text("Quiz added successfully")
                        .showInformation();
            } else {
                Notifications.create()
                        .title("Error").position(Pos.CENTER)
                        .darkStyle()
                        .text("Error adding quiz")
                        .showError();
            }
        }


    }

    private boolean validateFields() {

        if (quiz == null) {
            Notifications.create()
                    .title("Error").position(Pos.CENTER)
                    .darkStyle()
                    .text("Please set the quiz title")
                    .showError();
            return false;
        }

        String questionText = question.getText();
        String option1Text = option1.getText();
        String option2Text = option2.getText();
        String option3Text = option3.getText();
        String option4Text = option4.getText();
        Toggle selectedRadio = radioGroup.getSelectedToggle();

        if (questionText.trim().isEmpty() || option1Text.trim().isEmpty() || option2Text.trim().isEmpty() || option3Text.trim().isEmpty() || option4Text.trim().isEmpty()) {
            Notifications.create()
                    .title("Error").position(Pos.CENTER).darkStyle()
                    .text("Please fill all fields")
                    .showError();
            return false;
        } else if (selectedRadio == null) {
            Notifications.create()
                    .title("Error").position(Pos.CENTER).darkStyle()
                    .text("Please select the correct option")
                    .showError();
            return false;
        }
        return true;
    }
}
