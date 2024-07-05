package com.example.task.basic.crud.basicCrud.web;

import com.example.task.basic.crud.basicCrud.model.Department;
import com.example.task.basic.crud.basicCrud.model.Location;
import com.example.task.basic.crud.basicCrud.model.dto.LocationDTO;
import com.example.task.basic.crud.basicCrud.model.exceptions.InvalidLocationException;
import com.example.task.basic.crud.basicCrud.model.mappers.LocationMapper;
import com.example.task.basic.crud.basicCrud.service.LocationService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @PostMapping("/location")
    public ResponseEntity<LocationDTO> createLocation(@RequestBody LocationDTO locationDTO) {
        LocationDTO savedLocation = locationService.save(locationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLocation);
    }

    @GetMapping("/locations")
    public ResponseEntity<List<LocationDTO>> listLocations() {
        return ResponseEntity.ok().body(locationService.findAll());
    }
}
