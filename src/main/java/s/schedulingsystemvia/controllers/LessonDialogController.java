package s.schedulingsystemvia.controllers;

import javafx.stage.Stage;
import s.schedulingsystemvia.application.ViewHandler;
import s.schedulingsystemvia.generator.*;
import s.schedulingsystemvia.database.Database;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import s.schedulingsystemvia.generator.Class;

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

    private Lesson lesson;
    private Stage stage;

    ViewHandler handler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(this);

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

        cancelBtn.setOnAction(e -> {
            ((Stage)cancelBtn.getScene().getWindow()).close();
        });
        addBtn.setOnAction(e -> {

            TimeTable timeTable = database.getTimeTable(aClass.getValue());

            Lesson lesson = new Lesson(
                    classroom.getValue(),
                    timeTable.getAClass(),
                    LocalDateTime.of(date.getValue(), LocalTime.of(Integer.parseInt(startHour.getValue()), Integer.parseInt(startMinute.getValue()))),
                    LocalDateTime.of(date.getValue(), LocalTime.of(Integer.parseInt(endHour.getValue()), Integer.parseInt(endMinute.getValue()))),
                    course.getValue(),
                    teacher.getItems().toArray(new Teacher[0])
            );

            timeTable.addLesson(lesson);
            stage = (Stage)addBtn.getScene().getWindow();
            stage.close();

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

    public void setLesson(Lesson lesson){
        this.lesson = lesson;

        addBtn.setText("Save");

        addBtn.setOnAction(e -> {
            Lesson editLesson = new Lesson(
                    classroom.getValue(),
                    this.lesson.getStudentClass(),
                    LocalDateTime.of(date.getValue(), LocalTime.of(Integer.parseInt(startHour.getValue()), Integer.parseInt(startMinute.getValue()))),
                    LocalDateTime.of(date.getValue(), LocalTime.of(Integer.parseInt(endHour.getValue()), Integer.parseInt(endMinute.getValue()))),
                    course.getValue(),
                    teacher.getItems().toArray(new Teacher[0])
            );
            lesson.setLesson(editLesson);
            ((Stage)(addBtn.getScene().getWindow())).close();
        });

        date.setValue(lesson.getStart().toLocalDate());
        int startH = lesson.getStart().getHour();
        startHour.getSelectionModel().select(String.valueOf((startH<10)?"0"+startH:startH));
        int startM = lesson.getStart().getMinute();
        startMinute.getSelectionModel().select(String.valueOf((startM<10)?"0"+startM:startM));
        int endH = lesson.getEnd().getHour();
        endHour.getSelectionModel().select(String.valueOf((endH<10)?"0"+endH:endH));
        int endM = lesson.getEnd().getMinute();
        endMinute.getSelectionModel().select(String.valueOf((endM<10)?"0"+endM:endM));
        programme.getSelectionModel().select(lesson.getStudentClass().getProgramme());
        semester.getSelectionModel().select(lesson.getStudentClass().getSemester());
        aClass.getSelectionModel().select(lesson.getStudentClass());
        teacher.getSelectionModel().select(lesson.getTeachers()[0]);
        course.getSelectionModel().select(lesson.getCourse());
        classroom.getSelectionModel().select(lesson.getClassroom());

    }

    public void setViewHandler(ViewHandler handler){
        this.handler = handler;
    }

}
