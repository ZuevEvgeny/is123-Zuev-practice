package ru.kafpin.Task3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kafpin.Task3.model.Student;
import ru.kafpin.Task3.service.StudentService;

@Controller
public class FormController {

    private final StudentService studentService;

    @Autowired
    public FormController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("student", new Student());
        return "form";
    }

    @PostMapping("/form")
    public String submitForm(@ModelAttribute Student student, Model model) {
        int currentYear = java.time.Year.now().getValue();
        String groupName = StudentService.generateGroupName(currentYear);
        student.setGroupName(groupName);

        studentService.saveStudent(student);

        String login = StudentService.generateLogin(groupName, student.getId());
        student.setLogin(login);

        model.addAttribute("student", student);
        return "result";
    }
}