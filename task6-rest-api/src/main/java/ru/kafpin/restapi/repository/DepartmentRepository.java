package ru.kafpin.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kafpin.restapi.model.Department;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByTitleIgnoreCase(String title);

    boolean existsByTitleIgnoreCase(String title);
}