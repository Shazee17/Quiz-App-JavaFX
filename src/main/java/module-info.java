module com.quizapp.quizapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.management;
    requires org.controlsfx.controls;
    requires java.sql;
    requires java.desktop;


    opens com.quizapp.quizapp to javafx.fxml;
    opens models to javafx.base;

    exports com.quizapp.quizapp;
}