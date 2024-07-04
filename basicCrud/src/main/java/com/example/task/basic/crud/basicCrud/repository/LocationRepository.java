package com.example.task.basic.crud.basicCrud.repository;

import com.example.task.basic.crud.basicCrud.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {
}
