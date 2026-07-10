package ru.kafpin;

public class CppStudent implements Student {
    private String name;

    public CppStudent() {
        this.name = "Petya";
    }

    public CppStudent(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getKnowledge() {
        return "C++";
    }
}