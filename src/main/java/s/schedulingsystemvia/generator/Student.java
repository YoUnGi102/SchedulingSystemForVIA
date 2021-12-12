package s.schedulingsystemvia.generator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class Student {

    private String name;
    private LocalDate birthday;
    private Gender gender;
    private Class studentClass;
    private int ECTSPoints;
    private String VIANumber;

    public Student(String name, LocalDate birthday, Gender gender, Class studentClass, int ECTSPoints, String VIANumber) {
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
        this.studentClass = studentClass;
        this.ECTSPoints = ECTSPoints;
        this.VIANumber = VIANumber;
    }

    public String getName() {
        return name;
    }
    public LocalDate getBirthday() {
        return birthday;
    }
    public Gender getGender() {
        return gender;
    }
    public String getProgramme() {
        return studentClass.getProgramme();
    }
    public Class getStudentClass() {
        return studentClass;
    }
    public int getSemester() {
        return studentClass.getSemester();
    }
    public int getECTSPoints() {
        return ECTSPoints;
    }
    public String getVIANumber() {
        return VIANumber;
    }

    public enum Gender{
        MALE,
        FEMALE,
        OTHER;

        public static final ObservableList<Gender> GENDERS_LIST = FXCollections.observableArrayList(Gender.values());

        @Override
        public String toString() {
            return switch (this){
                case MALE -> "Male";
                case FEMALE -> "Female";
                case OTHER -> "Other";
            };
        }

        public static Gender getGender(String gender){
            return switch (gender) {
                case "Male" -> MALE;
                case "Female" -> FEMALE;
                case "Other" -> OTHER;
                default -> null;
            };
        }


    }

}
