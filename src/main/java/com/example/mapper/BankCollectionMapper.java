package com.example.mapper;

import com.example.data.model.BankEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.JSR330, uses = BankMapper.class)
public interface BankCollectionMapper {
    List<String> bankEntityCollectionToBankNameList(final Collection<BankEntity> bankEntityCollection);

    List<Integer> bankEntityCollectionToBankIdList(final Collection<BankEntity> bankEntityCollection);
}
