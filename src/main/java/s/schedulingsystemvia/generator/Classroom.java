package s.schedulingsystemvia.generator;

import s.schedulingsystemvia.Lesson;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;

public class Classroom {

    // TODO ROOMS CAN BE BOOKED FROM 8:00 to 16:15 FOR LESSONS
    // TODO DATE FOR LESSON START AND END HAS TO BE SAME

    // WHAT WE KNOW ABOUT THE TIME TABLE
    // LUNCH BREAK IS 11:50 to 12:45
    // LESSONS ARE GENERATED FROM 8:20 to 16:15
    // NOTE: MANUALLY ADDED LESSONS DO NOT HAVE TO FOLLOW THESE RULES


    private String name;
    private LinkedList<Lesson> scheduledLessons;
    private int capacity;

    public Classroom(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        scheduledLessons = new LinkedList<>();
    }

    public Classroom(String name, int capacity, LinkedList<Lesson> scheduledLessons) {
        this.name = name;
        this.scheduledLessons = scheduledLessons;
        this.capacity = capacity;
    }

    public boolean addLesson(Lesson lesson){
        for (Lesson times : scheduledLessons) {
            if(times.getEnd().isBefore(lesson.getStart())){
                int index = scheduledLessons.indexOf(times);
                if(index+1 < scheduledLessons.size() && scheduledLessons.get(index+1).getStart().isAfter(lesson.getEnd())) {
                    scheduledLessons.add(index + 1, lesson);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isEmpty(){
        return scheduledLessons.size() == 0;
    }

    public ArrayList<LocalDateTime[]> getAvailableTimes(){
        ArrayList<LocalDateTime[]> availableTimes = new ArrayList<>();
        availableTimes.add(new LocalDateTime[]{null, scheduledLessons.getFirst().getStart()});
        for (int i = 0; i < scheduledLessons.size()-1; i++) {
            availableTimes.add(new LocalDateTime[]{scheduledLessons.get(i).getEnd(), scheduledLessons.get(i+1).getStart()});
        }
        availableTimes.add(new LocalDateTime[]{scheduledLessons.getLast().getEnd(), null});
        return availableTimes;
    }

    public ArrayList<LocalDateTime[]> getAvailableTimesBetween(LocalDateTime start, LocalDateTime end){
        ArrayList<LocalDateTime[]> availableTimes = new ArrayList<>();
        boolean foundFirst = false;
        for (int i = 0; i < scheduledLessons.size(); i++) {
            if (foundFirst){
                availableTimes.add(new LocalDateTime[]{scheduledLessons.get(i-1).getEnd(), scheduledLessons.get(i).getStart()});
            } else if(scheduledLessons.get(i).getStart().isAfter(start)){
                availableTimes.add(new LocalDateTime[]{start, scheduledLessons.get(i).getStart()});
                foundFirst = true;
            }
        }
        return availableTimes;
    }

    public boolean isOccupied(LocalDateTime start, LocalDateTime end){
        for (Lesson times : scheduledLessons) {
            if(start.isAfter(times.getStart()) && start.isBefore(times.getEnd()) || end.isAfter(times.getStart()) && end.isBefore(times.getEnd()))
                return true;
        }

        return false;
    }

    public LocalDateTime[] getNextAvailableTime(Duration duration){
        for (int i = 1; i < scheduledLessons.size(); i++) {
            LocalDateTime start = scheduledLessons.get(i-1).getEnd();
            LocalDateTime end = scheduledLessons.get(i).getStart();
            if((end.getHour()*60+end.getMinute())-(start.getHour()*60+start.getMinute()) >= duration.toMillis()/60000)
                return new LocalDateTime[]{start, end};
        }
        // TODO CHECK FOR TIME AFTER LAST LESSON
        return null;
    }

    public Lesson addNextAvailableTime(Lesson lesson, Duration duration){
        for (int i = 1; i < scheduledLessons.size(); i++) {
            LocalDateTime start = scheduledLessons.get(i-1).getEnd();
            LocalDateTime end = scheduledLessons.get(i).getStart();
            if((end.getHour()*60+end.getMinute())-(start.getHour()*60+start.getMinute()) >= duration.toMillis()/60000)
                addLesson(lesson);
        }
        // TODO CHECK FOR TIME AFTER LAST LESSON
        return null;
    }

    public String getName() {
        return name;
    }

    public LinkedList<Lesson> getScheduledLessons() {
        return scheduledLessons;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return name;
    }
}
