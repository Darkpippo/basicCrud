package com.example.task.basic.crud.basicCrud.service.impl;

import com.example.task.basic.crud.basicCrud.model.Department;
import com.example.task.basic.crud.basicCrud.model.dto.DepartmentDTO;
import com.example.task.basic.crud.basicCrud.model.exceptions.DepartmentNotFoundException;
import com.example.task.basic.crud.basicCrud.model.exceptions.InvalidDepartmentDtoException;
import com.example.task.basic.crud.basicCrud.model.exceptions.InvalidDepartmentIdException;
import com.example.task.basic.crud.basicCrud.model.mappers.DepartmentMapper;
import com.example.task.basic.crud.basicCrud.repository.DepartmentRepository;
import com.example.task.basic.crud.basicCrud.service.DepartmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class DepartmentImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Transactional
    @Override
    public DepartmentDTO save(DepartmentDTO departmentDTO) {
        if(departmentDTO.getId() != null) {
            throw new IllegalArgumentException("You cannot send id when creating a department");
        }
        if(departmentDTO.getName()!=null && !departmentDTO.getName().isEmpty()){
            Department department = Department.builder()
                    .name(departmentDTO.getName())
                    .build();

            departmentRepository.save(department);
            return DepartmentMapper.INSTANCE.departmentToDepartmentDTO(department);
        } else {
            throw new IllegalArgumentException("Invalid department name");
        }
    }

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        List<DepartmentDTO> departmentDTOList = new ArrayList<>();
        for(Department department : departments) {
            departmentDTOList.add(DepartmentMapper.INSTANCE.departmentToDepartmentDTO(department));
        }
//        nejke da rab
//        DepartmentMapper.INSTANCE.toRegularDepartmentDTOs(departments);
        return departmentDTOList;
    }

    @Override
    public DepartmentDTO getDepartmentById(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            Department department = departmentRepository.findById(uuid).orElseThrow(InvalidDepartmentIdException::new);
            return DepartmentMapper.INSTANCE.departmentToDepartmentDTO(department);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public DepartmentDTO getDepartmentByName(String name) {
        Department department = departmentRepository.findByNameIgnoreCase(name);
        if (department != null) {
            return DepartmentMapper.INSTANCE.departmentToDepartmentDTO(department);
        } else {
            throw new IllegalArgumentException("Department not found");
        }
    }


    @Override
    public DepartmentDTO deleteDepartmentById(String id) {
        Department department = departmentRepository.findById(UUID.fromString(id)).orElseThrow(InvalidDepartmentIdException::new);
        departmentRepository.delete(department);
        return DepartmentMapper.INSTANCE.departmentToDepartmentDTO(department);
    }

    @Override
    public DepartmentDTO deleteDepartmentByName(String name) {
        Department department = departmentRepository.findByNameIgnoreCase(name);
        if (department != null) {
            departmentRepository.delete(department);
        } else {
            throw new IllegalArgumentException("Department not found");
        }
        return DepartmentMapper.INSTANCE.departmentToDepartmentDTO(department);
    }
}
