package ru.kafpin.demo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import ru.kafpin.Department;
import ru.kafpin.Student;

@ComponentScan(basePackages = "ru.kafpin")
public class AnnotationDemo {

    public static void main(String[] args) {

        // Создаём контекст из текущего класса (у него есть @ComponentScan)
        ApplicationContext context =
                new AnnotationConfigApplicationContext(AnnotationDemo.class);

        // Получаем Department - он создан благодаря @Component
        Department dept = context.getBean(Department.class);

        dept.displayInfo();

        // Получаем всех студентов
        Student misha = context.getBean("misha", Student.class);
        Student masha = context.getBean("masha", Student.class);
        Student petya = context.getBean("petya", Student.class);
        Student ivan = context.getBean("ivan", Student.class);
        Student anna = context.getBean("anna", Student.class);


    }
}