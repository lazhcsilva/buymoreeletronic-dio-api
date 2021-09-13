package com.dititalinnovation.buymore.mapper;

import com.dititalinnovation.buymore.dto.ElectronicDTO;
import com.dititalinnovation.buymore.entity.Electronic;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ElectronicMapper {

    ElectronicMapper INSTANCE = Mappers.getMapper(ElectronicMapper.class);

    Electronic toModel(ElectronicDTO electronicDTO);

    ElectronicDTO toDTO(Electronic electronic);

}
