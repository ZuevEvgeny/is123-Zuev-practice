package ru.kafpin.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kafpin.restapi.model.Student;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByNameContainingIgnoreCase(String name);

    List<Student> findByAgeBetween(int minAge, int maxAge);

    List<Student> findByDepartmentId(Long departmentId);
}