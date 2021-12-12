package s.schedulingsystemvia;

import s.schedulingsystemvia.generator.Classroom;
import s.schedulingsystemvia.generator.Course;
import s.schedulingsystemvia.generator.Teacher;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import s.schedulingsystemvia.generator.Classroom;
import s.schedulingsystemvia.generator.Course;
import s.schedulingsystemvia.generator.Teacher;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Lesson extends AnchorPane {

    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm");

    private String room;

    private LocalDateTime start;
    private LocalDateTime end;

    private Course course;
    private Teacher[] teacher;
    private Classroom classroom;

    private Label courseLabel;
    private Label teacherLabel;
    private Label timeLabel;
    private Label roomLabel;

    public Lesson(Classroom classroom, LocalDateTime start, LocalDateTime end, Course course, Teacher[] teacher) {
        this.room = room;
        this.start = start;
        this.end = end;
        this.course = course;
        this.classroom = classroom;
        this.teacher = teacher;
        initialize();
    }

    public Lesson(Classroom classroom, LocalDateTime start, LocalDateTime end, Course course) {
        this.room = room;
        this.start = start;
        this.end = end;
        this.course = course;
        this.classroom = classroom;
        this.teacher = course.getTeachers();
        initialize();
    }

    private void initialize(){

        AnchorPane pane = this;
        Platform.runLater(() -> {

            courseLabel = new Label(course.getShortcut());
            courseLabel.setFont(new Font(15));

            // TODO IF MORE TEACHERS
            teacherLabel = new Label(teacher[0].getName());
            teacherLabel.setFont(new Font(10));

            timeLabel = new Label(timeFormatter.format(start) + " - " + timeFormatter.format(end));
            timeLabel.setFont(new Font(12));

            roomLabel = new Label(room);
            roomLabel.setFont(new Font(15));

            VBox courseAndTeacher = new VBox();
            courseAndTeacher.getChildren().addAll(courseLabel, teacherLabel);

            getChildren().addAll(courseAndTeacher, timeLabel, roomLabel);

            setTopAnchor(courseAndTeacher, 5.0);
            setLeftAnchor(courseAndTeacher, 5.0);

            setBottomAnchor(timeLabel, 5.0);
            setLeftAnchor(timeLabel, 5.0);

            setTopAnchor(roomLabel, 5.0);
            setRightAnchor(roomLabel, 5.0);

            pane.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

                Region root = null;
                try
                {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource(ViewHandler.LESSON_DIALOG_VIEW_PATH));
                    root = loader.load();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                if(root != null){

                    Stage stage = new Stage();
                    stage.setResizable(false);
                    stage.setScene(new Scene(root, 720, 480));

                    stage.showAndWait();
                }
            });

        });
    }

    public void reset(){
        courseLabel.setText(course.getShortcut());
        // TODO IF MORE TEACHERS
        teacherLabel.setText(teacher[0].getName());
        timeLabel.setText(timeFormatter.format(start) + " - " + timeFormatter.format(end));
        roomLabel.setText(room);
    }

    public void setColor(Color color){
        CornerRadii corn = new CornerRadii(10);
        Background background = new Background(new BackgroundFill(color, corn, Insets.EMPTY));
        setBackground(background);
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public Course getCourse() {
        return course;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public Teacher[] getTeachers() {
        return teacher;
    }
}

