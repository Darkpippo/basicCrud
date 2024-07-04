package com.example.task.basic.crud.basicCrud.service;

import com.example.task.basic.crud.basicCrud.model.Department;
import com.example.task.basic.crud.basicCrud.model.dto.DepartmentDTO;

import java.util.List;

public interface DepartmentService {
    public DepartmentDTO save(DepartmentDTO departmentDTO);
    public List<DepartmentDTO> getAllDepartments();
    public DepartmentDTO getDepartmentById(String id);
    public DepartmentDTO getDepartmentByName(String name);
    public DepartmentDTO deleteDepartmentById(String id);
}
