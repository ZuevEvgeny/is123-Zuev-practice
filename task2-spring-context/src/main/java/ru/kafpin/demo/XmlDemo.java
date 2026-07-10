package ru.kafpin.demo;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.kafpin.Department;
import ru.kafpin.Student;

public class XmlDemo {

    public static void main(String[] args) {

        try (ClassPathXmlApplicationContext context =
                     new ClassPathXmlApplicationContext("appContext.xml")) {

            // 1.1 Получаем бин, созданный через конструктор
            Department dept1 = context.getBean("departmentConstructor", Department.class);
            dept1.displayInfo();

            // 1.2 Получаем бин, созданный через сеттер
            Department dept2 = context.getBean("departmentSetter", Department.class);
            dept2.displayInfo();

            // 1.3 Выводим всех студентов
            Student java = context.getBean("javaStudent", Student.class);
            Student python = context.getBean("pythonStudent", Student.class);
            Student cpp = context.getBean("cppStudent", Student.class);

        }
    }
}