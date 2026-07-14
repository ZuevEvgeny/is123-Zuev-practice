package ru.kafpin.restapi.dto;

import jakarta.validation.constraints.*;

public class StudentDTO {

    private Long id;

    @NotBlank(message = "Имя студента обязательно")
    @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
    private String name;

    @Min(value = 16, message = "Возраст должен быть не менее 16 лет")
    @Max(value = 60, message = "Возраст должен быть не более 60 лет")
    private int age;

    private Long departmentId;

    private String departmentTitle;

    // Конструкторы
    public StudentDTO() {}

    public StudentDTO(String name, int age, Long departmentId) {
        this.name = name;
        this.age = age;
        this.departmentId = departmentId;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

    public String getDepartmentTitle() { return departmentTitle; }
    public void setDepartmentTitle(String departmentTitle) { this.departmentTitle = departmentTitle; }
}