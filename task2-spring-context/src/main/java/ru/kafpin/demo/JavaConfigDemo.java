package ru.kafpin.demo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.kafpin.Department;
import ru.kafpin.Student;
import ru.kafpin.config.AppConfig;

public class JavaConfigDemo {

    public static void main(String[] args) {

        ApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        // Получаем Department - Spring сам создаст его через @Component
        Department dept = context.getBean(Department.class);

        // Но Department уже имеет внедрённого студента через @Qualifier("misha")
        // Мы можем просто вывести информацию
        dept.displayInfo();

        // Или можем вручную заменить студента
        dept.setStudent(context.getBean("anna", Student.class));
        dept.displayInfo();

        // Получаем всех студентов
        Student misha = context.getBean("misha", Student.class);
        Student masha = context.getBean("masha", Student.class);
        Student petya = context.getBean("petya", Student.class);
        Student ivan = context.getBean("ivan", Student.class);
        Student anna = context.getBean("anna", Student.class);


    }
}