package com.example.task.basic.crud.basicCrud.service.impl;

import com.example.task.basic.crud.basicCrud.model.Department;
import com.example.task.basic.crud.basicCrud.model.Location;
import com.example.task.basic.crud.basicCrud.model.dto.DepartmentDTO;
import com.example.task.basic.crud.basicCrud.model.dto.LocationDTO;
import com.example.task.basic.crud.basicCrud.model.exceptions.BadRequestException;
import com.example.task.basic.crud.basicCrud.model.exceptions.InvalidLocationDtoException;
import com.example.task.basic.crud.basicCrud.model.exceptions.InvalidLocationIdException;
import com.example.task.basic.crud.basicCrud.model.mappers.DepartmentMapper;
import com.example.task.basic.crud.basicCrud.model.mappers.LocationMapper;
import com.example.task.basic.crud.basicCrud.repository.LocationRepository;
import com.example.task.basic.crud.basicCrud.service.DepartmentService;
import com.example.task.basic.crud.basicCrud.service.LocationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;
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
        if(locationDTO.getId()!=null) {
            throw new BadRequestException("You cannot send id when creating a location");
        }
        DepartmentDTO departmentDTO = departmentService.getDepartmentByName(locationDTO.getDepartment().getName());
        if(locationDTO.getName()!=null && !locationDTO.getName().isEmpty()){
            Department department = DepartmentMapper.INSTANCE.departmentDTOToDepartment(departmentDTO);
            Location location = Location.builder()
                    .name(locationDTO.getName())
                    .department(department)
                    .build();

            locationRepository.save(location);
            return LocationMapper.INSTANCE.locationToLocationDTO(location);
        }else {
            throw new BadRequestException("Bad request Location name is empty");
        }
    }

    @Override
    public LocationDTO update(String id, LocationDTO locationDTO) {
        if(isValidUUID(id)) {
            validateLocationDTO(locationDTO);
            Location savedLocation = locationRepository.findById(UUID.fromString(id)).orElseThrow(InvalidLocationIdException::new);

            DepartmentDTO incomingDepartmentDTO = departmentService.getDepartmentByName(locationDTO.getDepartment().getName());

            savedLocation.setName(locationDTO.getName());
            savedLocation.setDepartment(DepartmentMapper.INSTANCE.departmentDTOToDepartment(incomingDepartmentDTO));
            locationRepository.save(savedLocation);
            return LocationMapper.INSTANCE.locationToLocationDTO(savedLocation);
        } else {
            throw new BadRequestException("Invalid location id");
        }
    }

    private void validateLocationDTO(LocationDTO locationDTO) {
        if(locationDTO == null) {
            throw new BadRequestException("LocationDTO cannot be null");
        }
        if (locationDTO.getName() == null || locationDTO.getName().trim().isEmpty()) {
            throw new BadRequestException("Location name cannot be null or empty");
        }
        if(locationDTO.getDepartment() == null || locationDTO.getDepartment().getName() == null || locationDTO.getDepartment().getName().trim().isEmpty()) {
            throw new BadRequestException("Location department cannot be null or empty");
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
        if(isValidUUID(id)) {
            return locationRepository.findById(UUID.fromString(id))
                    .map(LocationMapper.INSTANCE::locationToLocationDTO)
                    .orElseThrow(InvalidLocationIdException::new);
        } else {
            throw new BadRequestException("Invalid id");
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
}
