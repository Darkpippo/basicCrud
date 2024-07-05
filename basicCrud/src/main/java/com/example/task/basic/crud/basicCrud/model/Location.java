package com.example.task.basic.crud.basicCrud.model;

import jakarta.persistence.*;
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
    private String name;

    @ManyToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}
