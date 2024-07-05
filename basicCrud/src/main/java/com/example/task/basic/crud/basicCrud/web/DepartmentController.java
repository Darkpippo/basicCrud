package com.example.task.basic.crud.basicCrud.web;


import com.example.task.basic.crud.basicCrud.model.Department;
import com.example.task.basic.crud.basicCrud.model.dto.DepartmentDTO;
import com.example.task.basic.crud.basicCrud.service.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController()
@RequestMapping("/api")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping("/department")
    public DepartmentDTO addDepartment(@RequestBody DepartmentDTO departmentDTO) {
        try {
            DepartmentDTO savedDepartment = departmentService.save(departmentDTO);
            return savedDepartment;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/department")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        return ResponseEntity.ok().body(departmentService.getAllDepartments());
    }

    @GetMapping("/department/id/{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable String id) {
        DepartmentDTO department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok().body(department);
    }

    @GetMapping("/department/name/{name}")
    public ResponseEntity<DepartmentDTO> getDepartmentByName(@PathVariable String name) {
        DepartmentDTO departmentDTO = departmentService.getDepartmentByName(name);
        return ResponseEntity.ok().body(departmentDTO);
    }

    @DeleteMapping("/department/delete/id/{id}")
    public ResponseEntity<DepartmentDTO> deleteDepartmentById(@PathVariable String id) {
        DepartmentDTO departmentDTO = departmentService.deleteDepartmentById(id);
        return ResponseEntity.status(HttpStatus.OK).body(departmentDTO);
    }

    @DeleteMapping("/department/delete/name/{name}")
    public ResponseEntity<DepartmentDTO> deleteDepartmentByName(@PathVariable String name) {
        DepartmentDTO departmentDTO = departmentService.deleteDepartmentByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(departmentDTO);
    }
}
