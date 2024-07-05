package com.example.task.basic.crud.basicCrud.model.mappers;

import com.example.task.basic.crud.basicCrud.model.Location;
import com.example.task.basic.crud.basicCrud.model.dto.LocationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LocationMapper {
    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    @Mapping(source = "uuid", target = "id")
    LocationDTO locationToLocationDTO(Location location);

    @Mapping(source = "id", target = "uuid")
    Location locationDTOToLocation(LocationDTO locationDTO);
}
