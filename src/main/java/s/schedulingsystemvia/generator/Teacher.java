package s.schedulingsystemvia.generator;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Teacher {

    private String name;
    private String VIANumber;

    private ArrayList<Lesson> lessons;

    public Teacher(String name, String VIANumber) {
        this.name = name;
        this.VIANumber = VIANumber;
        lessons = new ArrayList<>();
    }

    public void addLesson(Lesson lesson){
        lessons.add(lesson);
    }
    public boolean isAvailable(LocalDateTime start, LocalDateTime end){
        for (Lesson times : lessons) {
            if(start.isAfter(times.getStart()) && start.isBefore(times.getEnd()) || end.isAfter(times.getStart()) && end.isBefore(times.getEnd()))
                return false;
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public String getVIANumber() {
        return VIANumber;
    }

    @Override
    public String toString() {
        return name;
    }
}
