package com.example.task.basic.crud.basicCrud.web;

import com.example.task.basic.crud.basicCrud.model.Department;
import com.example.task.basic.crud.basicCrud.model.Location;
import com.example.task.basic.crud.basicCrud.model.dto.LocationDTO;
import com.example.task.basic.crud.basicCrud.model.exceptions.BadRequestException;
import com.example.task.basic.crud.basicCrud.model.exceptions.DepartmentNotFoundException;
import com.example.task.basic.crud.basicCrud.model.exceptions.InvalidLocationException;
import com.example.task.basic.crud.basicCrud.model.mappers.LocationMapper;
import com.example.task.basic.crud.basicCrud.service.LocationService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @PostMapping("/location")
    @ResponseStatus(HttpStatus.CREATED)
    public LocationDTO createLocation(@Valid @RequestBody LocationDTO locationDTO) {
        try {
            return locationService.save(locationDTO);
        } catch (BadRequestException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (DepartmentNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/location")
    public ResponseEntity<List<LocationDTO>> listLocations() {
        List<LocationDTO> locationDTOList = locationService.findAll();
        if(locationDTOList.isEmpty()) {
            return ResponseEntity.status(204).body(locationDTOList);
        } else {
            return ResponseEntity.ok().body(locationService.findAll());
        }
    }

    @GetMapping("/location/name/{name}")
    public ResponseEntity<LocationDTO> getLocationByName(@PathVariable String name) {
        return ResponseEntity.ok().body(locationService.findByName(name));
    }

    @GetMapping("/location/id/{id}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable String id) {
        return ResponseEntity.ok().body(locationService.findById(id));
    }

    @DeleteMapping("/location/delete/name/{name}")
    public ResponseEntity<LocationDTO> deleteLocationByName(@PathVariable String name) {
        LocationDTO locationDTO = locationService.deleteByName(name);
        return ResponseEntity.ok().body(locationDTO);
    }

    @DeleteMapping("/location/delete/id/{id}")
    public ResponseEntity<LocationDTO> deleteLocationById(@PathVariable String id) {
        LocationDTO locationDTO = locationService.delete(id);
        return ResponseEntity.ok().body(locationDTO);
    }
}
