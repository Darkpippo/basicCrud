package com.example.task.basic.crud.basicCrud.unitTesting;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.example.task.basic.crud.basicCrud.model.dto.DepartmentDTO;
import com.example.task.basic.crud.basicCrud.model.dto.LocationDTO;
import com.example.task.basic.crud.basicCrud.model.exceptions.BadRequestException;
import com.example.task.basic.crud.basicCrud.model.mappers.DepartmentMapper;
import com.example.task.basic.crud.basicCrud.service.DepartmentService;
import com.example.task.basic.crud.basicCrud.service.LocationService;
import com.example.task.basic.crud.basicCrud.web.LocationController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

@WebMvcTest(LocationController.class)
public class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentService departmentService;

    @MockBean
    private LocationService locationService;

    private DepartmentDTO departmentDTO;
    private LocationDTO locationDTO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        departmentDTO = new DepartmentDTO();
        departmentDTO.setId(UUID.randomUUID().toString());
        departmentDTO.setName("Dev Ops");

        locationDTO = new LocationDTO();
        locationDTO.setId(null);
        locationDTO.setName("New York");
        locationDTO.setDepartment(DepartmentMapper.INSTANCE.departmentDTOToDepartment(departmentDTO));
    }

    @Test
    public void testSaveLocation_Success() throws Exception {
        when(departmentService.getDepartmentByName(departmentDTO.getName())).thenReturn(departmentDTO);
        when(locationService.save(any(LocationDTO.class))).thenReturn(locationDTO);

        mockMvc.perform(post("/api/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "New York",
                            "department": {
                                "name": "Dev Ops"
                            }
                        }"""))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(new ObjectMapper().writeValueAsString(locationDTO)));
    }

    @Test
    public void testSaveLocation_NameFailure() throws Exception{
        when(departmentService.getDepartmentByName(departmentDTO.getName())).thenReturn(departmentDTO);
        when(locationService.save(any(LocationDTO.class))).thenThrow(new BadRequestException("Bad request Location name is empty"));

        mockMvc.perform(post("/api/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name":"",
                            "department": {
                                "name": "Dev Ops"
                            }
                        }"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Bad request Location name is empty"));
    }

    @Test
    public void testSaveLocation_Failure_Id() throws Exception{
        when(departmentService.getDepartmentByName(departmentDTO.getName())).thenReturn(departmentDTO);
        when(locationService.save(any(LocationDTO.class))).thenThrow(new BadRequestException("You cannot send id when creating a location"));

        mockMvc.perform(post("/api/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "id": "",
                            "name": "HR",
                            "department": {
                                "name": "Dev Ops"
                            }
                        }"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("You cannot send id when creating a location"));

    }
    @Test
    public void testSaveLocation_Failure_Empty_Department_Name() throws Exception {
        mockMvc.perform(post("/api/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "name": "New York",
                        "department": {
                            "name": ""
                        }
                    }"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Department name cannot be empty"));
    }
}
