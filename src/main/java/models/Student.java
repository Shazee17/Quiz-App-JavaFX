package models;

import java.sql.*;
import java.util.ArrayList;

public class Student {
    private Integer studentId;
    private String firstName;
    private String lastName;
    private String phoneNo;
    private Character gender;
    private String email;
    private String password;

    private static class MetaData {
        public static final String TABLE_NAME = "students";
        public static final String STUDENT_ID = "student_id";
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";
        public static final String PHONE_NO = "phone_no";
        public static final String GENDER = "gender";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
    }

    public Student() {
    }

    public Student(String firstName, String lastName, String phoneNo, Character gender, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNo = phoneNo;
        this.gender = gender;
        this.email = email;
        this.password = password;
    }

    public Student(Integer id, String firstName, String lastName, String phoneNo, Character gender, String email, String password) {
        this.studentId = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNo = phoneNo;
        this.gender = gender;
        this.email = email;
        this.password = password;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static void createTable() {
        try {
            String raw = "CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "%s TEXT NOT NULL," +
                    "%s TEXT NOT NULL," +
                    "%s TEXT NOT NULL UNIQUE," +  // Unique constraint on email column
                    "%s TEXT NOT NULL," +
                    "%s TEXT NOT NULL," +
                    "%s TEXT NOT NULL)";
            String query = String.format(raw, MetaData.TABLE_NAME, MetaData.STUDENT_ID, MetaData.FIRST_NAME,
                    MetaData.LAST_NAME, MetaData.EMAIL, MetaData.PHONE_NO, MetaData.GENDER, MetaData.PASSWORD);

            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Establish connection
            String connectionUrl = "jdbc:sqlite:quiz.db";
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement statement = connection.prepareStatement(query)) {
                // Execute the query
                boolean executed = statement.execute();
                System.out.println("Table creation status: " + executed);
            } catch (SQLException e) {
                System.out.println("Error creating table: " + e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC Driver not found.");
        }
    }

    public static ArrayList<Student> getAll() {
        ArrayList<Student> students = new ArrayList<>();
        String query = String.format("SELECT * FROM %s", MetaData.TABLE_NAME);
        String connectionUrl = "jdbc:sqlite:quiz.db";
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    Student s = new Student();
                    s.setStudentId(resultSet.getInt(MetaData.STUDENT_ID));
                    s.setFirstName(resultSet.getString(MetaData.FIRST_NAME));
                    s.setLastName(resultSet.getString(MetaData.LAST_NAME));
                    s.setPhoneNo(resultSet.getString(MetaData.PHONE_NO));
                    s.setGender(resultSet.getString(MetaData.GENDER).charAt(0));
                    s.setEmail(resultSet.getString(MetaData.EMAIL));
                    s.setPassword(resultSet.getString(MetaData.PASSWORD));
                    students.add(s);
                }
            } catch (SQLException e) {
                System.out.println("Error fetching students: " + e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC Driver not found.");
        }
        return students;
    }

    public Student save() {
        String checkQuery = "SELECT COUNT(*) AS count FROM %s WHERE %s=?";
        String checkEmailQuery = String.format(checkQuery, MetaData.TABLE_NAME, MetaData.EMAIL);

        String raw = "INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?)";
        String insertQuery = String.format(raw, MetaData.TABLE_NAME, MetaData.FIRST_NAME, MetaData.LAST_NAME,
                MetaData.PHONE_NO, MetaData.GENDER, MetaData.EMAIL, MetaData.PASSWORD);

        String connectionUrl = "jdbc:sqlite:quiz.db";
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement checkStatement = connection.prepareStatement(checkEmailQuery);
                 PreparedStatement insertStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

                // Check if email already exists
                checkStatement.setString(1, this.email);
                ResultSet resultSet = checkStatement.executeQuery();
                if (resultSet.next() && resultSet.getInt("count") > 0) {
                    System.out.println("Error: Email already exists.");
                    return null;
                }

                // Insert new record
                insertStatement.setString(1, this.firstName);
                insertStatement.setString(2, this.lastName);
                insertStatement.setString(3, this.phoneNo);
                insertStatement.setString(4, String.valueOf(this.gender)); // Set gender at index 4
                insertStatement.setString(5, this.email);
                insertStatement.setString(6, this.password);

                insertStatement.executeUpdate();

                ResultSet generatedKeys = insertStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    this.studentId = generatedKeys.getInt(1);
                }

                System.out.println("Student saved successfully.");
                return this;
            } catch (SQLException e) {
                System.out.println("Error saving student: " + e.getMessage());
            }
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC Driver not found.");
            return null;
        }
    }
}
