package s.schedulingsystemvia.models;

import javafx.beans.property.*;
import s.schedulingsystemvia.generator.Lesson;
import s.schedulingsystemvia.generator.Class;
import s.schedulingsystemvia.generator.Classroom;
import s.schedulingsystemvia.generator.Course;
import s.schedulingsystemvia.generator.Teacher;

import java.time.LocalDateTime;

public class LessonViewModel {

  private Lesson lesson;

  private ObjectProperty<Classroom> classroom;
  private ObjectProperty<LocalDateTime> startTime;
  private ObjectProperty<LocalDateTime> endTime;
  private ObjectProperty<Course> course;
  private ObjectProperty<Teacher[]> teachers;
  private ObjectProperty<Class> studentClass;
  private IntegerProperty semester;
  private StringProperty programme;

  public LessonViewModel(Lesson lesson){
    this.lesson = lesson;
    reset();
  }

  public Lesson getLesson() {
    return lesson;
  }

  public Classroom getClassroom() {
    return classroom.get();
  }
  public ObjectProperty<Classroom> classroomProperty() {
    return classroom;
  }

  public LocalDateTime getStartTime() {
    return startTime.get();
  }
  public ObjectProperty<LocalDateTime> startTimeProperty() {
    return startTime;
  }

  public LocalDateTime getEndTime() {
    return endTime.get();
  }
  public ObjectProperty<LocalDateTime> endTimeProperty() {
    return endTime;
  }

  public Course getCourse() {
    return course.get();
  }
  public ObjectProperty<Course> courseProperty() {
    return course;
  }

  public Teacher[] getTeachers() {
    return teachers.get();
  }
  public ObjectProperty<Teacher[]> teachersProperty() {
    return teachers;
  }

  public Class getStudentClass() {
    return studentClass.get();
  }
  public ObjectProperty<Class> studentClassProperty() {
    return studentClass;
  }

  public int getSemester() {
    return semester.get();
  }
  public IntegerProperty semesterProperty() {
    return semester;
  }

  public String getProgramme() {
    return programme.get();
  }
  public StringProperty programmeProperty() {
    return programme;
  }

  public void reset(){
    classroom = new SimpleObjectProperty<>(lesson.getClassroom());
    startTime = new SimpleObjectProperty<>(lesson.getStart());
    endTime = new SimpleObjectProperty<>(lesson.getEnd());
    course = new SimpleObjectProperty<>(lesson.getCourse());
    teachers = new SimpleObjectProperty<>(lesson.getTeachers());
    studentClass = new SimpleObjectProperty<>(lesson.getStudentClass());
    programme = new SimpleStringProperty(lesson.getStudentClass().getProgramme());
    semester = new SimpleIntegerProperty(lesson.getStudentClass().getSemester());
  }

}
