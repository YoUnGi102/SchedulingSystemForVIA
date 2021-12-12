package s.schedulingsystemvia.database;

import s.schedulingsystemvia.generator.Classroom;
import s.schedulingsystemvia.generator.Course;
import s.schedulingsystemvia.generator.Teacher;

import java.time.format.DateTimeFormatter;

public class test {

    public static void main(String[] args) {


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm");
        /*
        for (LocalDateTime[] times : CalendarGenerator.getNextWeekBlocks()){

            System.out.println(times[0].getDayOfWeek() + " " + formatter.format(times[0]) + " - " + formatter.format(times[1]));
        }
        */

        Teacher[] classZTeachers = new Teacher[]{
                new Teacher("Steffen Vissing Andersen", "293"),
                new Teacher("Mona Wendel Andersen", "232"),
                new Teacher("Line Lindhardt Egsgaard", "432"),
                new Teacher("Klaus Bøgestrand", "125"),
                new Teacher("Io Odderskov", "534")
        };
        Course[] classZCourses = new Course[]{
                new Course("Software Development with UML and Java", "SDJ1",  10, new Teacher[]{classZTeachers[0], classZTeachers[3]}),
                new Course("Discrete Mathematics and Algorithms", "DMA1", 5, new Teacher[]{classZTeachers[0], classZTeachers[3]}),
                new Course("Responsive Web Design", "RWD1", 5, new Teacher[]{classZTeachers[2]}),
                new Course("Semester Project", "SEP1", 10, new Teacher[]{classZTeachers[0], classZTeachers[1]}),
        };

        Classroom[] classrooms = new Classroom[]{
                new Classroom("CO5.16b", 45),
                new Classroom("CO5.16a", 45)
        };

        /*
        Class classZ = new Class("Software Technology Engineering", 1, "Z", new ArrayList<>(), classZCourses);

        CalendarGenerator generator = new CalendarGenerator(classrooms, new Class[]{classZ}, 15);

        Map<Class, TimeTable> timeTableMap = generator.generateTimeTables();
        for (Class c : timeTableMap.keySet()) {

            List<Lesson> lessonMap = timeTableMap.get(c).getLessons();

            for (int[][] i : lessonMap.keySet()) {
                Lesson l = lessonMap.get(i);
                System.out.println(l.getCourse().getShortcut() + " " + l.getStart().getDayOfWeek() + " " + formatter.format(l.getStart()) + " - " + formatter.format(l.getEnd()));
            }
        }
*/
    }

}
