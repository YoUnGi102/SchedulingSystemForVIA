package s.schedulingsystemvia.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import s.schedulingsystemvia.Lesson;
import s.schedulingsystemvia.database.Database;
import s.schedulingsystemvia.generator.Class;
import s.schedulingsystemvia.generator.Classroom;
import s.schedulingsystemvia.generator.Course;
import s.schedulingsystemvia.generator.Teacher;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

import static s.schedulingsystemvia.database.Database.*;

public class LessonDialogController implements Initializable {

    // TODO CHECK IF LESSON EXISTS

    @FXML
    private DatePicker date;
    @FXML
    private ChoiceBox<String> startHour, endHour, startMinute, endMinute, programme;
    @FXML
    private ChoiceBox<Course> course;
    @FXML
    private ChoiceBox<Teacher> teacher;
    @FXML
    private ChoiceBox<Classroom> classroom;
    @FXML
    private ChoiceBox<Class> aClass;
    @FXML
    private ChoiceBox<Integer> semester;

    @FXML
    private Button cancelBtn, addBtn;
    @FXML
    private Label messageLabel;

    private Database database;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        database = Database.getInstance();

        startHour.setItems(HOURS_LIST);
        endHour.setItems(HOURS_LIST);
        startMinute.setItems(MINUTES_LIST);
        endMinute.setItems(MINUTES_LIST);

        startHour.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            setClassrooms();
        });
        endHour.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            setClassrooms();
        });
        startMinute.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            setClassrooms();
        });
        endMinute.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            setClassrooms();
        });
        date.onActionProperty().addListener((observableValue, s, t1) -> {
            setClassrooms();
        });

        // TODO END TIME CANOT BE BEFORE START TIME
        startHour.onActionProperty().addListener((oldVal, actionEventEventHandler, newVal) -> setClassrooms());

        programme.setItems(database.getProgrammesList());
        programme.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            if(semester.getValue() != null){
                aClass.setItems(database.getClassesList(programme.getValue(), semester.getValue()));
            }
        });

        semester.setItems(Database.SEMESTER_LIST);
        semester.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            if(programme.getValue() != null){
                aClass.setItems(database.getClassesList(programme.getValue(), semester.getValue()));
            }
        });

        aClass.getSelectionModel().selectedItemProperty().addListener((observableValue, aClass, t1) -> course.setItems(FXCollections.observableArrayList((t1.getCourses()))));

        course.getSelectionModel().selectedItemProperty().addListener((observableValue, course, t1) -> {
            teacher.setItems(FXCollections.observableArrayList(t1.getTeachers()));
        });

        classroom.getSelectionModel().selectedItemProperty().addListener((observableValue, aClassroom, t1) -> {

        });

        addBtn.setOnAction(e -> {
            Lesson lesson = new Lesson(
                    classroom.getValue(),
                    LocalDateTime.of(date.getValue(), LocalTime.of(Integer.parseInt(startHour.getValue()), Integer.parseInt(startMinute.getValue()))),
                    LocalDateTime.of(date.getValue(), LocalTime.of(Integer.parseInt(endHour.getValue()), Integer.parseInt(endMinute.getValue()))),
                    course.getValue(),
                    teacher.getItems().toArray(new Teacher[0])
            );
            database.getTimeTable(aClass.getValue()).addLesson(lesson);
        });

        cancelBtn.setOnAction(e -> {
            ((Stage)cancelBtn.getScene().getWindow()).close();
        });

    }

    private void setClassrooms(){
        if(date.getValue() != null && startHour.getValue() != null && startMinute.getValue() != null && endHour.getValue() != null && endMinute.getValue() != null){
            LocalDateTime start = LocalDateTime.of(date.getValue(), LocalTime.of(Integer.parseInt(startHour.getValue()), Integer.parseInt(startMinute.getValue())));
            LocalDateTime end = LocalDateTime.of(date.getValue(), LocalTime.of(Integer.parseInt(endHour.getValue()), Integer.parseInt(endMinute.getValue())));
            classroom.setItems(database.getAvailableClassroom(start, end));
        }else{
            messageLabel.setText("Select date, start and end time to see available classes");
        }
    }



}
