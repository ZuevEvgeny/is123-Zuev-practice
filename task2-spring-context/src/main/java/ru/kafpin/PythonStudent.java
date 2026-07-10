package ru.kafpin;

public class PythonStudent implements Student {
    private String name;

    public PythonStudent() {
        this.name = "Masha";
    }

    public PythonStudent(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getKnowledge() {
        return "Python";
    }
}