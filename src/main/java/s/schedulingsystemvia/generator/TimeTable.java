package s.schedulingsystemvia.generator;

import javafx.scene.paint.Color;
import s.schedulingsystemvia.generator.Class;
import javafx.scene.layout.GridPane;
import s.schedulingsystemvia.generator.Lesson;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.IsoFields;
import java.util.*;

import static s.schedulingsystemvia.generator.CalendarGenerator.COLOR_COUNT;

public class TimeTable {

    public static final ZoneId ZONE_ID = ZoneId.of("Europe/Paris");

    private Class aClass;
    private List<Lesson> lessons;

    HashMap<Course, Color> colors;

    public TimeTable(Class aClass, List<Lesson> lessons) {
        this.aClass = aClass;
        this.lessons = lessons;
    }

    public TimeTable(Class aClass) {
        this.aClass = aClass;
        this.lessons = new ArrayList<>();
    }

    public void addLesson(Lesson lesson){
        lessons.add(lesson);
    }

    public void resetGridPane(GridPane pane, int week, int year){

        ArrayList<Integer> colorIDs = new ArrayList<>();
        Random rn = new Random();

        int i = 0;
        while (i < aClass.getCourses().length){
            int ID = rn.nextInt(COLOR_COUNT);
            if (!colorIDs.contains(ID)){
                colorIDs.add(ID);
                i++;
            }
        }

        for (int j = 0; j < aClass.getCourses().length; j++) {
            colors.put(aClass.getCourses()[j], CalendarGenerator.getColor(colorIDs.get(j)));
        }

        ArrayList<Lesson> lessonsInWeek = getLessonsInWeek(week);

        while(pane.getChildren().size() > 16)
            pane.getChildren().remove(16);

        for (Lesson lesson : lessonsInWeek) {
            lesson.setColor(colors.get(lesson.getCourse()));
            pane.add(lesson, getColumn(lesson.getStart()), getRow(lesson.getStart()), 1, getRowSpan(lesson.getStart(), lesson.getEnd()));
        }

    }

    public ArrayList<Lesson> getLessonsInWeek(int week){
        ArrayList<Lesson> lessonsInWeek = new ArrayList<>();
        for (Lesson lesson : lessons) {
            if(getWeek(lesson) == week)
                lessonsInWeek.add(lesson);
        }

        return lessonsInWeek;

    }

    public List<Lesson> getLessons() {
        return lessons;
    }
    public String getProgramme() {
        return aClass.getProgramme();
    }
    public int getSemester() {
        return aClass.getSemester();
    }
    public String getClassName(){
        return aClass.getName();
    }
    public Class getAClass() {
        return aClass;
    }

    public static int getWeek(Lesson lesson){
        return ZonedDateTime.of(lesson.getStart(), ZONE_ID).get( IsoFields.WEEK_OF_WEEK_BASED_YEAR);
    }
    public static int getColumn(LocalDateTime dateTime){
        return switch (dateTime.getDayOfWeek()){
            case MONDAY -> 1;
            case TUESDAY -> 2;
            case WEDNESDAY -> 3;
            case THURSDAY -> 4;
            case FRIDAY -> 5;
            case SATURDAY, SUNDAY -> -1;
        };
    }
    public static int getRowSpan(LocalDateTime start, LocalDateTime end){
        return ((end.getHour()*60+end.getMinute()) - (start.getHour()*60+start.getMinute()))/5;
    }
    public static int getRow(LocalDateTime dateTime){
        if(dateTime.getHour() < 8 || dateTime.getHour() >= 19)
            return -1;
        return (dateTime.getHour()-7)*12+dateTime.getMinute()/5;
    }

}
