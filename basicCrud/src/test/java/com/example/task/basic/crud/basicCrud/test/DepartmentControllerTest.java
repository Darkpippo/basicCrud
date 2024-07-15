package com.example.task.basic.crud.basicCrud.test;

import com.example.task.basic.crud.basicCrud.model.dto.DepartmentDTO;
import com.example.task.basic.crud.basicCrud.model.exceptions.BadRequestException;
import com.example.task.basic.crud.basicCrud.model.exceptions.InvalidDepartmentIdException;
import com.example.task.basic.crud.basicCrud.service.DepartmentService;
import com.example.task.basic.crud.basicCrud.web.DepartmentController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@WebMvcTest(DepartmentController.class)
public class DepartmentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentService departmentService;

    private DepartmentDTO departmentDTO;
    private String departmentId;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        departmentId = UUID.randomUUID().toString();
        departmentDTO = new DepartmentDTO(departmentId, "Engineering");
    }

    @Test
    public void testAddDepartment_Success() throws Exception {
        when(departmentService.save(any(DepartmentDTO.class))).thenReturn(departmentDTO);

        mockMvc.perform(post("/api/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Engineering\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(departmentDTO.getId()))
                .andExpect(jsonPath("$.name").value("Engineering"));
    }

    @Test
    public void testUpdateDepartment_Success() throws Exception {
        when(departmentService.update(departmentDTO.getId(), departmentDTO)).thenReturn(departmentDTO);

        mockMvc.perform(put("/api/department/" + departmentDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Engineering\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateDepartment_Failure() throws Exception {
        when(departmentService.update(eq(departmentDTO.getId()), any(DepartmentDTO.class))).thenThrow(new BadRequestException("Department name cannot be null or empty"));

        mockMvc.perform(put("/api/department/" + departmentDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Department name cannot be null or empty"));
    }

    @Test
    public void testGetAllDepartments_Success() throws Exception {
        DepartmentDTO department1 = new DepartmentDTO(UUID.randomUUID().toString(), "HR");
        DepartmentDTO department2 = new DepartmentDTO(UUID.randomUUID().toString(), "Marketing");
        List<DepartmentDTO> departments = Arrays.asList(department1, department2);

        when(departmentService.getAllDepartments()).thenReturn(departments);

        mockMvc.perform(get("/api/department")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(departments.size()))
                .andExpect(jsonPath("$[0].name").value(department1.getName()))
                .andExpect(jsonPath("$[1].name").value(department2.getName()))
                .andDo(print());
    }
    @Test
    public void testGetDepartmentById_Success() throws Exception {
        when(departmentService.getDepartmentById(departmentId)).thenReturn(departmentDTO);

        mockMvc.perform(get("/api/department/id/" + departmentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(departmentId))
                .andExpect(jsonPath("$.name").value("Engineering"));
    }

    @Test
    public void testGetDepartmentByName_Success() throws Exception {
        when(departmentService.getDepartmentByName("Engineering")).thenReturn(departmentDTO);

        mockMvc.perform(get("/api/department/name/Engineering")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(departmentId))
                .andExpect(jsonPath("$.name").value("Engineering"));
    }

    @Test
    public void testDeleteDepartmentById_Success() throws Exception {
        when(departmentService.deleteDepartmentById(departmentId)).thenReturn(departmentDTO);

        mockMvc.perform(delete("/api/department/delete/id/" + departmentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(departmentId))
                .andExpect(jsonPath("$.name").value("Engineering"));
    }

    @Test
    public void testAddDepartment_Failure_name() throws Exception {
        when(departmentService.save(any(DepartmentDTO.class))).thenThrow(new BadRequestException("Invalid department name"));

        mockMvc.perform(post("/api/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid department name"));
    }
    @Test
    public void testAddDepartment_Failure_Id() throws Exception {
        when(departmentService.save(any(DepartmentDTO.class))).thenThrow(new BadRequestException("You cannot send id when creating a department"));

        mockMvc.perform(post("/api/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"" + departmentId + "\",\"name\":\"Engineering\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("You cannot send id when creating a department"));
    }

    @Test
    public void testGetDepartmentById_Failure() throws Exception {
        String id = UUID.randomUUID().toString();
        when(departmentService.getDepartmentById(id)).thenThrow(new InvalidDepartmentIdException());

        mockMvc.perform(get("/api/department/id/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.message").value("No department with the given id exists"));
    }

    @Test
    public void testGetDepartmentByName_Failure() throws Exception {
        when(departmentService.getDepartmentByName("eng")).thenThrow(new BadRequestException("No such department with the name"));

        mockMvc.perform(get("/api/department/name/eng")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.message").value("No such department with the name"));
    }

    @Test
    public void testGetDepartmentByName_FailureInternal() throws Exception {
        when(departmentService.getDepartmentByName("Engineering")).thenThrow(new RuntimeException("Department not found"));

        mockMvc.perform(get("/api/department/name/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.message").value("No static resource api/department/name."));
    }

    @Test
    public void testDeleteDepartmentById_Failure() throws Exception {
        when(departmentService.deleteDepartmentById("exampleOfInvalidId")).thenThrow(new BadRequestException("Invalid id"));

        mockMvc.perform(delete("/api/department/delete/id/"+"exampleOfInvalidId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Invalid id"));
    }

//    @Test
//    public void testDeleteDepartmentByName_Failure() throws Exception {
//        when(departmentService.deleteDepartmentByName("HR")).thenThrow(new DepartmentNotFoundException("Department not found"));
//
//        mockMvc.perform(delete("/api/department/delete/name/HR")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
//                .andExpect(jsonPath("$.message").value("Department not found"));
//    }
}