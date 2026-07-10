package ru.kafpin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.kafpin.CppStudent;
import ru.kafpin.JavaStudent;
import ru.kafpin.PythonStudent;
import ru.kafpin.Student;

@Configuration
@ComponentScan(basePackages = "ru.kafpin")
public class AppConfig {

    @Bean
    public Student misha() {
        return new JavaStudent("Misha");
    }

    @Bean
    public Student masha() {
        return new PythonStudent("Masha");
    }

    @Bean
    public Student petya() {
        return new CppStudent("Petya");
    }

    @Bean
    public Student ivan() {
        return new JavaStudent("Ivan");
    }

    @Bean
    public Student anna() {
        return new PythonStudent("Anna");
    }
}