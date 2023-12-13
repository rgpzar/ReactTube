package com.ReactTube.backApplication.mappers;

import com.ReactTube.backApplication.dto.UploadedByDto;
import com.ReactTube.backApplication.dto.UserInputDto;
import com.ReactTube.backApplication.dto.UserOutputDto;
import com.ReactTube.backApplication.models.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserUpdateMapper {

    UserUpdateMapper INSTANCE = Mappers.getMapper( UserUpdateMapper.class );

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UserInputDto userInputDto, @MappingTarget User user);

    UploadedByDto uploadedByDtoFromUser(User user);
    User userFromUploadedByDto(UploadedByDto uploadedByDto);

    User userFromUserDto(UserOutputDto userOutputDto);

    UserOutputDto userOutputDtoFromUser(User user);

    UserOutputDto userOutputDtoFromUserInputDto(UserInputDto userInputDto);

}
