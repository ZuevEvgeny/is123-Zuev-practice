package ru.kafpin.restapi.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя студента обязательно")
    @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
    @Column(name = "name", nullable = false)
    private String name;

    @Min(value = 16, message = "Возраст должен быть не менее 16 лет")
    @Max(value = 60, message = "Возраст должен быть не более 60 лет")
    @Column(name = "age", nullable = false)
    private int age;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", nullable = false)
    @JsonManagedReference
    private Department department;

    public Student() {}

    public Student(String name, int age, Department department) {
        this.name = name;
        this.age = age;
        this.department = department;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
}