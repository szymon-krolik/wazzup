package com.wazzup.mapper;

import com.wazzup.dto.CreateUserDTO;
import com.wazzup.dto.UserInformationDTO;
import com.wazzup.entity.User;
import com.wazzup.entity.User.UserBuilder;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-02-26T12:20:42+0100",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.1 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(CreateUserDTO dto) {
        if ( dto == null ) {
            return null;
        }

        UserBuilder user = User.builder();

        user.email( dto.getEmail() );
        user.phoneNumber( dto.getPhoneNumber() );
        user.name( dto.getName() );
        user.password( dto.getPassword() );
        user.city( dto.getCity() );

        return user.build();
    }

    @Override
    public UserInformationDTO toUserInformation(User user) {
        if ( user == null ) {
            return null;
        }

        UserInformationDTO userInformationDTO = new UserInformationDTO();

        userInformationDTO.setBirthDate( user.getBirthdate() );
        userInformationDTO.setName( user.getName() );
        userInformationDTO.setEmail( user.getEmail() );
        userInformationDTO.setPhoneNumber( user.getPhoneNumber() );

        return userInformationDTO;
    }
}
