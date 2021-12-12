package s.schedulingsystemvia;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import s.schedulingsystemvia.generator.Classroom;
import s.schedulingsystemvia.generator.Course;
import s.schedulingsystemvia.generator.Teacher;
import javafx.scene.paint.Color;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Random;

public class Constants {

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

    public static HashMap<int[][], Lesson> getRandomLessons(){
        HashMap<int[][], Lesson> lessons = new HashMap<>();
        // 6.12 - 10.12
        Random rn = new Random();
        for (int i = 0; i < 10; i++) {
            LocalDateTime start = LocalDateTime.of(2021, 12, rn.nextInt(5) + 6, rn.nextInt(11) + 8, 0);
            LocalDateTime end = start.plusMinutes(15);
            Lesson node = new Lesson(
                    new Classroom("CO5.16b",0),
                    start,
                    end,
                    new Course("Responsive Web Design", "RWD", 5, new Teacher[]{new Teacher("Line", "2355")})
            );
            node.setColor(Color.YELLOW);
            lessons.put(new int[][]{{getColumn(start), getRow(start)}}, node);
        }
        return lessons;
    }

    public static LocalDateTime adjustTime(LocalDateTime time){
        if(time.getMinute() % 5 == 0)
            return time;
        else if(time.getMinute() % 10 <= 4)
            return time.minusMinutes(time.getMinute() % 10);
        return time.plusMinutes(time.getMinute() % 10);
    }

    public static Color getColor(String shortcut){
        return switch (shortcut) {
            case "RWD1" -> Color.YELLOW;
            case "DMA1" -> Color.AQUA;
            case "SDJ1" -> Color.MOCCASIN;
            case "SEP1" -> Color.CORAL;
            default -> Color.WHITE;
        };
    }

}
