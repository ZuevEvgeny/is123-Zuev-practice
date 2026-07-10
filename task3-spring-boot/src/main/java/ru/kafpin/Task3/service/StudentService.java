package ru.kafpin.Task3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kafpin.Task3.model.Student;
import ru.kafpin.Task3.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.getStudents();
    }

    public Student getStudentById(Long id) {
        return studentRepository.getStudentById(id);
    }

    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    public void deleteStudentById(Long id) {
        studentRepository.deleteById(id);
    }

    public static String generateGroupName(int enrollmentYear) {
        String lastTwoDigits = String.valueOf(enrollmentYear % 100);
        return "ПИНз-" + lastTwoDigits;
    }

    public static String generateLogin(String groupName, Long id) {
        return "student-" + groupName.toLowerCase() + "-" + id;
    }
}