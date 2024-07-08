package com.example.task.basic.crud.basicCrud.model.mappers;

import com.example.task.basic.crud.basicCrud.model.Department;
import com.example.task.basic.crud.basicCrud.model.dto.DepartmentDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-08T14:04:03+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Amazon.com Inc.)"
)
public class DepartmentMapperImpl implements DepartmentMapper {

    @Override
    public DepartmentDTO departmentToDepartmentDTO(Department department) {
        if ( department == null ) {
            return null;
        }

        DepartmentDTO departmentDTO = new DepartmentDTO();

        if ( department.getUuid() != null ) {
            departmentDTO.setId( department.getUuid().toString() );
        }
        departmentDTO.setName( department.getName() );

        return departmentDTO;
    }

    @Override
    public List<DepartmentDTO> toRegularDepartmentDTOs(List<Department> departments) {
        if ( departments == null ) {
            return null;
        }

        List<DepartmentDTO> list = new ArrayList<DepartmentDTO>( departments.size() );
        for ( Department department : departments ) {
            list.add( departmentToDepartmentDTO( department ) );
        }

        return list;
    }

    @Override
    public Department departmentDTOToDepartment(DepartmentDTO departmentDTO) {
        if ( departmentDTO == null ) {
            return null;
        }

        Department.DepartmentBuilder department = Department.builder();

        if ( departmentDTO.getId() != null ) {
            department.uuid( UUID.fromString( departmentDTO.getId() ) );
        }
        department.name( departmentDTO.getName() );

        return department.build();
    }

    @Override
    public List<Department> toRegularDepartments(List<DepartmentDTO> departmentDTOs) {
        if ( departmentDTOs == null ) {
            return null;
        }

        List<Department> list = new ArrayList<Department>( departmentDTOs.size() );
        for ( DepartmentDTO departmentDTO : departmentDTOs ) {
            list.add( departmentDTOToDepartment( departmentDTO ) );
        }

        return list;
    }
}
