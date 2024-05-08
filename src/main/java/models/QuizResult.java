package models;

import javafx.geometry.Pos;
import org.controlsfx.control.Notifications;

import java.sql.*;
import java.util.Map;

public class QuizResult {

    private Integer quizResultId;
    private Student student;
    private Quiz quiz;
    private Integer rightAnswers;

    public static class MetaData {
        public static final String TABLE_NAME = "quiz_results";
        public static final String ID = "quiz_result_id";
        public static final String STUDENT_ID = "student_id";
        public static final String QUIZ_ID = "quiz_id";
        public static final String RIGHT_ANSWERS = "right_answers";
    }

    public QuizResult() {
    }

    public QuizResult(Quiz quiz, Integer quizResultId, Integer rightAnswers, Student student, Timestamp timestamp) {
        this.quiz = quiz;
        this.quizResultId = quizResultId;
        this.rightAnswers = rightAnswers;
        this.student = student;
    }

    public QuizResult(Quiz quiz, Integer rightAnswers, Student student) {
        this.quiz = quiz;
        this.rightAnswers = rightAnswers;
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public Integer getRightAnswers() {
        return rightAnswers;
    }


    public Integer getQuizResultId() {
        return quizResultId;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public void setQuizResultId(Integer quizResultId) {
        this.quizResultId = quizResultId;
    }

    public void setRightAnswers(Integer rightAnswers) {
        this.rightAnswers = rightAnswers;
    }

    public void setStudent(Student student) {
        this.student = student;
    }


    public static void createTable(){

        String raw = "CREATE table %s (\n" +
                "        %s Integer not null PRIMARY key AUTOINCREMENT,\n" +
                "        %s int not null,\n" +
                "        %s int not null ,\n" +
                "        %s int not null ,\n" +
                "        FOREIGN KEY (%s) REFERENCES %s(%s),\n" +
                "        FOREIGN KEY (%s) REFERENCES %s(%s)\n" +
                "        )";
        String query  = String.format(raw,
                MetaData.TABLE_NAME ,
                MetaData.ID,
                MetaData.STUDENT_ID,
                MetaData.QUIZ_ID,
                MetaData.RIGHT_ANSWERS,
                MetaData.STUDENT_ID,
                Quiz.MetaData.TABLE_NAME ,
                Quiz.MetaData.QUIZ_ID ,
                MetaData.STUDENT_ID ,
                Student.MetaData.TABLE_NAME ,
                Student.MetaData.STUDENT_ID
        );

        System.err.println(query);
        try{
            String connectionUrl = "jdbc:sqlite:quiz.db";
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionUrl);
            PreparedStatement ps = connection.prepareStatement(query);
            boolean b = ps.execute();
            System.out.println("Table creation status: " + b);
            System.out.println("models.QuizResults.createTable()");
            System.out.println(b);

        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public boolean save(Map<Question , String> userAnswers){
        String raw = "INSERT INTO %s (%s , %s , %s ) values (?, ? , ?)";
        String query  = String.format(raw,
                MetaData.TABLE_NAME ,
                MetaData.STUDENT_ID,
                MetaData.QUIZ_ID,
                MetaData.RIGHT_ANSWERS);

        try{
            String connectionUrl = "jdbc:sqlite:quiz.db";
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionUrl);
            PreparedStatement ps = connection.prepareStatement(query , Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1 , this.getStudent().getStudentId());
            ps.setInt(2 , this.getQuiz().getQuizId());
            ps.setInt(3 , this.getRightAnswers());
            int result =  ps.executeUpdate();

            // Get student and quiz information
            String studentName = this.getStudent().getFirstName().concat(" ").concat(this.getStudent().getLastName());
            String quizName = this.getQuiz().getQuizTitle();
            int totalQuestions = userAnswers.size();
            int rightAnswers = this.getRightAnswers();
            int wrongAnswers = totalQuestions - rightAnswers;

            // Collect wrong questions and their correct answers
            StringBuilder wrongQuestionsInfo = new StringBuilder();
            for (Map.Entry<Question, String> entry : userAnswers.entrySet()) {
                Question question = entry.getKey();
                String userAnswer = entry.getValue();
                if (!question.getCorrectOption().equals(userAnswer)) {
                    wrongQuestionsInfo.append(String.format("\n \n%s\nYour Answer: %s\nCorrect Answer: %s\n",
                            question.getQuestion(), userAnswer, question.getCorrectOption()));
                }
            }

            // Trigger notification after saving the result with the necessary information
            Notifications.create()
                    .title("Quiz Result")
                    .graphic(null)
                    .darkStyle()
                    .position(Pos.CENTER)
                    .hideAfter(javafx.util.Duration.seconds(50))
                    .text(String.format("Student: %s\nQuiz: %s\nRight Answers: %d\nWrong Answers: %d%s",
                            studentName, quizName, rightAnswers, wrongAnswers, wrongQuestionsInfo.toString()))
                    .showInformation();

            return true;
        } catch(Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        return false;
    }




}
