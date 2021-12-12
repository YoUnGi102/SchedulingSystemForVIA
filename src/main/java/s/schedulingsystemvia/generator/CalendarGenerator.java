package s.schedulingsystemvia.generator;

import s.schedulingsystemvia.Constants;
import s.schedulingsystemvia.Lesson;
import s.schedulingsystemvia.TimeTable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class CalendarGenerator {

    public static final int LESSON_BLOCK = 210;
    public static final int LUNCH_BREAK = 55;
    public static final int NEXT_DAY = 965;
    public static final ChronoUnit TIME_UNIT = ChronoUnit.MINUTES;

    private Random rn;
    private Classroom[] classrooms;
    private Class[] classes;
    private int weeks; // Without breaks

    // TODO CLASSES HAVE TO BE ORDERED BY SEMESTER
    public CalendarGenerator(Classroom[] classrooms, Class[] classes, int weeks) {
        this.classrooms = classrooms;
        this.classes = classes;
        this.weeks = weeks;
        rn = new Random();
    }

    public HashMap<Class, TimeTable> generateTimeTables() {

        HashMap<Class, TimeTable> timeTables = new HashMap<>();
        for (Class c : classes) {
            TimeTable timeTable = new TimeTable(new Class(c.getProgramme(), c.getSemester(), c.getName(), new Course[]{}));
            // TODO THIS WONT WORK IN REAL CASE -> CLASSROOM MIGHT BE OCCUPIED
            Classroom classroom = classrooms[rn.nextInt(classrooms.length)];

            LocalDateTime[][] nextWeekBlocks = getNextWeekBlocks();
            boolean[] used = new boolean[nextWeekBlocks.length];

            for (Course course : c.getCourses()) {
                double minutesPerWeek = (double) course.getDurationPerSemester().toMinutes()/weeks;
                System.out.println(minutesPerWeek);
                int n = 1;
                if(minutesPerWeek > LESSON_BLOCK*2)
                    n = 3;
                else if(minutesPerWeek > LESSON_BLOCK)
                    n = 2;

                for (int i = 0; i < n; i++) {
                    int lessonIndex = getLessonIndex(used);
                    used[lessonIndex] = true;
                    if(lessonIndex % 2 == 0){
                        Lesson l = new Lesson(
                                classroom,
                                Constants.adjustTime(nextWeekBlocks[lessonIndex][1].minus((long) minutesPerWeek/n, ChronoUnit.MINUTES)),
                                Constants.adjustTime(nextWeekBlocks[lessonIndex][1]),
                                course, course.getTeachers());
                        l.setColor(Constants.getColor(l.getCourse().getShortcut()));
                        classroom.addLesson(l);
                        timeTable.addLesson(l);
                    }else {
                        Lesson l = new Lesson(
                                classroom,
                                Constants.adjustTime(nextWeekBlocks[lessonIndex][0]),
                                Constants.adjustTime(nextWeekBlocks[lessonIndex][0].plus((long) minutesPerWeek/n, ChronoUnit.MINUTES)),
                                course, course.getTeachers());
                        l.setColor(Constants.getColor(l.getCourse().getShortcut()));
                        classroom.addLesson(l);
                        timeTable.addLesson(l);
                    }
                    System.out.println(course.getName());
                }
            }
            timeTables.put(c, timeTable);
        }
        return timeTables;
    }

    // TESTED -  WORKING
    private LocalDateTime getNextWeekStart(){
        LocalDate now = LocalDate.now();
        int addDays = switch (now.getDayOfWeek()){
            case MONDAY -> 7;
            case TUESDAY -> 6;
            case WEDNESDAY -> 5;
            case THURSDAY -> 4;
            case FRIDAY -> 3;
            case SATURDAY -> 2;
            case SUNDAY -> 1;
        };
        return LocalDateTime.of(now.plusDays(addDays), LocalTime.of(8,20));
    }

    // TESTED - WORKING
    public LocalDateTime[][] getNextWeekBlocks(){

        ArrayList<LocalDateTime[]> nextWeekBlocks = new ArrayList<>();
        LocalDateTime start = getNextWeekStart();

        nextWeekBlocks.add(new LocalDateTime[]{start, start.plus(LESSON_BLOCK, TIME_UNIT)});
        for (int i = 1; i < 10; i++) {
            start = nextWeekBlocks.get(i-1)[1];
            if(i % 2 == 1){
                nextWeekBlocks.add(new LocalDateTime[]{start.plus(LUNCH_BREAK, TIME_UNIT), start.plus(LUNCH_BREAK+LESSON_BLOCK, TIME_UNIT)});
            }else {
                nextWeekBlocks.add(new LocalDateTime[]{start.plus(NEXT_DAY, TIME_UNIT), start.plus(NEXT_DAY+LESSON_BLOCK, TIME_UNIT)});
            }
        }

        return nextWeekBlocks.toArray(new LocalDateTime[0][0]);

    }

    // TODO IN TIMETABLE, CHANGE LESSON LIST FROM Map<int[][],Lesson>
    public void spreadTimeTable(TimeTable timeTable){
        //ArrayList<Lesson> lessonsInWeek = timeTable.getLessonsInWeek();
        for (int i = 0; i < weeks; i++) {

        }
    }

    private int getLessonIndex(boolean[] used){
        int counter = 0;
        for (boolean b : used) {
            if(!b) counter++;
        }
        int random = rn.nextInt(counter);

        for (int i = 0; i < used.length; i++) {
            if(!used[i]){
                if(random > 0)
                    random--;
                else
                    return i;
            }
        }
        return -1;
    }
}
