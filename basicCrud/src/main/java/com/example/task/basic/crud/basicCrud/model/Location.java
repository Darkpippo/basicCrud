package com.example.task.basic.crud.basicCrud.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @ManyToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name = "department_id", nullable = false)
    @NotNull(message = "Department is mandatory")
    private Department department;
}
