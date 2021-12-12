package s.schedulingsystemvia.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import s.schedulingsystemvia.Lesson;
import s.schedulingsystemvia.ViewHandler;
import s.schedulingsystemvia.database.Database;
import s.schedulingsystemvia.generator.Class;
import s.schedulingsystemvia.generator.Classroom;
import s.schedulingsystemvia.generator.Course;
import s.schedulingsystemvia.generator.Teacher;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class TimeTableViewController implements Initializable {

    // TODO GENERATION FOR MULTIPLE CLASSES
    // TODO EDITING LESSONS
    // TODO ADDING LESSONS

    @FXML
    private GridPane gridPane;

    @FXML
    private ChoiceBox<String> programme;

    @FXML
    private ChoiceBox<Integer> semester;

    @FXML
    private ChoiceBox<Class> aClass;

    @FXML
    private ImageView arrowLeft, arrowRight;

    @FXML
    private Label weekLabel;

    private Class currentClass;
    private int currentWeek;
    private int currentYear;

    public TimeTableViewController() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        currentWeek = 50;
        currentYear = 2021;

        Database database = Database.getInstance();

        /*
        Region root = null;
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(ViewHandler.LESSON_DIALOG_CONTROLLER_PATH));
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

        }*/

        /*

        CalendarGenerator generator = new CalendarGenerator(
                database.getClassrooms().toArray(new Classroom[0]), database.getClasses().toArray(new Class[0]), 15
        );

        HashMap<Class, TimeTable> timeTableHashMap = generator.generateTimeTables();
        TimeTable classZ = timeTableHashMap.get(database.getClass("Software Technology Engineering", 1, "Z"));
        classZ.resetGridPane(gridPane, currentWeek);
        setWeek(currentWeek);

        */

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

    }

    public void setWeek(int week){
        weekLabel.setText("Week " + week);
    }

    public static Lesson getNewNode(){
        LocalDateTime start = LocalDateTime.of(2021, 12, 1, 8, 20);
        LocalDateTime end = LocalDateTime.of(2021, 12, 1, 11, 45);
        Teacher teacher = new Teacher("Stephen Andersen", "932322");
        Lesson node = new Lesson(new Classroom("C05.16", 0), start, end, new Course("Responsive Web Design", "RWD", 5, new Teacher[]{new Teacher("Line", "2355")}));
        node.setColor(Color.YELLOW);
        return node;
    }
}