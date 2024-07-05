package com.example.task.basic.crud.basicCrud.model.mappers;

import com.example.task.basic.crud.basicCrud.model.Department;
import com.example.task.basic.crud.basicCrud.model.dto.DepartmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DepartmentMapper {
    DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

    @Mapping(source = "uuid", target = "id")
    DepartmentDTO departmentToDepartmentDTO(Department department);

    List<DepartmentDTO> toRegularDepartmentDTOs(List<Department> departments);

    @Mapping(source = "id", target = "uuid")
    Department departmentDTOToDepartment(DepartmentDTO departmentDTO);

    List<Department> toRegularDepartments(List<DepartmentDTO> departmentDTOs);
}
