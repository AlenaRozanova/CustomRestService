package com.example.mapper;

import com.example.data.model.CountryEntity;
import com.example.requset.CountryInsertRequest;
import com.example.requset.CountryModifyRequest;
import com.example.response.CountryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JSR330, uses = {BankCollectionMapper.class})
public abstract class CountryMapper {
    public abstract CountryEntity countryInsertRequestToCountryEntity(CountryInsertRequest countryInsertRequest);

    public abstract CountryEntity countryModifyRequestToCountryEntity(CountryModifyRequest countryModifyRequest);

    @Mapping(source = "bankEntities", target = "banksName")
    public abstract CountryResponse countryEntityToCountryResponse(CountryEntity countryEntity);

    public String countryEntityToCountryName(CountryEntity countryEntity) {
        return countryEntity.getName();
    }

    public CountryEntity countryIdToCountryEntity(Integer countryId) {
        CountryEntity countryEntity = new CountryEntity();
        countryEntity.setId(countryId);
        return countryEntity;
    }
}
