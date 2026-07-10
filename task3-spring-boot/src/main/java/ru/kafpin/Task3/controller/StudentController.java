package ru.kafpin.Task3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kafpin.Task3.model.Student;
import ru.kafpin.Task3.service.StudentService;

import java.util.List;

@Controller
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public String listStudents(Model model) {
        List<Student> allStudents = studentService.getAllStudents();
        model.addAttribute("students", allStudents);
        return "students";
    }

    @GetMapping("/students/details/{id}")
    public String studentDetails(@PathVariable("id") Long id, Model model) {
        Student student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        return "details";
    }

    @GetMapping("/students/add")
    public String showAddForm(Model model) {
        model.addAttribute("student", new Student());
        return "add-student";
    }

    @PostMapping("/students/add")
    public String addStudent(@ModelAttribute Student student) {
        studentService.saveStudent(student);
        return "redirect:/students";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long id) {
        studentService.deleteStudentById(id);
        return "redirect:/students";
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }
}