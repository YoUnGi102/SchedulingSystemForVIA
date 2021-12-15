package s.schedulingsystemvia.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import s.schedulingsystemvia.generator.Lesson;
import s.schedulingsystemvia.generator.TimeTable;
import s.schedulingsystemvia.generator.Classroom;
import s.schedulingsystemvia.generator.Course;
import s.schedulingsystemvia.generator.Student;
import s.schedulingsystemvia.generator.Teacher;
import s.schedulingsystemvia.generator.Class;
import s.schedulingsystemvia.models.LessonViewModel;
import s.schedulingsystemvia.models.StudentViewModel;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static s.schedulingsystemvia.generator.Student.Gender;

public class Database {

    public static final ObservableList<String> HOURS_LIST = FXCollections.observableArrayList("8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18");
    public static final ObservableList<String> MINUTES_LIST = FXCollections.observableArrayList("00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55");
    public static final ObservableList<Integer> SEMESTER_LIST = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7);

    public static final DateTimeFormatter LESSON_TIME_FORMATTER = DateTimeFormatter.ofPattern("ee dd/MM/yyyy HH:mm");
    public static final DateTimeFormatter BIRTHDATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String DATABASE_PATH = "src/main/java/s/schedulingsystemvia/resources/database.xml";

    private static Database database;

    private List<Student> students;
    private List<Teacher> teachers;
    private List<Classroom> classrooms;
    private List<TimeTable> timeTables;
    private List<Course> courses;
    private List<Class> classes;

    private DocumentBuilderFactory factory;
    private DocumentBuilder builder;
    private Document document;
    private Transformer transformer;

    private Database(){
        openConnection();
        if(isConnectionEstablished()){
            readTeachersList();
            readClassroomsList();
            readCoursesList();
            readClassesList();
            readStudentsList();
            readTimeTableList();
        }
    }

    public static Database getInstance(){
        if(database == null){
            database = new Database();
        }
        return database;
    }
    private void openConnection(){

        try {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            document = builder.parse(DATABASE_PATH.replaceAll("[^\\x20-\\x7e]", ""));
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "no");
        } catch (TransformerConfigurationException | ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

    }
    private boolean isConnectionEstablished(){
        return factory != null && builder != null && document != null && transformer != null;
    }

    public void saveAll(){
        Document document = builder.newDocument();
        Element rootElement = document.createElement("database");

        saveTeachersList(document, rootElement);
        saveClassroomsList(document, rootElement);
        saveCoursesList(document, rootElement);
        saveClassesList(document, rootElement);
        saveTimeTableList(document, rootElement);
        saveStudentsList(document, rootElement);

        document.appendChild(rootElement);

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        try {
            transformer.transform(new DOMSource(document), new StreamResult(DATABASE_PATH));
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }

    private void readStudentsList(){
        if(isConnectionEstablished()){

            transformer.setOutputProperty(OutputKeys.INDENT, "no");
            students = new ArrayList<>();

            NodeList studentNodes = document.getElementsByTagName("student");
            for (int i = 0; i < studentNodes.getLength(); i++) {
                NodeList childNodes = studentNodes.item(i).getChildNodes();

                String name = null, VIANumber = null, programme = null, aClass = null;
                int semester = 0, ECTSPoints = 0;
                Gender gender = null;
                LocalDate birthday = null;

                for (int j = 0; j < childNodes.getLength(); j++) {
                    switch (childNodes.item(j).getNodeName()) {
                        case "name" -> name = childNodes.item(j).getTextContent();
                        case "birthday" -> birthday = LocalDate.parse(childNodes.item(j).getTextContent(), BIRTHDATE_FORMATTER);
                        case "gender" -> gender = Gender.getGender(childNodes.item(j).getTextContent());
                        case "programme" -> programme = childNodes.item(j).getTextContent();
                        case "className" -> aClass = childNodes.item(j).getTextContent();
                        case "semester" -> semester = Integer.parseInt(childNodes.item(j).getTextContent());
                        case "ECTSPoints" -> ECTSPoints = Integer.parseInt(childNodes.item(j).getTextContent());
                        case "VIANumber" -> VIANumber = childNodes.item(j).getTextContent();
                    }
                }
                students.add(new Student(name, birthday, gender, getClass(programme, semester, aClass), ECTSPoints, VIANumber));
            }

        }
    }
    public void saveStudentsList(Document saveDocument, Element databaseElement){
        for (Student student : students) {

            Element rootElement = saveDocument.createElement("student");

            Element subElement = saveDocument.createElement("name");
            subElement.appendChild(saveDocument.createTextNode(student.getName()));
            rootElement.appendChild(subElement);

            subElement = saveDocument.createElement("birthday");
            subElement.appendChild(saveDocument.createTextNode(BIRTHDATE_FORMATTER.format(student.getBirthday())));
            rootElement.appendChild(subElement);

            subElement = saveDocument.createElement("gender");
            subElement.appendChild(saveDocument.createTextNode(student.getGender().toString()));
            rootElement.appendChild(subElement);

            subElement = saveDocument.createElement("programme");
            subElement.appendChild(saveDocument.createTextNode(student.getProgramme()));
            rootElement.appendChild(subElement);

            subElement = saveDocument.createElement("className");
            subElement.appendChild(saveDocument.createTextNode(student.getStudentClass().getName()));
            rootElement.appendChild(subElement);

            subElement = saveDocument.createElement("semester");
            subElement.appendChild(saveDocument.createTextNode(String.valueOf(student.getStudentClass().getSemester())));
            rootElement.appendChild(subElement);

            subElement = saveDocument.createElement("ECTSPoints");
            subElement.appendChild(saveDocument.createTextNode(String.valueOf(student.getECTSPoints())));
            rootElement.appendChild(subElement);

            subElement = saveDocument.createElement("VIANumber");
            subElement.appendChild(saveDocument.createTextNode(student.getVIANumber()));
            rootElement.appendChild(subElement);

            databaseElement.appendChild(rootElement);
        }
    }
    public List<Student> getStudents(){
        return students;
    }
    public Student getStudent(String name, String VIANumber){
        for (Student student : students) {
            if(student.getName().equals(name) && student.getVIANumber().equals(VIANumber))
                return student;
        }
        return null;
    }
    public void addStudent(Student student){
        students.add(student);
        saveAll();
    }
    public void removeStudent(Student student){
        students.remove(student);
        database.saveAll();
    }

    private void readTeachersList(){
        if(isConnectionEstablished()){

            transformer.setOutputProperty(OutputKeys.INDENT, "no");
            teachers = new ArrayList<>();

            NodeList teachersNodes = document.getElementsByTagName("teacher");

            for (int i = 0; i < teachersNodes.getLength(); i++) {
                NodeList children = teachersNodes.item(i).getChildNodes();
                String name = null, VIANumber = null;
                for (int j = 0; j < children.getLength(); j++) {
                    switch (children.item(j).getNodeName()) {
                        case "name" -> name = children.item(j).getTextContent();
                        case "VIANumber" -> VIANumber = children.item(j).getTextContent();
                    }
                }
                teachers.add(new Teacher(name, VIANumber));
            }
        }
    }
    public void saveTeachersList(Document saveDocument, Element databaseElement){
        for (Teacher teacher : teachers) {

            Element rootElement = saveDocument.createElement("teacher");

            Element subElement = saveDocument.createElement("name");
            subElement.appendChild(saveDocument.createTextNode(teacher.getName()));
            rootElement.appendChild(subElement);

            subElement = saveDocument.createElement("VIANumber");
            subElement.appendChild(saveDocument.createTextNode(teacher.getVIANumber()));
            rootElement.appendChild(subElement);

            databaseElement.appendChild(rootElement);
        }

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
    }
    public List<Teacher> getTeachers() {
        return teachers;
    }
    public Teacher getTeacher(String name){
        for (Teacher teacher : teachers) {
            if(teacher.getName().equals(name))
                return teacher;
        }
        return null;
    }

    private void readClassroomsList(){
        if(isConnectionEstablished()){

            transformer.setOutputProperty(OutputKeys.INDENT, "no");
            classrooms = new ArrayList<>();

            NodeList classroomNodes = document.getElementsByTagName("classroom");

            for (int i = 0; i < classroomNodes.getLength(); i++) {
                NodeList children = classroomNodes.item(i).getChildNodes();
                String name = null;
                int capacity = 0;
                for (int j = 0; j < children.getLength(); j++) {
                    switch (children.item(j).getNodeName()){
                        case "name" -> name = children.item(j).getTextContent();
                        case "capacity" -> capacity = Integer.parseInt(children.item(j).getTextContent());
                    }
                }
                classrooms.add(new Classroom(name, capacity));
            }
        }
    }
    public void saveClassroomsList(Document saveDocument, Element databaseElement){
        for (Classroom classroom : classrooms) {

            Element rootElement = saveDocument.createElement("classroom");

            Element subElement = saveDocument.createElement("name");
            subElement.appendChild(saveDocument.createTextNode(classroom.getName()));
            rootElement.appendChild(subElement);

            subElement = saveDocument.createElement("capacity");
            subElement.appendChild(saveDocument.createTextNode(String.valueOf(classroom.getCapacity())));
            rootElement.appendChild(subElement);

            databaseElement.appendChild(rootElement);
        }
    }
    public List<Classroom> getClassrooms() {
        return classrooms;
    }
    public Classroom getClassroom(String name){
        for (Classroom classroom : classrooms) {
            if (classroom.getName().equals(name))
                return classroom;
        }
        return null;
    }

    private void readClassesList(){
        if(isConnectionEstablished()){

            transformer.setOutputProperty(OutputKeys.INDENT, "no");
            classes = new ArrayList<>();
            NodeList classesNodes = document.getElementsByTagName("class");

            for (int i = 0; i < classesNodes.getLength(); i++) {
                NodeList children = classesNodes.item(i).getChildNodes();
                String name = null, programme = null;
                int semester = 0;
                List<Course> coursesList = new ArrayList<>();
                for (int j = 0; j < children.getLength(); j++) {
                    switch (children.item(j).getNodeName()){
                        case "name" -> name = children.item(j).getTextContent();
                        case "semester" -> semester = Integer.parseInt(children.item(j).getTextContent());
                        case "programme" -> programme = children.item(j).getTextContent();
                        case "courseName" -> coursesList.add(getCourse(children.item(j).getTextContent()));
                    }
                }
                classes.add(new Class(programme, semester, name, coursesList.toArray(new Course[0])));
            }
        }
    }
    public void saveClassesList(Document saveDocument, Element databaseElement){

        for (Class aClass : classes) {

            Element rootElement = saveDocument.createElement("class");

            Element subElement = saveDocument.createElement("name");
            subElement.appendChild(saveDocument.createTextNode(aClass.getName()));
            rootElement.appendChild(subElement);

            subElement = saveDocument.createElement("semester");
            subElement.appendChild(saveDocument.createTextNode(String.valueOf(aClass.getSemester())));
            rootElement.appendChild(subElement);

            subElement = saveDocument.createElement("programme");
            subElement.appendChild(saveDocument.createTextNode(aClass.getProgramme()));
            rootElement.appendChild(subElement);

            for (Course course : aClass.getCourses()) {

                subElement = saveDocument.createElement("courseName");
                subElement.appendChild(saveDocument.createTextNode(course.getShortcut()));
                rootElement.appendChild(subElement);

            }

            databaseElement.appendChild(rootElement);
        }
    }
    public List<Class> getClasses() {
        return classes;
    }
    public Class getClass(String programme, int semester, String name){
        for (Class aClass : classes) {
            if (aClass.getName().equals(name))
                return aClass;
        }
        return null;
    }

    private void readCoursesList(){
        if(isConnectionEstablished()){

            transformer.setOutputProperty(OutputKeys.INDENT, "no");
            courses = new ArrayList<>();
            NodeList coursesNodes = document.getElementsByTagName("course");

            for (int i = 0; i < coursesNodes.getLength(); i++) {
                NodeList children = coursesNodes.item(i).getChildNodes();

                String name = null, shortcut = null;
                int ects = 0;
                List<Teacher> teachersList = new ArrayList<>();

                for (int j = 0; j < children.getLength(); j++) {
                    switch (children.item(j).getNodeName()){
                        case "name" -> name = children.item(j).getTextContent();
                        case "ects" -> ects = Integer.parseInt(children.item(j).getTextContent());
                        case "shortcut" -> shortcut = children.item(j).getTextContent();
                        case "teacherName" -> teachersList.add(getTeacher(children.item(j).getTextContent()));
                    }
                }

                courses.add(new Course(name, shortcut, ects, teachersList.toArray(new Teacher[0])));
            }
        }
    }
    public void saveCoursesList(Document saveDocument, Element databaseElement){

        for (Course course : courses) {

            Element rootElement = saveDocument.createElement("course");

            Element subElement = saveDocument.createElement("name");
            subElement.appendChild(saveDocument.createTextNode(course.getName()));
            rootElement.appendChild(subElement);

            subElement = saveDocument.createElement("shortcut");
            subElement.appendChild(saveDocument.createTextNode(course.getShortcut()));
            rootElement.appendChild(subElement);

            subElement = saveDocument.createElement("ects");
            subElement.appendChild(saveDocument.createTextNode(String.valueOf(course.getECTSPoints())));
            rootElement.appendChild(subElement);

            for (Teacher teacher : course.getTeachers()) {

                subElement = saveDocument.createElement("teacherName");
                subElement.appendChild(saveDocument.createTextNode(teacher.getName()));
                rootElement.appendChild(subElement);

            }

            databaseElement.appendChild(rootElement);
        }
    }
    public List<Course> getCourses() {
        return courses;
    }
    public Course getCourse(String shortcut){
        for (Course course : courses) {
            if(course.getShortcut().equals(shortcut))
                return course;
        }
        return null;
    }

    private void readTimeTableList(){
        if (isConnectionEstablished()){

            timeTables = new ArrayList<>();

            transformer.setOutputProperty(OutputKeys.INDENT, "no");

            NodeList rootList = document.getElementsByTagName("timeTable");
            for (int i = 0; i < rootList.getLength(); i++) {

                TimeTable timeTable;
                String programme = null, aClass = null;
                int semester = 0;
                ArrayList<Lesson> lessons = new ArrayList<>();

                Node timeTableNode = rootList.item(i);
                NodeList subNodes = timeTableNode.getChildNodes();

                for (int j = 0; j < subNodes.getLength(); j++)
                    switch (subNodes.item(j).getNodeName()) {
                        case "programme" -> programme = subNodes.item(j).getTextContent();
                        case "semester" -> semester = Integer.parseInt(subNodes.item(j).getTextContent());
                        case "className" -> aClass = subNodes.item(j).getTextContent();
                        case "lesson" -> {
                            LocalDateTime start = null, end = null;
                            String course = null, classroom = null, description = null;
                            List<Teacher> teachers = new ArrayList<>();
                            NodeList lessonNodes = subNodes.item(j).getChildNodes();
                            for (int k = 0; k < lessonNodes.getLength(); k++) {
                                switch (lessonNodes.item(k).getNodeName()) {
                                    case "start" -> start = LocalDateTime.parse(lessonNodes.item(k).getTextContent(), LESSON_TIME_FORMATTER);
                                    case "end" -> end = LocalDateTime.parse(lessonNodes.item(k).getTextContent(), LESSON_TIME_FORMATTER);
                                    case "courseName" -> course = lessonNodes.item(k).getTextContent();
                                    case "classroomName" -> classroom = lessonNodes.item(k).getTextContent();
                                    case "teacherName" -> teachers.add(getTeacher(lessonNodes.item(k).getTextContent()));
                                    //case "description" -> description = lessonNodes.item(k).getTextContent();
                                }
                            }
                            Lesson lesson = new Lesson(getClassroom(classroom), getClass(programme, semester, aClass), start, end, getCourse(course), teachers.toArray(new Teacher[0]));
                            lessons.add(lesson);
                            for (Teacher t : teachers) {
                                t.addLesson(lesson);
                            }
                        }
                    }
                    timeTables.add(new TimeTable(getClass(programme, semester, aClass), lessons));

                }

            }

        }
    private void saveTimeTableList(Document saveDocument, Element databaseElement){

        for (TimeTable timeTable : timeTables) {

            Element rootElement = saveDocument.createElement("timeTable");

            Element subElement = saveDocument.createElement("programme");
            subElement.appendChild(saveDocument.createTextNode(timeTable.getProgramme()));
            rootElement.appendChild(subElement);

            subElement = saveDocument.createElement("semester");
            subElement.appendChild(saveDocument.createTextNode(String.valueOf(timeTable.getSemester())));
            rootElement.appendChild(subElement);

            subElement = saveDocument.createElement("className");
            subElement.appendChild(saveDocument.createTextNode(timeTable.getClassName()));
            rootElement.appendChild(subElement);

            for (Lesson lesson : timeTable.getLessons()) {

                Element lessonRootElement = saveDocument.createElement("lesson");

                subElement = saveDocument.createElement("start");
                subElement.appendChild(saveDocument.createTextNode(LESSON_TIME_FORMATTER.format(lesson.getStart())));
                lessonRootElement.appendChild(subElement);

                subElement = saveDocument.createElement("end");
                subElement.appendChild(saveDocument.createTextNode(LESSON_TIME_FORMATTER.format(lesson.getEnd())));
                lessonRootElement.appendChild(subElement);

                subElement = saveDocument.createElement("courseName");
                subElement.appendChild(saveDocument.createTextNode(lesson.getCourse().getShortcut()));
                lessonRootElement.appendChild(subElement);

                subElement = saveDocument.createElement("classroomName");
                subElement.appendChild(saveDocument.createTextNode(lesson.getClassroom().getName()));
                lessonRootElement.appendChild(subElement);

                for (Teacher teacher : lesson.getTeachers()) {
                    subElement = saveDocument.createElement("teacherName");
                    subElement.appendChild(saveDocument.createTextNode(teacher.getName()));
                    lessonRootElement.appendChild(subElement);
                }

                rootElement.appendChild(lessonRootElement);

            }

            databaseElement.appendChild(rootElement);
        }

    }
    public List<TimeTable> getTimeTables() {
        return timeTables;
    }
    public TimeTable getTimeTable(Class aClass){
        System.out.println("TEST-CLASS:" + aClass.getProgramme() + " " + aClass.getSemester() + " " + aClass.getName());
        System.out.println("LIST-CLASS:" + aClass.getProgramme() + " " + aClass.getSemester() + " " + aClass.getName());
        for (TimeTable timeTable : timeTables) {
            if(timeTable.getAClass().getProgramme().equals(aClass.getProgramme())
            && timeTable.getAClass().getSemester() == aClass.getSemester()
            && timeTable.getAClass().getName().equals(aClass.getName()))
                return timeTable;
        }
        return null;
    }
    public void resetTimeTables(HashMap<Class, TimeTable> timeTableMap){
        timeTables.clear();
        for (Class c : timeTableMap.keySet()) {
            timeTables.add(timeTableMap.get(c));
        }
    }
    public void addLesson(TimeTable timeTable, Lesson lesson){
        timeTable.addLesson(lesson);
        saveAll();
    }
    public void removeLesson(Lesson lessonToRemove){
        for (TimeTable timeTable : timeTables) {
            if(timeTable.getLessons().remove(lessonToRemove)) {
                saveAll();
                return;
            }
        }
    }

    public ObservableList<String> getProgrammesList(){
        ObservableList<String> programmesList = FXCollections.observableArrayList();
        for (Class c : classes) {
            if(!programmesList.contains(c.getProgramme()))
                programmesList.add(c.getProgramme());
        }
        return programmesList;
    }
    public ObservableList<Class> getClassesList(String programme, int semester){
        ObservableList<Class> programmesList = FXCollections.observableArrayList();
        for (Class c : classes) {
            if(c.getProgramme().equals(programme) && c.getSemester() == semester)
                programmesList.add(c);
        }
        return programmesList;
    }

    public ObservableList<Classroom> getAvailableClassroom(LocalDateTime start, LocalDateTime end){
        ObservableList<Classroom> availableClassrooms = FXCollections.observableArrayList();
        for (Classroom classroom : classrooms) {
            if(classroom.isAvailable(start, end))
                availableClassrooms.add(classroom);
        }
        return availableClassrooms;
    }

    public ObservableList<StudentViewModel> getStudentViewModelList(){
        ObservableList<StudentViewModel> studentViewModelList = FXCollections.observableArrayList();
        for (Student student : students) {
            studentViewModelList.add(new StudentViewModel(student));
        }
        return studentViewModelList;
    }
    public ObservableList<StudentViewModel> getSearchedStudents(String name, String VIANumber, String programme, int semester, Class c){
        ObservableList<StudentViewModel> studentsSearched = FXCollections.observableArrayList();
        for (Student student : students) {
            if(programme != null && !student.getStudentClass().getProgramme().equals(programme))
                continue;
            if(semester != 0 && student.getStudentClass().getSemester() != semester)
                continue;
            if(c != null && !student.getStudentClass().equals(c))
                continue;
            if(name != null && !student.getName().contains(name))
                continue;
            if(VIANumber != null && !student.getVIANumber().contains(VIANumber))
                continue;
            studentsSearched.add(new StudentViewModel(student));
        }
        return studentsSearched;
    }

    public ObservableList<LessonViewModel> getLessonViewModelList(){
        ObservableList<LessonViewModel> lessons = FXCollections.observableArrayList();
        for (TimeTable timeTable : timeTables) {
            for (Lesson lesson : timeTable.getLessons()) {
                lessons.add(new LessonViewModel(lesson));
            }
        }
        return lessons;
    }
    public ObservableList<Lesson> getLessonsList(LocalDateTime start, LocalDateTime end){
        // IF BOTH == NULL then it is ALL
        ObservableList<Lesson> lessons = FXCollections.observableArrayList();
        for (TimeTable timeTable : timeTables) {
            for (Lesson lesson : timeTable.getLessons()) {
                if(start == null && end == null)
                    lessons.add(lesson);
                else if (start == null && lesson.getEnd().isBefore(end))
                    lessons.add(lesson);
                else if(end == null && lesson.getStart().isAfter(start))
                    lessons.add(lesson);
                else if(start != null && end != null && lesson.getStart().isAfter(start) && lesson.getEnd().isBefore(end))
                    lessons.add(lesson);
            }
        }
        return lessons;
    }
    public ObservableList<LessonViewModel> getSearchedLessons(String programme, int semester, Class c, String course, LocalDateTime start, LocalDateTime end){
        ObservableList<LessonViewModel> lessonsSearched = FXCollections.observableArrayList();
        for (Lesson lesson : getLessonsList(start, end)) {
            if(programme != null && !lesson.getStudentClass().getProgramme().equals(programme))
                continue;
            if(semester != 0 && lesson.getStudentClass().getSemester() != semester)
                continue;
            if(c != null && !lesson.getStudentClass().equals(c))
                continue;
            if(!course.equals("") && !lesson.getCourse().equals(getCourse(course)))
                continue;
            lessonsSearched.add(new LessonViewModel(lesson));
        }
        return lessonsSearched;
    }


}
