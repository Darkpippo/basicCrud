package com.example.task.basic.crud.basicCrud.unitTesting;

import com.example.task.basic.crud.basicCrud.model.Department;
import com.example.task.basic.crud.basicCrud.model.dto.DepartmentDTO;
import com.example.task.basic.crud.basicCrud.model.exceptions.BadRequestException;
import com.example.task.basic.crud.basicCrud.model.exceptions.DepartmentNotFoundException;
import com.example.task.basic.crud.basicCrud.model.exceptions.InvalidDepartmentIdException;
import com.example.task.basic.crud.basicCrud.model.mappers.DepartmentMapper;
import com.example.task.basic.crud.basicCrud.repository.DepartmentRepository;

import static org.assertj.core.api.Assertions.as;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import com.example.task.basic.crud.basicCrud.service.impl.DepartmentServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(MockitoJUnitRunner.class)
public class DepartmentServiceTest {
    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @Test
    public void addDepartment_Success() {
        String id = UUID.randomUUID().toString();

        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName("Dev ops");

        Department department = new Department();
        department.setUuid(UUID.fromString(id));
        department.setName("Dev ops");

        when(departmentRepository.save(any(Department.class))).thenAnswer(invocation -> {
           Department dept = invocation.getArgument(0);
           dept.setUuid(department.getUuid());
           return dept;
        });

        DepartmentDTO result = departmentService.save(departmentDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo("Dev ops");
    }

    @Test
    public void addDepartmentNoNameProvided_Failure() {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName("");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            departmentService.save(departmentDTO);
        });

        assertEquals("Invalid department name", exception.getMessage());
    }

    @Test
    public void addDepartmentIdProvided_Failure() {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName("HR");

        BadRequestException exception = assertThrows(BadRequestException.class, () ->
        {
            departmentDTO.setId(UUID.randomUUID().toString());
            departmentService.save(departmentDTO);
        });

        assertEquals("You cannot send id when creating a department", exception.getMessage());
    }

    @Test
    public void testUpdateDepartment_Success() {
        String id = UUID.randomUUID().toString();
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName("HR");

        Department department = new Department();
        department.setUuid(UUID.fromString(id));
        department.setName("HR");

        when(departmentRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(department));

        DepartmentDTO updatedDepartment = departmentService.update(id, departmentDTO);

        assertThat(updatedDepartment).isNotNull();
        assertThat(updatedDepartment.getName()).isEqualTo("HR");

        verify(departmentRepository, times(1)).findById(UUID.fromString(id));
        verify(departmentRepository, times(1)).save(department);
    }

    @Test
    public void testUpdateDepartmentNoNameProvided_Failure() {
        String id = UUID.randomUUID().toString();
        DepartmentDTO departmentDTO = new DepartmentDTO();

        BadRequestException exception = assertThrows(BadRequestException.class, () ->{
            departmentService.update(id, departmentDTO);
        });

        assertEquals("Department name cannot be null or empty", exception.getMessage());
    }

    @Test
    public void testUpdateDepartmentNullDTO_Failure() {
        String id = UUID.randomUUID().toString();
        DepartmentDTO departmentDTO = null;

        BadRequestException exception = assertThrows(BadRequestException.class, () ->{
            departmentService.update(id, departmentDTO);
        });

        assertEquals("DepartmentDTO cannot be null", exception.getMessage());
    }

    @Test
    public void testFindByIdNotFoundException_Failure() {
        String id = UUID.randomUUID().toString();
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(id);
        departmentDTO.setName("HR");

        InvalidDepartmentIdException exception = assertThrows(InvalidDepartmentIdException.class, () ->{
            departmentService.getDepartmentById(id);
        });

        assertEquals("No department with the given id exists", exception.getMessage());
    }

    @Test
    public void testFindByIdInvalidFormat_Failure() {
        String id = "invalid-uuid";

        BadRequestException exception = assertThrows(BadRequestException.class, () ->{
            departmentService.getDepartmentById(id);
        });

        assertEquals("Wrong id format :D", exception.getMessage());
    }

    @Test
    public void testFindById_Success() {
        Department department = new Department();
        department.setUuid(UUID.randomUUID());
        department.setName("Dev ops");

        when(departmentRepository.findById(UUID.fromString(department.getUuid().toString())))
                .thenReturn(Optional.of(department));

        DepartmentDTO result = departmentService.getDepartmentById(department.getUuid().toString());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(department.getUuid().toString());
        assertThat(result.getName()).isEqualTo("Dev ops");
    }
}