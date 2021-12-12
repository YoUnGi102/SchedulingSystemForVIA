package s.schedulingsystemvia.models;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import s.schedulingsystemvia.Lesson;
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

  public LessonViewModel(Lesson lesson){
    this.lesson = lesson;
    classroom = new SimpleObjectProperty<>(lesson.getClassroom());
    startTime = new SimpleObjectProperty<>(lesson.getStart());
    endTime = new SimpleObjectProperty<>(lesson.getEnd());
    course = new SimpleObjectProperty<>(lesson.getCourse());
    teachers = new SimpleObjectProperty<>(lesson.getTeachers());
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
}
