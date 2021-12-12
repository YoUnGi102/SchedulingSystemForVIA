package s.schedulingsystemvia.generator;

import java.util.Arrays;

public class Class {

    private String programme;
    private int semester;
    private String name;
    private Course[] courses;

    public Class(String programme, int semester, String name, Course[] courses) {
        this.programme = programme;
        this.semester = semester;
        this.name = name;
        this.courses = courses;
    }

    public String getProgramme() {
        return programme;
    }

    public int getSemester() {
        return semester;
    }

    public String getName() {
        return name;
    }

    public Course[] getCourses() {
        return courses;
    }

    @Override
    public String toString() {
        return name;
    }
}
