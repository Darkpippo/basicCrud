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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

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

    @Test(expected = BadRequestException.class)
    public void addDepartmentNoNameProvided_Failure() {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName("");

        departmentService.save(departmentDTO);
    }

    @Test(expected = BadRequestException.class)
    public void addDepartmentIdProvided_Failure() {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(UUID.randomUUID().toString());
        departmentDTO.setName("HR");

        departmentService.save(departmentDTO);
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

    @Test(expected = InvalidDepartmentIdException.class)
    public void testUpdateDepartment_NotFound() {
        String id = UUID.randomUUID().toString();
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(id);
        departmentDTO.setName("HR");

        departmentService.update(id, departmentDTO);
    }

    @Test(expected = BadRequestException.class)
    public void testUpdateDepartment_InvalidId() {
        String invalidId = "invalid-uuid";
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(invalidId);

        departmentService.update(invalidId, departmentDTO);
    }

    @Test
    public void testFindByIdNotFoundException_Failure() {
        String id = UUID.randomUUID().toString();
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(id);
        departmentDTO.setName("HR");

        when(departmentRepository.findById(UUID.fromString(id))).thenReturn(Optional.empty());
    }

    @Test(expected = BadRequestException.class)
    public void testFindByIdInvalidFormat_Failure() {
        String id = "invalid-uuid";
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(id);

        departmentService.getDepartmentById(id);
    }

    @Test
    public void testFindById_Success() {
        String id = UUID.randomUUID().toString();

        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName("Dev ops");

        Department department = new Department();
        department.setUuid(UUID.fromString(id));
        department.setName("Dev ops");

        when(departmentRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(department));

        DepartmentDTO result = departmentService.getDepartmentById(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo("Dev ops");
    }
}
