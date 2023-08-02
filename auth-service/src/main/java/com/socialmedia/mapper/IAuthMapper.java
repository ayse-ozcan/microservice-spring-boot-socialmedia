package com.socialmedia.mapper;

import com.socialmedia.dto.request.AuthRegisterRequestDto;
import com.socialmedia.dto.request.AuthUpdateRequestDto;
import com.socialmedia.dto.request.UserProfileSaveRequestDto;
import com.socialmedia.dto.response.AuthRegisterResponseDto;
import com.socialmedia.rabbitmq.model.MailForgotPasswordModel;
import com.socialmedia.rabbitmq.model.MailRegisterModel;
import com.socialmedia.rabbitmq.model.UserRegisterModel;
import com.socialmedia.repository.entity.Auth;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IAuthMapper {
    IAuthMapper INSTANCE = Mappers.getMapper(IAuthMapper.class);

    Auth fromRegisterDto(final AuthRegisterRequestDto dto);

    @Mapping(source = "id", target = "authId")
    UserRegisterModel fromAuthToUserRegisterModel(final Auth auth);
    MailRegisterModel fromAuthToMailRegisterModel(final Auth auth);

    AuthRegisterResponseDto fromAuth(final Auth auth);

    //1.yol
    //UserProfileSaveRequestDto fromRegisterDtoToUserProfileSaveRequestDto(final AuthRegisterRequestDto dto);
    //2.yol
    @Mapping(source = "id", target = "authId")
    UserProfileSaveRequestDto fromRegisterDtoToUserProfileSaveRequestDto(final Auth auth);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Auth fromAuthUpdateDto(final AuthUpdateRequestDto dto, @MappingTarget Auth auth);

    MailForgotPasswordModel fromAuthToMailForgotPasswordModel(final Auth auth);

}
