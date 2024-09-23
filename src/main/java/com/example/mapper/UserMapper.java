package com.example.mapper;

import com.example.data.model.UserEntity;
import com.example.requset.UserInsertRequest;
import com.example.requset.UserModifyRequest;
import com.example.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JSR330, uses = {CountryMapper.class, BankMapper.class, BankCollectionMapper.class})
public interface UserMapper {
    UserEntity userInsertRequestToUserEntity(UserInsertRequest userInsertRequest);

    UserEntity userModifyRequestToUserEntity(UserModifyRequest userModifyRequest);

    @Mapping(source = "bankEntitySet", target = "banksName")
    UserResponse userEntityToUserResponse(UserEntity userEntity);
}
