package s.schedulingsystemvia.generator;

import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class CalendarGenerator {

    public static final int COLOR_COUNT = 20;
    public static final int LESSON_BLOCK = 210;
    public static final int LUNCH_BREAK = 55;
    public static final int NEXT_DAY = 965;
    public static final ChronoUnit TIME_UNIT = ChronoUnit.MINUTES;

    private Random rn;
    private Classroom[] classrooms;
    private Class[] classes;
    private int weeks; // Without breaks

    HashMap<Class, TimeTable> timeTableHashMap;

    // TODO CLASSES HAVE TO BE ORDERED BY SEMESTER
    public CalendarGenerator(Classroom[] classrooms, Class[] classes, int weeks) {
        timeTableHashMap = new HashMap<>();
        this.classrooms = classrooms;
        this.classes = classes;
        this.weeks = weeks;
        rn = new Random();
    }

    public void generateTimeTables() {

        ArrayList<Classroom> classroomsList = new ArrayList<>(Arrays.asList(classrooms));

        timeTableHashMap = new HashMap<>();
        for (Class c : classes) {
            TimeTable timeTable = new TimeTable(new Class(c.getProgramme(), c.getSemester(), c.getName(), new Course[]{}));
            // TODO THIS WONT WORK IN REAL CASE -> CLASSROOM MIGHT BE OCCUPIED

            Classroom classroom = null;
            if(!classroomsList.isEmpty())
                classroom = classroomsList.remove(0);

            LocalDateTime[][] nextWeekBlocks = getNextWeekBlocks();
            boolean[] used = new boolean[nextWeekBlocks.length];

            for (Course course : c.getCourses()) {
                double minutesPerWeek = (double) course.getDurationPerSemester().toMinutes()/weeks;
                int n = 1;
                if(minutesPerWeek > LESSON_BLOCK*2)
                    n = 3;
                else if(minutesPerWeek > LESSON_BLOCK)
                    n = 2;

                for (int i = 0; i < n; i++) {
                    int lessonIndex = getLessonIndex(used);
                    used[lessonIndex] = true;
                    if(lessonIndex % 2 == 0){
                        LocalDateTime start = adjustTime(nextWeekBlocks[lessonIndex][1].minus((long) minutesPerWeek/n, ChronoUnit.MINUTES));
                        LocalDateTime end = adjustTime(nextWeekBlocks[lessonIndex][1]);
                        classroom = getAvailableClassroom(start, end, classroom);
                        Lesson l = new Lesson(
                                classroom,
                                timeTable.getAClass(),
                                start,
                                end,
                                course, course.getTeachers()
                        );
                        classroom.addLesson(l);
                        timeTable.addLesson(l);
                    }else {
                        LocalDateTime start = adjustTime(nextWeekBlocks[lessonIndex][0]);
                        LocalDateTime end = adjustTime(nextWeekBlocks[lessonIndex][0].plus((long) minutesPerWeek/n, ChronoUnit.MINUTES));
                        classroom = getAvailableClassroom(start, end, classroom);
                        Lesson l = new Lesson(
                                classroom,
                                timeTable.getAClass(),
                                start,
                                end,
                                course, course.getTeachers());
                        classroom.addLesson(l);
                        timeTable.addLesson(l);
                    }
                }
            }
            timeTableHashMap.put(c, timeTable);
        }

    }

    private Classroom getAvailableClassroom(LocalDateTime start, LocalDateTime end, Classroom classroom){
        if(classroom != null){
            return classroom;
        }else {
            for (Classroom c : classrooms) {
                if(c.isAvailable(start, end)){
                    return c;
                }
            }
            throw new NullPointerException("There are no more available classrooms");
        }
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

    public void spreadTimeTables(){
        for (Class c : timeTableHashMap.keySet()) {
            TimeTable timeTable = timeTableHashMap.get(c);
            int week = TimeTable.getWeek(timeTable.getLessons().get(0));
            for (int i = 1; i < weeks+1; i++) {

                ArrayList<Lesson> lessons = timeTable.getLessonsInWeek(week);
                for (Lesson lesson : lessons) {
                    Lesson newLesson = new Lesson(
                            lesson.getClassroom(),
                            lesson.getStudentClass(),
                            lesson.getStart().plus(i, ChronoUnit.WEEKS),
                            lesson.getEnd().plus(i, ChronoUnit.WEEKS),
                            lesson.getCourse(),
                            lesson.getTeachers()
                    );
                    timeTable.addLesson(newLesson);
                }
            }
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

    public static LocalDateTime adjustTime(LocalDateTime time){
        if(time.getMinute() % 5 == 0)
            return time;
        else if(time.getMinute() % 10 <= 4)
            return time.minusMinutes(time.getMinute() % 10);
        return time.plusMinutes(time.getMinute() % 10);
    }

    public static Color getColor(int i){
        return switch (i){
            case 0 -> Color.valueOf("#CCFF00");
            case 1 -> Color.valueOf("#CCFFFF");
            case 2 -> Color.valueOf("#FFFF33");
            case 3 -> Color.valueOf("#CCCC00");
            case 4 -> Color.valueOf("#CCCCFF");
            case 5 -> Color.valueOf("#FFCCFF");
            case 6 -> Color.valueOf("#FFCCCC");
            case 7 -> Color.valueOf("#FFCC00");
            case 8 -> Color.valueOf("#CC9933");
            case 9 -> Color.valueOf("#CC99FF");
            case 10 -> Color.valueOf("#FF9999");
            case 11 -> Color.valueOf("#FF9900");
            case 12 -> Color.valueOf("#CC66FF");
            case 13 -> Color.valueOf("#FF66CC");
            case 14 -> Color.valueOf("#FF6600");
            case 15 -> Color.valueOf("#CC33FF");
            case 16 -> Color.valueOf("#CC00FF");
            case 17 -> Color.valueOf("#6699FF");
            case 18 -> Color.valueOf("#66CC33");
            case 19 -> Color.valueOf("#66CCFF");
            default -> Color.WHITE;
        };
    }

    public HashMap<Class, TimeTable> getTimeTableHashMap() {
        return timeTableHashMap;
    }
}
