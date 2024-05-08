package models;

import java.sql.*;
import java.util.*;

public class Quiz {

    public static class MetaData {
        public static final String TABLE_NAME = "quiz";
        public static final String QUIZ_ID = "quiz_id";
        public static final String TITLE = "title";
    }

    private Integer quizId;
    private String quizTitle;

    public Quiz() {
    }

    public Quiz(String quizTitle) {
        this.quizTitle = quizTitle;
    }

    public void setQuizId(Integer quizId) {
        this.quizId = quizId;
    }

    public Integer getQuizId() {
        return quizId;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "quizId=" + quizId +
                ", quizTitle='" + quizTitle + '\'' +
                '}';
    }

    public static void createTable() {
        try {
            String raw = "CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s VARCHAR(50))";
            String query = String.format(raw, MetaData.TABLE_NAME, MetaData.QUIZ_ID, MetaData.TITLE);
            System.out.println(query);
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


    public int save() {
        try {
            String raw = "INSERT INTO %s (%s) VALUES (?)";
            String query = String.format(raw, MetaData.TABLE_NAME, MetaData.TITLE);
            System.out.println(query);
            String connectionUrl = "jdbc:sqlite:quiz.db";
            Class.forName("org.sqlite.JDBC");
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, quizTitle);
                int result = ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    return keys.getInt(1);
                }
                return -1; // Return -1 if no keys were generated
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return -1; // Return -1 in case of any exception
        }
    }

    public boolean save(ArrayList<Question> questions) {
        boolean flag = true;
        this.quizId = this.save();

        for (Question question : questions) {
            question.save();
        }

        return flag;
    }

    public static Map<Quiz, Integer> getAllWithQuestionCount() {
        Map<Quiz, Integer> quizzes = new HashMap<>();
        Quiz key = null;

        String query = String.
                format("SELECT %s.%s , %s  ," +
                                " COUNT(*) as question_count  " +

                                "FROM %s join %s on %s.%s = %s.%s GROUP BY %s.%s",
                        MetaData.TABLE_NAME,
                        MetaData.QUIZ_ID,
                        MetaData.TITLE,
                        MetaData.TABLE_NAME,
                        Question.MetaData.TABLE_NAME,
                        Question.MetaData.TABLE_NAME,
                        Question.MetaData.QUIZ_ID,
                        MetaData.TABLE_NAME,
                        MetaData.QUIZ_ID,
                        MetaData.TABLE_NAME,
                        MetaData.QUIZ_ID
                );
        String connectionUrl = "jdbc:sqlite:quiz.db";
        System.out.println(query);
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection connection = DriverManager.getConnection(connectionUrl)) {

                PreparedStatement ps = connection.prepareStatement(query);
                ResultSet result = ps.executeQuery();

                while (result.next()) {
                    Quiz temp = new Quiz();
                    temp.setQuizId(result.getInt(1));
                    temp.setQuizTitle(result.getString(2));
                    int count = result.getInt(3);
                    quizzes.put(temp, count);

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return quizzes;
    }

    public List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();

        String query = String.format(
                "SELECT %s, %s, %s, %s, %s, %s FROM %s WHERE %s = ?",
                Question.MetaData.QUESTION,
                Question.MetaData.OPTION1,
                Question.MetaData.OPTION2,
                Question.MetaData.OPTION3,
                Question.MetaData.OPTION4,
                Question.MetaData.CORRECT_OPTION,
                Question.MetaData.TABLE_NAME,
                Question.MetaData.QUIZ_ID
        );

        String connectionUrl = "jdbc:sqlite:quiz.db";
        System.out.println(query);

        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection connection = DriverManager.getConnection(connectionUrl)) {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, this.quizId);
                ResultSet result = ps.executeQuery();

                while (result.next()) {
                    Question tempQuestion = new Question();
                    // Skip setting Question ID
                    tempQuestion.setQuestion(result.getString(1));
                    tempQuestion.setOption1(result.getString(2));
                    tempQuestion.setOption2(result.getString(3));
                    tempQuestion.setOption3(result.getString(4));
                    tempQuestion.setOption4(result.getString(5));
                    tempQuestion.setCorrectOption(result.getString(6));
                    tempQuestion.setQuiz(this);
                    questions.add(tempQuestion);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return questions;
    }




    public static Map<Quiz, List<Question>> getAll() {
        Map<Quiz, List<Question>> quizzes = new HashMap<>();
        Quiz key = null;
        List<Question> questions = new ArrayList<Question>();


        String query = String.format(
                "SELECT %s.%s AS quiz_id, %s, %s.%s AS question_id, %s.%s AS question, %s.%s AS option1, %s.%s AS option2, %s.%s AS option3, %s.%s AS option4, %s.%s AS correct_option FROM %s JOIN %s ON %s.%s = %s.%s",
                MetaData.TABLE_NAME, MetaData.QUIZ_ID, MetaData.TITLE,
                Question.MetaData.TABLE_NAME, Question.MetaData.QUIZ_ID,
                Question.MetaData.TABLE_NAME, Question.MetaData.QUESTION,
                Question.MetaData.TABLE_NAME, Question.MetaData.OPTION1,
                Question.MetaData.TABLE_NAME, Question.MetaData.OPTION2,
                Question.MetaData.TABLE_NAME, Question.MetaData.OPTION3,
                Question.MetaData.TABLE_NAME, Question.MetaData.OPTION4,
                Question.MetaData.TABLE_NAME, Question.MetaData.CORRECT_OPTION,
                MetaData.TABLE_NAME, Question.MetaData.TABLE_NAME,
                Question.MetaData.TABLE_NAME, Question.MetaData.QUIZ_ID,
                MetaData.TABLE_NAME, MetaData.QUIZ_ID);

        String connectionUrl = "jdbc:sqlite:quiz.db";

        System.out.println(query);

        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    System.out.println("Quiz ID: " + resultSet.getInt("quiz_id") +
                            ", Title: " + resultSet.getString(MetaData.TITLE) +
                            ", Question ID: " + resultSet.getInt("question_id") +
                            ", Question: " + resultSet.getString("question") +
                            ", Option 1: " + resultSet.getString("option1") +
                            ", Option 2: " + resultSet.getString("option2") +
                            ", Option 3: " + resultSet.getString("option3") +
                            ", Option 4: " + resultSet.getString("option4") +
                            ", Correct Option: " + resultSet.getString("correct_option"));


                    Quiz temp = new Quiz();
                    temp.setQuizId(resultSet.getInt(1));
                    temp.setQuizTitle(resultSet.getString(2));

                    Question tempQ = new Question();
                    tempQ.setQuestionId(resultSet.getInt(3));
                    tempQ.setOption1(resultSet.getString(4));
                    tempQ.setOption2(resultSet.getString(5));
                    tempQ.setOption3(resultSet.getString(6));
                    tempQ.setOption4(resultSet.getString(7));
                    tempQ.setCorrectOption(resultSet.getString(8));


                    if (key != null && key.equals(temp)) {
                        quizzes.get(key).add(tempQ);
                    } else {
                        ArrayList<Question> value = new ArrayList<>();
                        value.add(tempQ);
                        quizzes.put(temp, value);
                    }
                    key = temp;


                }
            } catch (SQLException e) {
                System.out.println("Error fetching quizzes: " + e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC Driver not found.");
        }

        return quizzes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Quiz)) {
            return false;
        }

        if (this.quizId == ((Quiz) obj).quizId) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quizId, quizTitle);
    }
}
