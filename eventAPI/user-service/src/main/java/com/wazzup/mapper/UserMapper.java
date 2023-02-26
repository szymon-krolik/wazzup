package com.wazzup.mapper;

import com.wazzup.dto.CreateUserDTO;
import com.wazzup.dto.UserInformationDTO;
import com.wazzup.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(CreateUserDTO dto);
    @Mapping(source = "birthdate", target = "birthDate", dateFormat = "dd/MM/yyyy")
    @Mapping(ignore = true, target = "authorities")
    UserInformationDTO toUserInformation(User user);
}
