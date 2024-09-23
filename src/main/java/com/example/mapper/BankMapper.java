package com.example.mapper;

import com.example.data.model.BankEntity;
import com.example.requset.BankInsertRequest;
import com.example.requset.BankModifyRequest;
import com.example.response.BankResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JSR330, uses = {CountryMapper.class, UserCollectionMapper.class})
public abstract class BankMapper {
    @Mapping(source = "countryId", target = "country")
    public abstract BankEntity bankInsertRequestBankEntity(final BankInsertRequest bankInsertRequest);

    @Mapping(source = "countryId", target = "country")
    public abstract BankEntity bankModifyRequestToBankEntity(final BankModifyRequest bankModifyRequest);


    @Mapping(source = "country", target = "countryName")
    @Mapping(source = "userEntities", target = "usersName")
    public abstract BankResponse bankEntityToBankResponse(final BankEntity bankEntity);

    public String bankEntityToBankName(final BankEntity bankEntity) {
        return bankEntity.getName();
    }

    public BankEntity bankIdToBankEntity(final Integer bankId) {
        BankEntity bankEntity = new BankEntity();
        bankEntity.setId(bankId);
        return bankEntity;
    }
}
