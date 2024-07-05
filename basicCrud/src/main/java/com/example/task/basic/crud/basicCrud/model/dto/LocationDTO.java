package com.example.task.basic.crud.basicCrud.model.dto;

import com.example.task.basic.crud.basicCrud.model.Department;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO {
    private String id;
    private String name;
    private Department department;
}
