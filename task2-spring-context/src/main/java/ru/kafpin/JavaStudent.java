package ru.kafpin;

public class JavaStudent implements Student {
    private String name;

    // Конструктор для XML и аннотаций (имя задаётся внутри)
    public JavaStudent() {
        this.name = "Misha";
    }

    // Конструктор для Java-конфига (имя передаётся извне)
    public JavaStudent(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getKnowledge() {
        return "Java";
    }
}