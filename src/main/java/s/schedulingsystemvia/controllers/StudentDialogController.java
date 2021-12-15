package s.schedulingsystemvia.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import s.schedulingsystemvia.application.ViewHandler;
import s.schedulingsystemvia.database.Database;
import s.schedulingsystemvia.generator.Class;
import s.schedulingsystemvia.generator.Lesson;
import s.schedulingsystemvia.generator.Student;
import s.schedulingsystemvia.generator.Teacher;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

import static s.schedulingsystemvia.generator.Student.*;

public class StudentDialogController implements Initializable
{

  @FXML
  private Button addBtn, cancelBtn;

  @FXML private TextField name;
  @FXML private TextField ECTS;
  @FXML private TextField VIANumber;

  @FXML private DatePicker birthday;
  @FXML private ChoiceBox<Gender> gender;
  @FXML private ChoiceBox<String> programme;
  @FXML private ChoiceBox<Class> studentClass;
  @FXML private ChoiceBox<Integer> semester;

  @FXML private Label label;

  private ViewHandler handler;
  private Database database;
  private Student student;

  private Stage stage;

  public StudentDialogController() {}

  @FXML
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

    database = Database.getInstance();

    gender.setItems(FXCollections.observableArrayList(Gender.GENDERS_LIST));

    programme.setItems(database.getProgrammesList());
    programme.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
      if(semester.getValue() != null){
        studentClass.setItems(database.getClassesList(programme.getValue(), semester.getValue()));
      }
    });

    semester.setItems(Database.SEMESTER_LIST);
    semester.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
      if(programme.getValue() != null){
        studentClass.setItems(database.getClassesList(programme.getValue(), semester.getValue()));
      }
    });

    cancelBtn.onActionProperty().addListener(e-> {
      ((Stage)cancelBtn.getScene().getWindow()).close();
    });

  }

  public void setViewHandler(ViewHandler handler){
    this.handler = handler;
  }

  @FXML
  void add(ActionEvent event) {

    label.setText("");

    try{
        Integer.parseInt(this.VIANumber.getText());
    }catch (IllegalArgumentException e){
      label.setText("Field ECTS points must be a number");
      return;
    }

    try{
      if(this.VIANumber.getText().length() != 6)
        throw new IllegalArgumentException();
      else
        Integer.parseInt(this.VIANumber.getText());
    }catch (IllegalArgumentException e){
      label.setText("Field VIA Number must consist of 6 numbers");
      return;
    }

    database.addStudent(
        new Student(
                name.getText(),
                birthday.getValue(),
                gender.getValue(),
                studentClass.getValue(),
                Integer.parseInt(ECTS.getText()),
                VIANumber.getText()
        )
    );

    stage = (Stage)addBtn.getScene().getWindow();
    stage.close();

  }

  public void setStudent(Student student) {
    this.student = student;

    addBtn.setText("Save");

    addBtn.setOnAction(e -> {

      label.setText("");

      try{
        Integer.parseInt(this.VIANumber.getText());
      }catch (IllegalArgumentException ex){
        label.setText("Field ECTS points must be a number");
        return;
      }

      try{
        if(this.VIANumber.getText().length() != 6)
          throw new IllegalArgumentException();
        else
          Integer.parseInt(this.VIANumber.getText());
      }catch (IllegalArgumentException ex){
        label.setText("Field VIA Number must consist of 6 numbers");
        return;
      }

      Student editStudent = new Student(
              name.getText(),
              birthday.getValue(),
              gender.getValue(),
              studentClass.getValue(),
              Integer.parseInt(ECTS.getText()),
              VIANumber.getText()
      );

      student.setStudent(editStudent);
      ((Stage)(addBtn.getScene().getWindow())).close();
    });

    name.setText(student.getName());
    ECTS.setText(String.valueOf(student.getECTSPoints()));
    VIANumber.setText(student.getVIANumber());
    birthday.setValue(student.getBirthday());
    gender.setValue(student.getGender());
    programme.setValue(student.getStudentClass().getProgramme());
    studentClass.setValue(student.getStudentClass());
    semester.setValue(student.getSemester());

  }
}
