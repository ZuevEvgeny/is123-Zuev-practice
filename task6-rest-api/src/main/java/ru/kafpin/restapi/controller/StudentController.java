package ru.kafpin.restapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.kafpin.restapi.dto.StudentDTO;
import ru.kafpin.restapi.exception.EntityNotFoundException;
import ru.kafpin.restapi.model.Department;
import ru.kafpin.restapi.model.Student;
import ru.kafpin.restapi.repository.DepartmentRepository;
import ru.kafpin.restapi.repository.StudentRepository;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@Tag(name = "Student API", description = "CRUD операции со студентами")
public class StudentController {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public StudentController(StudentRepository studentRepository,
                             DepartmentRepository departmentRepository) {
        this.studentRepository = studentRepository;
        this.departmentRepository = departmentRepository;
    }

    // 1. GET /api/students - список всех студентов
    @GetMapping
    @Operation(summary = "Получить всех студентов")
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // 2. GET /api/students/{id} - получить студента по ID
    @GetMapping("/{id}")
    @Operation(summary = "Получить студента по ID")
    public Student getStudentById(
            @Parameter(description = "ID студента") @PathVariable Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Студент с ID " + id + " не найден"));
    }

    // 3. POST /api/students - создать нового студента
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать нового студента")
    public Student createStudent(@Valid @RequestBody StudentDTO studentDTO) {
        // Ищем кафедру по ID или названию
        Department department;
        if (studentDTO.getDepartmentId() != null) {
            department = departmentRepository.findById(studentDTO.getDepartmentId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Кафедра с ID " + studentDTO.getDepartmentId() + " не найдена"));
        } else if (studentDTO.getDepartmentTitle() != null && !studentDTO.getDepartmentTitle().isEmpty()) {
            department = departmentRepository.findByTitleIgnoreCase(studentDTO.getDepartmentTitle())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Кафедра '" + studentDTO.getDepartmentTitle() + "' не найдена"));
        } else {
            throw new RuntimeException("Необходимо указать departmentId или departmentTitle");
        }

        Student student = new Student();
        student.setName(studentDTO.getName());
        student.setAge(studentDTO.getAge());
        student.setDepartment(department);

        return studentRepository.save(student);
    }

    // 4. PUT /api/students/{id} - обновить студента
    @PutMapping("/{id}")
    @Operation(summary = "Обновить студента")
    public Student updateStudent(
            @Parameter(description = "ID студента") @PathVariable Long id,
            @Valid @RequestBody StudentDTO studentDTO) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Студент с ID " + id + " не найден"));

        student.setName(studentDTO.getName());
        student.setAge(studentDTO.getAge());

        // Обновляем кафедру, если указана
        if (studentDTO.getDepartmentId() != null) {
            Department department = departmentRepository.findById(studentDTO.getDepartmentId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Кафедра с ID " + studentDTO.getDepartmentId() + " не найдена"));
            student.setDepartment(department);
        } else if (studentDTO.getDepartmentTitle() != null && !studentDTO.getDepartmentTitle().isEmpty()) {
            Department department = departmentRepository.findByTitleIgnoreCase(studentDTO.getDepartmentTitle())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Кафедра '" + studentDTO.getDepartmentTitle() + "' не найдена"));
            student.setDepartment(department);
        }

        return studentRepository.save(student);
    }

    // 5. DELETE /api/students/{id} - удалить студента
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить студента")
    public void deleteStudent(
            @Parameter(description = "ID студента") @PathVariable Long id) {
        if (!studentRepository.existsById(id)) {
            throw new EntityNotFoundException("Студент с ID " + id + " не найден");
        }
        studentRepository.deleteById(id);
    }

    // 6. GET /api/students/search/name?name=... - поиск по имени
    @GetMapping("/search/name")
    @Operation(summary = "Поиск студентов по имени")
    public List<Student> searchByName(@RequestParam String name) {
        return studentRepository.findByNameContainingIgnoreCase(name);
    }

    // 7. GET /api/students/search/age?min=18&max=25 - поиск по возрасту
    @GetMapping("/search/age")
    @Operation(summary = "Поиск студентов по диапазону возраста")
    public List<Student> searchByAge(
            @RequestParam int min,
            @RequestParam int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    // 8. GET /api/students/search/department?departmentId=1 - поиск по кафедре
    @GetMapping("/search/department")
    @Operation(summary = "Поиск студентов по кафедре")
    public List<Student> searchByDepartment(@RequestParam Long departmentId) {
        return studentRepository.findByDepartmentId(departmentId);
    }

    // 9. GET /api/students/by-department/{title} - по названию кафедры
    @GetMapping("/by-department/{title}")
    @Operation(summary = "Поиск студентов по названию кафедры")
    public List<Student> getStudentsByDepartmentTitle(
            @Parameter(description = "Название кафедры") @PathVariable String title) {
        Department department = departmentRepository.findByTitleIgnoreCase(title)
                .orElseThrow(() -> new EntityNotFoundException("Кафедра '" + title + "' не найдена"));
        return studentRepository.findByDepartmentId(department.getId());
    }
}