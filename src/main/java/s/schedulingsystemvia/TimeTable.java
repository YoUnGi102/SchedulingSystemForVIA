package s.schedulingsystemvia;

import s.schedulingsystemvia.generator.Class;
import javafx.scene.layout.GridPane;

import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.List;

public class TimeTable {

    public static final ZoneId ZONE_ID = ZoneId.of("Europe/Paris");

    private Class aClass;
    private List<Lesson> lessons;

    public TimeTable(Class aClass, List<Lesson> lessons) {
        this.aClass = aClass;
        this.lessons = lessons;
    }

    public TimeTable(Class aClass) {
        this.aClass = aClass;
        this.lessons = new ArrayList<>();
    }


    public void addLesson(Lesson lesson){
        System.out.println(Constants.getColumn(lesson.getStart()) + " " + Constants.getRow(lesson.getStart()));
        lessons.add(lesson);
    }

    public void resetGridPane(GridPane pane, int week){

        ArrayList<Lesson> lessonsInWeek = getLessonsInWeek(week);

        while(pane.getChildren().size() > 16)
            pane.getChildren().remove(16);

        for (Lesson lesson : lessonsInWeek) {
            pane.add(lesson, Constants.getColumn(lesson.getStart()), Constants.getRow(lesson.getStart()), 1, Constants.getRowSpan(lesson.getStart(), lesson.getEnd()));
        }

    }

    public ArrayList<Lesson> getLessonsInWeek(int week){
        ArrayList<Lesson> lessonsInWeek = new ArrayList<>();
        for (Lesson lesson : lessons) {
            if(ZonedDateTime.of(lesson.getStart(), ZONE_ID).get( IsoFields.WEEK_OF_WEEK_BASED_YEAR) == week)
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
}
