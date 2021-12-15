package s.schedulingsystemvia.generator;

import java.util.Arrays;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Class aClass = (Class) o;
        return semester == aClass.semester && Objects.equals(programme, aClass.programme) && Objects.equals(name, aClass.name);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(programme, semester, name);
        result = 31 * result + Arrays.hashCode(courses);
        return result;
    }
}
