package com.example.task.basic.crud.basicCrud.service.impl;

import com.example.task.basic.crud.basicCrud.model.Department;
import com.example.task.basic.crud.basicCrud.model.Location;
import com.example.task.basic.crud.basicCrud.model.dto.DepartmentDTO;
import com.example.task.basic.crud.basicCrud.model.dto.LocationDTO;
import com.example.task.basic.crud.basicCrud.model.exceptions.InvalidLocationDtoException;
import com.example.task.basic.crud.basicCrud.model.exceptions.InvalidLocationIdException;
import com.example.task.basic.crud.basicCrud.model.mappers.DepartmentMapper;
import com.example.task.basic.crud.basicCrud.model.mappers.LocationMapper;
import com.example.task.basic.crud.basicCrud.repository.LocationRepository;
import com.example.task.basic.crud.basicCrud.service.DepartmentService;
import com.example.task.basic.crud.basicCrud.service.LocationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    private final DepartmentService departmentService;

    @Transactional
    @Override
    public LocationDTO save(LocationDTO locationDTO) {
        if(locationDTO.getName()!=null && !locationDTO.getName().isEmpty()){
            DepartmentDTO departmentDTO = departmentService.getDepartmentByName(locationDTO.getDepartment().getName());
            Department department = DepartmentMapper.INSTANCE.departmentDTOToDepartment(departmentDTO);
            Location location = Location.builder()
                    .name(locationDTO.getName())
                    .department(department)
                    .build();

            locationRepository.save(location);
            return LocationMapper.INSTANCE.locationToLocationDTO(location);
        }else {
            throw new InvalidLocationDtoException();
        }
    }

    @Override
    public List<LocationDTO> findAll() {
        List<Location> locations = locationRepository.findAll();
        List<LocationDTO> locationDTOList = new ArrayList<>();
        for(Location location : locations) {
            locationDTOList.add(LocationMapper.INSTANCE.locationToLocationDTO(location));
        }
        return locationDTOList;
    }

    @Override
    public LocationDTO findById(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            Location location = locationRepository.findById(uuid).orElseThrow(InvalidLocationIdException::new);
            return LocationMapper.INSTANCE.locationToLocationDTO(location);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public LocationDTO findByName(String name) {
        Location location = locationRepository.findByNameIgnoreCase(name);
        if(location!=null) {
            return LocationMapper.INSTANCE.locationToLocationDTO(location);
        } else {
            throw new IllegalArgumentException("Location not found");
        }
    }

    @Override
    public List<LocationDTO> findByDepartment(DepartmentDTO departmentDTO) {
        Department department = DepartmentMapper.INSTANCE.departmentDTOToDepartment(departmentDTO);
        List<Location> locations = locationRepository.findAllByDepartment(department);
        List<LocationDTO> locationDTOList = new ArrayList<>();
        for(Location location : locations) {
            locationDTOList.add(LocationMapper.INSTANCE.locationToLocationDTO(location));
        }
        return locationDTOList;
    }

    @Override
    public LocationDTO delete(String id) {
        Location location = locationRepository.findById(UUID.fromString(id)).orElseThrow(InvalidLocationIdException::new);
        locationRepository.delete(location);
        return LocationMapper.INSTANCE.locationToLocationDTO(location);
    }

    @Override
    public LocationDTO deleteByName(String name) {
        Location location = locationRepository.findByNameIgnoreCase(name);
        if(location != null) {
            locationRepository.delete(location);
        } else {
            throw new IllegalArgumentException("Location not found");
        }
        return LocationMapper.INSTANCE.locationToLocationDTO(location);
    }
}
