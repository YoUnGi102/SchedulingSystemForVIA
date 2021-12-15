package s.schedulingsystemvia.models;

import javafx.beans.property.*;
import s.schedulingsystemvia.generator.Class;
import s.schedulingsystemvia.generator.Student;

import java.time.LocalDate;

import static s.schedulingsystemvia.generator.Student.*;

public class StudentViewModel {

  private StringProperty name;
  private StringProperty VIANumber;
  private ObjectProperty<LocalDate> birthday;
  private StringProperty programme;
  private IntegerProperty semester;
  private StringProperty studentClass;
  private Student student;

  public StudentViewModel(Student student){
    this.student = student;
    reset();
  }

  public Student getStudent() {
    return student;
  }

  public String getName() {
    return name.get();
  }

  public StringProperty nameProperty() {
    return name;
  }

  public String getVIANumber() {
    return VIANumber.get();
  }

  public StringProperty VIANumberProperty() {
    return VIANumber;
  }

  public LocalDate getBirthday() {
    return birthday.get();
  }

  public ObjectProperty<LocalDate> birthdayProperty() {
    return birthday;
  }

  public String getProgramme() {
    return programme.get();
  }

  public StringProperty programmeProperty() {
    return programme;
  }

  public int getSemester() {
    return semester.get();
  }

  public IntegerProperty semesterProperty() {
    return semester;
  }

  public String getStudentClass() {
    return studentClass.get();
  }

  public StringProperty studentClassProperty() {
    return studentClass;
  }

  public void reset(){
    name = new SimpleStringProperty(student.getName());
    VIANumber = new SimpleStringProperty(student.getVIANumber());
    birthday = new SimpleObjectProperty<>(student.getBirthday());
    programme = new SimpleStringProperty(student.getStudentClass().getProgramme());
    studentClass = new SimpleStringProperty(student.getStudentClass().getName());
    semester = new SimpleIntegerProperty(student.getStudentClass().getSemester());
  }

}
