package ru.kafpin.restapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.kafpin.restapi.dto.DepartmentDTO;
import ru.kafpin.restapi.exception.EntityNotFoundException;
import ru.kafpin.restapi.model.Department;
import ru.kafpin.restapi.model.Student;
import ru.kafpin.restapi.repository.DepartmentRepository;
import ru.kafpin.restapi.repository.StudentRepository;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@Tag(name = "Department API", description = "CRUD операции с кафедрами")
public class DepartmentController {

    private final DepartmentRepository departmentRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public DepartmentController(DepartmentRepository departmentRepository,
                                StudentRepository studentRepository) {
        this.departmentRepository = departmentRepository;
        this.studentRepository = studentRepository;
    }

    // 1. GET /api/departments - список всех кафедр
    @GetMapping
    @Operation(summary = "Получить все кафедры")
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    // 2. GET /api/departments/{id} - получить кафедру по ID
    @GetMapping("/{id}")
    @Operation(summary = "Получить кафедру по ID")
    public Department getDepartmentById(@PathVariable Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Кафедра с ID " + id + " не найдена"));
    }

    // 3. POST /api/departments - создать кафедру
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать новую кафедру")
    public Department createDepartment(@Valid @RequestBody DepartmentDTO departmentDTO) {
        if (departmentRepository.existsByTitleIgnoreCase(departmentDTO.getTitle())) {
            throw new RuntimeException("Кафедра с названием '" + departmentDTO.getTitle() + "' уже существует");
        }
        Department department = new Department();
        department.setTitle(departmentDTO.getTitle());
        return departmentRepository.save(department);
    }

    // 4. PUT /api/departments/{id} - обновить кафедру
    @PutMapping("/{id}")
    @Operation(summary = "Обновить кафедру")
    public Department updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentDTO departmentDTO) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Кафедра с ID " + id + " не найдена"));

        // Проверяем, что новое название не занято
        if (!department.getTitle().equalsIgnoreCase(departmentDTO.getTitle())) {
            if (departmentRepository.existsByTitleIgnoreCase(departmentDTO.getTitle())) {
                throw new RuntimeException("Кафедра с названием '" + departmentDTO.getTitle() + "' уже существует");
            }
            department.setTitle(departmentDTO.getTitle());
        }

        return departmentRepository.save(department);
    }

    // 5. DELETE /api/departments/{id} - удалить кафедру
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить кафедру")
    public void deleteDepartment(@PathVariable Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Кафедра с ID " + id + " не найдена"));

        // Проверяем, есть ли студенты на кафедре
        List<Student> students = studentRepository.findByDepartmentId(id);
        if (!students.isEmpty()) {
            throw new RuntimeException("Нельзя удалить кафедру, на которой есть студенты");
        }

        departmentRepository.deleteById(id);
    }

    // 6. GET /api/departments/{id}/students - получить студентов кафедры
    @GetMapping("/{id}/students")
    @Operation(summary = "Получить всех студентов кафедры")
    public List<Student> getDepartmentStudents(@PathVariable Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Кафедра с ID " + id + " не найдена");
        }
        return studentRepository.findByDepartmentId(id);
    }
}