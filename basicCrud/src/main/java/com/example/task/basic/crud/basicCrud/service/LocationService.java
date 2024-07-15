package com.example.task.basic.crud.basicCrud.service;

import com.example.task.basic.crud.basicCrud.model.dto.DepartmentDTO;
import com.example.task.basic.crud.basicCrud.model.dto.LocationDTO;

import java.util.List;

public interface LocationService {
    LocationDTO save(LocationDTO locationDTO);
    LocationDTO update(String id, LocationDTO locationDTO);
    List<LocationDTO> findAll();
    LocationDTO findById(String id);
    LocationDTO findByName(String name);
    List<LocationDTO> findByDepartment(DepartmentDTO departmentDTO);
    LocationDTO delete(String id);
}
