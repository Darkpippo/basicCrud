package com.example.task.basic.crud.basicCrud.unitTesting;

import com.example.task.basic.crud.basicCrud.model.Department;
import com.example.task.basic.crud.basicCrud.model.Location;
import com.example.task.basic.crud.basicCrud.model.dto.DepartmentDTO;
import com.example.task.basic.crud.basicCrud.model.dto.LocationDTO;
import com.example.task.basic.crud.basicCrud.model.mappers.LocationMapper;
import com.example.task.basic.crud.basicCrud.service.DepartmentService;
import com.example.task.basic.crud.basicCrud.service.LocationService;
import com.example.task.basic.crud.basicCrud.web.DepartmentController;
import com.example.task.basic.crud.basicCrud.web.LocationController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(LocationController.class)
public class UnitTesting {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationService locationService;

    @MockBean
    private DepartmentService departmentService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new LocationController(locationService)).build();
    }

    @Test
    public void testCreateLocation_Success() throws Exception {
        Department department = new Department(UUID.fromString("9435e87d-81a7-4ffa-9803-598dd3012bd0"), "Practice");
        LocationDTO locationDTO = new LocationDTO(null, "Main Office - Skopje", department);

        when(locationService.save(any(LocationDTO.class))).thenReturn(locationDTO);

        String locationJson = "{ \"name\": \"Main Office - Skopje\", \"department\": { \"id\": \"9435e87d-81a7-4ffa-9803-598dd3012bd0\", \"name\": \"Practice\" } }";

        mockMvc.perform(post("/api/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(locationJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value("Location created successfully"));

        verify(locationService, times(1)).save(any(LocationDTO.class));
    }

    @Test
    public void testCreateLocation_NameIsMandatory() throws Exception {
        String locationJson = "{ \"name\": \"\", \"department\": { \"id\": \"9435e87d-81a7-4ffa-9803-598dd3012bd0\", \"name\": \"Practice\" } }";

        mockMvc.perform(post("/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(locationJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Name is mandatory"));
    }

    @Test
    public void testCreateLocation_DepartmentIsMandatory() throws Exception {
        String locationJson = "{ \"name\": \"Main Office - Skopje\", \"department\": {} }";

        mockMvc.perform(post("/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(locationJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Department is mandatory"));
    }

    @Test
    public void testCreateLocation_InvalidDepartmentId() throws Exception {
        String locationJson = "{ \"name\": \"Main Office - Skopje\", \"department\": { \"id\": \"invalid-id\", \"name\": \"Practice\" } }";

        mockMvc.perform(post("/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(locationJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Department ID must be a valid UUID"));
    }

    @Test
    public void testCreateLocation_InvalidContent() throws Exception {
        String invalidJson = "{ \"invalidField\": \"Some Value\" }";

        mockMvc.perform(post("/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}