package com.example.mapper;

import com.example.data.model.CountryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.JSR330, uses = CountryMapper.class)
public interface CountryCollectionMapper {
    List<String> countryEntityCollectionToCountryNameList(Collection<CountryEntity> countryEntities);
}
