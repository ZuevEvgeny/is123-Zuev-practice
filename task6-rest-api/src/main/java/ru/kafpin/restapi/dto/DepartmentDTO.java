package ru.kafpin.restapi.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class DepartmentDTO {

    private Long id;

    @NotBlank(message = "Название кафедры обязательно")
    private String title;

    private List<StudentDTO> students;

    // Конструкторы
    public DepartmentDTO() {}

    public DepartmentDTO(String title) {
        this.title = title;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<StudentDTO> getStudents() { return students; }
    public void setStudents(List<StudentDTO> students) { this.students = students; }
}