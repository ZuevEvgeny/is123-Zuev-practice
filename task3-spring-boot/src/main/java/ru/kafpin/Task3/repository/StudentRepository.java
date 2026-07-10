package ru.kafpin.Task3.repository;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import ru.kafpin.Task3.model.Student;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentRepository {
    private List<Student> students;
    private Long currentId = 5L;

    @PostConstruct
    public void init() {
        students = new ArrayList<>();
        students.add(new Student(1L, "Михаил", "Иванов", "Петрович",
                "misha@mail.ru", "Москва", "Мужской", 20,
                "Радиотехнический", "РИ-120", "student-ri120-1"));
        students.add(new Student(2L, "Мария", "Петрова", "Сергеевна",
                "masha@mail.ru", "Санкт-Петербург", "Женский", 19,
                "Информационных технологий", "ПИНз-120", "student-pinz120-2"));
        students.add(new Student(3L, "Магомед", "Алиев", "Расулович",
                "magomed@mail.ru", "Махачкала", "Мужской", 20,
                "Радиотехнический", "РИ-121", "student-ri121-3"));
        students.add(new Student(4L, "Анна", "Смирнова", "Алексеевна",
                "anna@mail.ru", "Владимир", "Женский", 18,
                "Информационных технологий", "ПИНз-121", "student-pinz121-4"));
    }

    public List<Student> getStudents() {
        return students;
    }

    public Student getStudentById(Long id) {
        return students.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void save(Student student) {
        if (student.getId() == null) {
            student.setId(currentId++);
        } else {
            deleteById(student.getId());
        }
        students.add(student);
    }

    public void deleteById(Long id) {
        students.removeIf(s -> s.getId().equals(id));
    }
}