package com.example.task.basic.crud.basicCrud.test;

import com.example.task.basic.crud.basicCrud.model.Department;
import com.example.task.basic.crud.basicCrud.model.Location;
import com.example.task.basic.crud.basicCrud.model.dto.DepartmentDTO;
import com.example.task.basic.crud.basicCrud.model.dto.LocationDTO;
import com.example.task.basic.crud.basicCrud.model.exceptions.BadRequestException;
import com.example.task.basic.crud.basicCrud.model.exceptions.InvalidLocationIdException;
import com.example.task.basic.crud.basicCrud.model.mappers.DepartmentMapper;
import com.example.task.basic.crud.basicCrud.repository.LocationRepository;
import com.example.task.basic.crud.basicCrud.service.DepartmentService;
import com.example.task.basic.crud.basicCrud.service.impl.LocationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class LocationServiceTest {
    @InjectMocks
    private LocationServiceImpl locationService;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private DepartmentService departmentService;

    private LocationDTO locationDTO;
    private DepartmentDTO departmentDTO;

    private Location location;
    private Department department;

    @BeforeEach
    public void setUp() {
        departmentDTO = new DepartmentDTO();
        departmentDTO.setName("HR");

        locationDTO = new LocationDTO();
        locationDTO.setName("Headquarters");
        locationDTO.setDepartment(DepartmentMapper.INSTANCE.departmentDTOToDepartment(departmentDTO));

        department = DepartmentMapper.INSTANCE.departmentDTOToDepartment(departmentDTO);
        location = Location.builder()
                .uuid(UUID.randomUUID())
                .name("Old name")
                .department(department)
                .build();
    }

    @Test
    public void save_ValidLocation_SavesAndReturnsLocationDTO() {
        when(departmentService.getDepartmentByName(any(String.class))).thenReturn(departmentDTO);

        Location location = new Location();
        location.setName("Headquarters");
        location.setDepartment(DepartmentMapper.INSTANCE.departmentDTOToDepartment(departmentDTO));

        when(locationRepository.save(any(Location.class))).thenReturn(location);

        LocationDTO savedLocationDTO = locationService.save(locationDTO);

        assertEquals("Headquarters", savedLocationDTO.getName());
        assertEquals("HR", savedLocationDTO.getDepartment().getName());
    }

    @Test
    public void save_LocationWithIdProvided_ThrowsBadRequestException() {
        locationDTO.setId(UUID.randomUUID().toString());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            locationService.save(locationDTO);
        });

        assertEquals("You cannot send id when creating a location", exception.getMessage());
    }

    @Test
    public void save_LocationWithEmptyName_ThrowsBadRequestException() {
        locationDTO.setName("");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            locationService.save(locationDTO);
        });

        assertEquals("Bad request Location name is empty", exception.getMessage());
    }

    @Test
    public void update_ValidIdAndDTO_Success() {
        String id = location.getUuid().toString();
        when(locationRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(location));
        when(departmentService.getDepartmentByName(anyString())).thenReturn(departmentDTO);

        LocationDTO updatedLocationDTO = locationService.update(id, locationDTO);

        assertEquals("Headquarters", updatedLocationDTO.getName());
        assertEquals("HR", updatedLocationDTO.getDepartment().getName());
    }

    @Test
    public void update_InvalidUUID_ThrowsBadRequestException() {
        String invalidId = "invalidId";
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            locationService.update(invalidId, locationDTO);
        });

        assertEquals("Invalid location id", exception.getMessage());
    }

    @Test
    public void update_LocationNotFound_ThrowsInvalidLocationIdException() {
        String id = UUID.randomUUID().toString();
        when(locationRepository.findById(UUID.fromString(id))).thenReturn(Optional.empty());

        InvalidLocationIdException exception = assertThrows(InvalidLocationIdException.class, () -> {
           locationService.update(id, locationDTO);
        });

        assertEquals("No location with the given id exists.", exception.getMessage());
    }

    @Test
    public void update_EmptyLocationName_ThrowsBadRequestException() {
        String id = location.getUuid().toString();
        locationDTO.setName("");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            locationService.update(id, locationDTO);
        });

        assertEquals("Location name cannot be null or empty", exception.getMessage());
    }

    @Test
    public void findById_Success() {
        String id = location.getUuid().toString();
        when(locationRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(location));

        LocationDTO result = locationService.findById(id);

        assertEquals("Old name", result.getName());
    }

    @Test
    public void findById_InvalidFormat_Failure() {
        String id = "Invalid format id";

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            locationService.findById(id);
        });

        assertEquals("Invalid id", exception.getMessage());
    }

    @Test
    public void findById_NoDepartment_Failure() {
        String id = UUID.randomUUID().toString();

        InvalidLocationIdException exception = assertThrows(InvalidLocationIdException.class, () -> {
            locationService.findById(id);
        });

        assertEquals("No location with the given id exists.", exception.getMessage());
    }

    @Test
    public void findAll_Success() {
        List<Location> locations = Arrays.asList(
                new Location(UUID.randomUUID(), "Location1", department),
                new Location(UUID.randomUUID(), "Location2", department)
        );

        when(locationRepository.findAll()).thenReturn(locations);

        List<LocationDTO> result = locationService.findAll();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getName()).isEqualTo("Location1");
        assertThat(result.get(1).getName()).isEqualTo("Location2");

        verify(locationRepository, times(1)).findAll();
    }
}
