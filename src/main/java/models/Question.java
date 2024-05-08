package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Question {
    private Quiz quiz;
    private Integer questionId;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String correctOption;

    public static class MetaData {
        public static final String TABLE_NAME = "questions";
        public static final String QUIZ_ID = "quiz_id";
        public static final String QUESTION_ID = "questionId";
        public static final String QUESTION = "question";
        public static final String OPTION1 = "option1";
        public static final String OPTION2 = "option2";
        public static final String OPTION3 = "option3";
        public static final String OPTION4 = "option4";
        public static final String CORRECT_OPTION = "correct_option";
    }

    public Question() {
    }

    public Question(Quiz quiz, String question, String option1, String option2, String option3, String option4, String correctOption) {
        this.quiz = quiz;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.correctOption = correctOption;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public static void createTable() {
        String raw = "CREATE TABLE IF NOT EXISTS %s (" +
                "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s INTEGER, " +
                "%s VARCHAR(1000), " +
                "%s VARCHAR(1000), " +
                "%s VARCHAR(1000), " +
                "%s VARCHAR(1000), " +
                "%s VARCHAR(1000), " +
                "%s VARCHAR(1000), " +
                "FOREIGN KEY (%s) REFERENCES %s(%s)" +
                ")";
        String query = String.format(raw, MetaData.TABLE_NAME, MetaData.QUESTION_ID, MetaData.QUIZ_ID, MetaData.QUESTION, MetaData.OPTION1, MetaData.OPTION2, MetaData.OPTION3, MetaData.OPTION4, MetaData.CORRECT_OPTION, MetaData.QUIZ_ID, Quiz.MetaData.TABLE_NAME, Quiz.MetaData.QUIZ_ID);

        System.out.println(query);

        try {
            String connectionUrl = "jdbc:sqlite:quiz.db";
            Class.forName("org.sqlite.JDBC");
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement ps = connection.prepareStatement(query)) {
                boolean result = ps.execute();
                System.out.println(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    public void save(){
        String raw = "INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String query = String.format(raw, MetaData.TABLE_NAME, MetaData.QUIZ_ID, MetaData.QUESTION, MetaData.OPTION1, MetaData.OPTION2, MetaData.OPTION3, MetaData.OPTION4, MetaData.CORRECT_OPTION);

        System.out.println(query);

        try {
            String connectionUrl = "jdbc:sqlite:quiz.db";
            Class.forName("org.sqlite.JDBC");
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, quiz.getQuizId());
                ps.setString(2, question);
                ps.setString(3, option1);
                ps.setString(4, option2);
                ps.setString(5, option3);
                ps.setString(6, option4);
                ps.setString(7, correctOption);

                int result = ps.executeUpdate();
                System.out.println(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }




}
