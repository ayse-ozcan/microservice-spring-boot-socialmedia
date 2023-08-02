package com.socialmedia.mapper;

import com.socialmedia.dto.request.AuthUpdateRequestDto;
import com.socialmedia.dto.request.ToAuthPasswordChangeRequestDto;
import com.socialmedia.dto.request.UserProfileSaveRequestDto;
import com.socialmedia.dto.request.UserProfileUpdateRequestDto;
import com.socialmedia.rabbitmq.model.UserRegisterModel;
import com.socialmedia.repository.entity.UserProfile;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserProfileMapper {
    IUserProfileMapper INSTANCE = Mappers.getMapper(IUserProfileMapper.class);
    UserProfile fromSaveRequestDto(final UserProfileSaveRequestDto dto);
    UserProfile fromRegisterModelToUserProfile(final UserRegisterModel model);

    //BeanMapping, NullValuePropertyMappingStrategy parametresi sayesinde null olarak gonderilen verileri dikkate almaz.
    //Yani update islemi yapilirken update etmek istemediginiz property leri swagger da sildiginizde null olacaklari icin,
    //bunlarin veri tabanina null olarak kaydedilmemesini saglar.
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserProfile fromUpdateRequestDto (final UserProfileUpdateRequestDto dto, @MappingTarget UserProfile userProfile);
    AuthUpdateRequestDto fromUserProfileToAuthUpdateRequestDto(final UserProfile userProfile);

    ToAuthPasswordChangeRequestDto fromUserProfileToAuthPasswordChangeDto(final UserProfile userProfile);
}
