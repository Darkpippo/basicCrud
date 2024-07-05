package com.example.task.basic.crud.basicCrud.service;

import com.example.task.basic.crud.basicCrud.model.Department;
import com.example.task.basic.crud.basicCrud.model.dto.DepartmentDTO;

import java.util.List;

public interface DepartmentService {
    DepartmentDTO save(DepartmentDTO departmentDTO);
    List<DepartmentDTO> getAllDepartments();
    DepartmentDTO getDepartmentById(String id);
    DepartmentDTO getDepartmentByName(String name);
    DepartmentDTO deleteDepartmentById(String id);
    DepartmentDTO deleteDepartmentByName(String name);
}
