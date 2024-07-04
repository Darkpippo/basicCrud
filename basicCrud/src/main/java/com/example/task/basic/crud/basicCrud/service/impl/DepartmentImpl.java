package com.example.task.basic.crud.basicCrud.service.impl;

import com.example.task.basic.crud.basicCrud.model.Department;
import com.example.task.basic.crud.basicCrud.model.dto.DepartmentDTO;
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
public class DepartmentImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Transactional
    @Override
    public DepartmentDTO save(DepartmentDTO departmentDTO) {
        if(departmentDTO.getName()!=null && !departmentDTO.getName().isEmpty()){
//            Department department = Department.builder()
//                    .uuid(UUID.randomUUID())
//                    .name(departmentDTO.getName())
//                    .build();
            Department department = new Department(UUID.randomUUID(), departmentDTO.getName());

            departmentRepository.save(department);
            return DepartmentMapper.INSTANCE.departmentToDepartmentDTO(department);
        } else {
            throw new InvalidDepartmentIdException();
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
        UUID uuid = UUID.fromString(id);
        Department department = departmentRepository.findById(uuid).orElseThrow(InvalidDepartmentIdException::new);
        return DepartmentMapper.INSTANCE.departmentToDepartmentDTO(department);
    }

    @Override
    public DepartmentDTO getDepartmentByName(String name) {
        Department department = departmentRepository.findByName(name);
        return DepartmentMapper.INSTANCE.departmentToDepartmentDTO(department);
    }

    @Override
    public DepartmentDTO deleteDepartmentById(String id) {
        Department department = departmentRepository.findById(UUID.fromString(id)).orElseThrow(InvalidDepartmentIdException::new);
        departmentRepository.delete(department);
        return DepartmentMapper.INSTANCE.departmentToDepartmentDTO(department);
    }
}
