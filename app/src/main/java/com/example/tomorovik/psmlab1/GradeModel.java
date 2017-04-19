package com.example.tomorovik.psmlab1;

/**
 * Created by Tomorovik on 11.03.2017.
 */

public class GradeModel {

    // Variables
    String name;
    int grade;

    // Creates GradeModel with only name as parameter
    GradeModel(String name) {
        this.name = name;
    }

    // Creates GradeModel with name and grade as parameters
    GradeModel(String name, int grade) {
        this.name = name;
        this.grade = grade;
    }

    /// Getters

    public String getName() {
        return name;
    }

    public int getGrade() {
        return grade;
    }

    /// Setters
    public void setGrade(int grade) {
        this.grade = grade;
    }


}
