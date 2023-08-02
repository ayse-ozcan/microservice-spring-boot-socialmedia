package com.socialmedia.service;

import com.socialmedia.dto.request.*;
import com.socialmedia.exception.ErrorType;
import com.socialmedia.exception.UserProfileManagerException;
import com.socialmedia.manager.IAuthManager;
import com.socialmedia.mapper.IUserProfileMapper;
import com.socialmedia.rabbitmq.model.UserForgotPasswordModel;
import com.socialmedia.rabbitmq.model.UserRegisterModel;
import com.socialmedia.repository.IUserProfileRepository;
import com.socialmedia.repository.entity.UserProfile;
import com.socialmedia.repository.enums.EStatus;
import com.socialmedia.utility.JwtProvider;
import com.socialmedia.utility.ServiceManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileService extends ServiceManager<UserProfile, String> {
    private final IUserProfileRepository userProfileRepository;
    private final IAuthManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public UserProfileService(IUserProfileRepository userProfileRepository, IAuthManager authManager,
                              PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        super(userProfileRepository);
        this.userProfileRepository = userProfileRepository;
        this.authManager = authManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public Boolean createUser(UserProfileSaveRequestDto dto) {
//        UserProfile userProfile = IUserProfileMapper.INSTANCE.fromSaveRequestDto(dto);
//        userProfileRepository.save(userProfile);
        userProfileRepository.save(IUserProfileMapper.INSTANCE.fromSaveRequestDto(dto));
        return true;
    }

    public Boolean createUserRabbitMq(UserRegisterModel model) {
        userProfileRepository.save(IUserProfileMapper.INSTANCE.fromRegisterModelToUserProfile(model));
        return true;
    }

    public Boolean updateUser(String token,UserProfileUpdateRequestDto dto) {
        Optional<Long> authId = jwtProvider.getIdFromToken(token);
        if (authId.isEmpty()){
            throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(authId.get());
        if (userProfile.isPresent()) {
            userProfileRepository.save(IUserProfileMapper.INSTANCE.fromUpdateRequestDto(dto, userProfile.get()));
            AuthUpdateRequestDto authUpdateRequestDto =
                    IUserProfileMapper.INSTANCE.fromUserProfileToAuthUpdateRequestDto(userProfile.get());
            authManager.updateAuth(authUpdateRequestDto);
            return true;
        }
        throw new RuntimeException("hata");
    }

    public Boolean deleteUser(Long authId) {
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findOptionalByAuthId(authId);
        if (optionalUserProfile.isPresent()) {
            optionalUserProfile.get().setEStatus(EStatus.DELETED);
            update(optionalUserProfile.get());
            return true;
        }
        throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
    }

    public Boolean activateStatus(Long authId) {
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findOptionalByAuthId(authId);
        if (optionalUserProfile.isPresent()) {
            optionalUserProfile.get().setEStatus(EStatus.ACTIVE);
            update(optionalUserProfile.get());
            return true;
        } else {
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }
    }

    public Boolean forgotPassword(UserSetPasswordRequestDto dto) {
        Optional<UserProfile> optionalAuth = userProfileRepository.findOptionalByAuthId(dto.getAuthId());
        if (optionalAuth.isPresent()) {
            optionalAuth.get().setPassword(dto.getPassword());
            update(optionalAuth.get());
            return true;
        } else {
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }
    }

    public Boolean forgotPasswordWithRabbitMq(UserForgotPasswordModel model) {
        Optional<UserProfile> optionalAuth = userProfileRepository.findOptionalByAuthId(model.getAuthId());
        if (optionalAuth.isPresent()) {
            optionalAuth.get().setPassword(model.getPassword());
            update(optionalAuth.get());
            return true;
        } else {
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }
    }

    public Boolean passwordChange(PasswordChangeRequestDto dto) {
        Optional<Long> authId = jwtProvider.getIdFromToken(dto.getToken());
        if (authId.isEmpty()){
            throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(authId.get());
        if (userProfile.isPresent()) {
            if (passwordEncoder.matches(dto.getOldPassword(), userProfile.get().getPassword())) {
                userProfile.get().setPassword(passwordEncoder.encode(dto.getNewPassword()));
                save(userProfile.get());
                authManager.passwordChange(IUserProfileMapper.INSTANCE.fromUserProfileToAuthPasswordChangeDto(userProfile.get()));
                return true;
            } else {
                throw new UserProfileManagerException(ErrorType.PASSWORDS_NOT_MATCH);
            }
        } else {
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }


    }

}