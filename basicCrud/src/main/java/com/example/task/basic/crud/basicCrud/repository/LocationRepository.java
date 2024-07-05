package com.example.task.basic.crud.basicCrud.repository;

import com.example.task.basic.crud.basicCrud.model.Department;
import com.example.task.basic.crud.basicCrud.model.Location;
import com.example.task.basic.crud.basicCrud.model.dto.DepartmentDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {
    Location findByNameIgnoreCase(String name);
    List<Location> findAllByDepartment(Department department);
}
