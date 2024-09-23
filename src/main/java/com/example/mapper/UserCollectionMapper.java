package com.example.mapper;

import com.example.data.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.JSR330, uses = UserMapper.class)
public abstract class UserCollectionMapper {
    public List<String> userEntityCollectionToUserNames(Collection<UserEntity> userEntitySet) {
        return userEntitySet.stream().map(UserEntity::getName).collect(Collectors.toList());
    }
}