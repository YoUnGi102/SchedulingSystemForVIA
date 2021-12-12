package s.schedulingsystemvia.generator;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;

public class Course {

    public static final double HOURS_PER_ECTS = 27.5;
    public static final double LESSONS_PERCENTAGE = 0.4; // EXPLAINED BELOW

    private String name;
    private String shortcut;
    private int ECTSPoints;
    private Teacher[] teacher;

    public Course(String name, String shortcut, int ECTSPoints, Teacher[] teacher) {
        this.name = name;
        this.shortcut = shortcut;
        this.ECTSPoints = ECTSPoints;
        this.teacher = teacher;
    }

    // 1 ECTS point is 27.5h of work
    // Lessons in school will be in our case 40%
    // This is taken from Class Z SDJ Lessons which apply for around 40% of 275 hours (10 ECTS)

    public Duration getDurationPerSemester(){
        return Duration.of((long) (ECTSPoints * HOURS_PER_ECTS * LESSONS_PERCENTAGE), ChronoUnit.HOURS);
    }

    public ArrayList<Duration> getLessonBlocks(Random rn, Duration timePerWeek){
        long lessonBlockUnit = timePerWeek.toMillis()/ECTSPoints;
        ArrayList<Duration> lessonBlocks = new ArrayList<>();

        int blocks = ECTSPoints;
        int days = 3;
        while(blocks > 0 && days > 1){
            int random = rn.nextInt(blocks+2)-2;
            lessonBlocks.add(Duration.of(lessonBlockUnit * random, ChronoUnit.MILLIS));
            blocks -= random;
            days--;
        }
        if(days > 0)
            lessonBlocks.add(Duration.of(lessonBlockUnit * blocks, ChronoUnit.MILLIS));

        return lessonBlocks;
    }

    public String getName() {
        return name;
    }

    public String getShortcut() {
        return shortcut;
    }

    public int getECTSPoints() {
        return ECTSPoints;
    }

    public Teacher[] getTeachers() {
        return teacher;
    }

    @Override
    public String toString() {
        return shortcut;
    }
}
