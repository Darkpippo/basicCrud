package com.example.task.basic.crud.basicCrud.web;

import com.example.task.basic.crud.basicCrud.model.dto.LocationDTO;
import com.example.task.basic.crud.basicCrud.model.exceptions.BadRequestException;
import com.example.task.basic.crud.basicCrud.model.exceptions.DepartmentNotFoundException;
import com.example.task.basic.crud.basicCrud.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.ServerProperties;
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

    @PutMapping("/location/{id}")
    public LocationDTO updateLocation(@PathVariable String id, @Valid @RequestBody LocationDTO locationDTO) {
        try {
            return locationService.update(id, locationDTO);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/location")
    public List<LocationDTO> listLocations() {
        try {
            return locationService.findAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/location/name/{name}")
    public LocationDTO getLocationByName(@PathVariable String name) {
        try {
            return locationService.findByName(name);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/location/id/{id}")
    public LocationDTO getLocationById(@PathVariable String id) {
        try {
            return locationService.findById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

//    @DeleteMapping("/location/delete/name/{name}")
//    public ResponseEntity<LocationDTO> deleteLocationByName(@PathVariable String name) {
//        LocationDTO locationDTO = locationService.deleteByName(name);
//        return ResponseEntity.ok().body(locationDTO);
//    }

    @DeleteMapping("/location/delete/id/{id}")
    public LocationDTO deleteLocationById(@PathVariable String id) {
        try {
            return locationService.delete(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
