package com.example.task.basic.crud.basicCrud.service.impl;

import com.example.task.basic.crud.basicCrud.model.Department;
import com.example.task.basic.crud.basicCrud.model.dto.DepartmentDTO;
import com.example.task.basic.crud.basicCrud.model.exceptions.BadRequestException;
import com.example.task.basic.crud.basicCrud.model.exceptions.DepartmentNotFoundException;
import com.example.task.basic.crud.basicCrud.model.exceptions.InvalidDepartmentIdException;
import com.example.task.basic.crud.basicCrud.model.mappers.DepartmentMapper;
import com.example.task.basic.crud.basicCrud.repository.DepartmentRepository;
import com.example.task.basic.crud.basicCrud.service.DepartmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Transactional
    @Override
    public DepartmentDTO save(DepartmentDTO departmentDTO) {
        if(departmentDTO.getId() != null) {
            throw new BadRequestException("You cannot send id when creating a department");
        }
        if(departmentDTO.getName() != null && !departmentDTO.getName().isEmpty()){
            Department department = Department.builder()
                    .name(departmentDTO.getName())
                    .build();

            departmentRepository.save(department);
            return DepartmentMapper.INSTANCE.departmentToDepartmentDTO(department);
        } else {
            throw new BadRequestException("Invalid department name");
        }
    }

    @Override
    public DepartmentDTO update(String id, DepartmentDTO departmentDTO) {
        if(isValidUUID(id)) {
            validateDepartmentDTO(departmentDTO); // tuka dali e podobro ili vnatre, ama puka u momentot ako nema json nikakov
            Department savedDepartment = departmentRepository.findById(UUID.fromString(id)).orElseThrow(InvalidDepartmentIdException::new);
            savedDepartment.setName(departmentDTO.getName());
            departmentRepository.save(savedDepartment);
            return DepartmentMapper.INSTANCE.departmentToDepartmentDTO(savedDepartment);
        } else {
            throw new BadRequestException("Invalid department id");
        }
    }

    private void validateDepartmentDTO(DepartmentDTO departmentDTO) {
        if (departmentDTO == null) {
            throw new BadRequestException("DepartmentDTO cannot be null");
        }
        if (departmentDTO.getName() == null || departmentDTO.getName().trim().isEmpty()) {
            throw new BadRequestException("Department name cannot be null or empty");
        }
    }

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        List<DepartmentDTO> departmentDTOList = new ArrayList<>();
        for(Department department : departments) {
            departmentDTOList.add(DepartmentMapper.INSTANCE.departmentToDepartmentDTO(department));
        }
        return departmentDTOList;
    }

    @Override
    public DepartmentDTO getDepartmentById(String id) {
        if(isValidUUID(id)) {
            return departmentRepository.findById(UUID.fromString(id))
                    .map(DepartmentMapper.INSTANCE::departmentToDepartmentDTO)
                    .orElseThrow(InvalidDepartmentIdException::new);
        } else  {
            throw new BadRequestException("Wrong id format :D");
        }
    }

    private static boolean isValidUUID(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    @Override
    public DepartmentDTO getDepartmentByName(String name) {
        if(name.isEmpty()) {
            throw new BadRequestException("Invalid name");
        }
        Department department = departmentRepository.findByNameIgnoreCase(name);
        if (department != null) {
            return DepartmentMapper.INSTANCE.departmentToDepartmentDTO(department);
        } else {
            throw new BadRequestException("No such department with the name");
        }
    }


    @Override
    public DepartmentDTO deleteDepartmentById(String id) {
        if(id==null || id.isEmpty()) {
            throw new BadRequestException("No id was passed");
        }
        if(isValidUUID(id)) {
            Department department = departmentRepository.findById(UUID.fromString(id)).orElseThrow(InvalidDepartmentIdException::new);
            departmentRepository.delete(department);
            return DepartmentMapper.INSTANCE.departmentToDepartmentDTO(department);
        } else {
            throw new BadRequestException("Invalid id");
        }
    }

    @Override
    public DepartmentDTO deleteDepartmentByName(String name) {
        Department department = departmentRepository.findByNameIgnoreCase(name);
        if (department != null) {
            departmentRepository.delete(department);
        } else {
            throw new DepartmentNotFoundException("Department not found");
        }
        return DepartmentMapper.INSTANCE.departmentToDepartmentDTO(department);
    }
}
