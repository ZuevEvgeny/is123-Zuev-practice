package ru.kafpin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Department {
    private Student student;

    public Department() {}

    public Department(Student student) {
        this.student = student;
    }

    @Autowired
    public void setStudent(@Qualifier("ivan") Student student) {
        this.student = student;
    }

    public void displayInfo() {
        System.out.println("Student: " + student.getName());
        System.out.println("Can coding on: " + student.getKnowledge());
    }
}