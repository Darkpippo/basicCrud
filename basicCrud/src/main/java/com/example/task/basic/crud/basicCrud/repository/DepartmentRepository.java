package com.example.task.basic.crud.basicCrud.repository;

import com.example.task.basic.crud.basicCrud.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {
    Department findByNameIgnoreCase(String name);
}
