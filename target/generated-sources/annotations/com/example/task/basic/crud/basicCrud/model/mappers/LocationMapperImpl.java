package com.example.task.basic.crud.basicCrud.model.mappers;

import com.example.task.basic.crud.basicCrud.model.Location;
import com.example.task.basic.crud.basicCrud.model.dto.LocationDTO;
import java.util.UUID;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-05T12:59:46+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Amazon.com Inc.)"
)
public class LocationMapperImpl implements LocationMapper {

    @Override
    public LocationDTO locationToLocationDTO(Location location) {
        if ( location == null ) {
            return null;
        }

        LocationDTO locationDTO = new LocationDTO();

        if ( location.getUuid() != null ) {
            locationDTO.setId( location.getUuid().toString() );
        }
        locationDTO.setName( location.getName() );
        locationDTO.setDepartment( location.getDepartment() );

        return locationDTO;
    }

    @Override
    public Location locationDTOToLocation(LocationDTO locationDTO) {
        if ( locationDTO == null ) {
            return null;
        }

        Location.LocationBuilder location = Location.builder();

        if ( locationDTO.getId() != null ) {
            location.uuid( UUID.fromString( locationDTO.getId() ) );
        }
        location.name( locationDTO.getName() );
        location.department( locationDTO.getDepartment() );

        return location.build();
    }
}
