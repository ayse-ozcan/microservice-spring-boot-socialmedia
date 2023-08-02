package com.socialmedia.service;

import com.socialmedia.dto.request.*;
import com.socialmedia.dto.response.AuthRegisterResponseDto;
import com.socialmedia.exception.AuthManagerException;
import com.socialmedia.exception.ErrorType;
import com.socialmedia.manager.IUserProfileManager;
import com.socialmedia.mapper.IAuthMapper;
import com.socialmedia.rabbitmq.model.MailForgotPasswordModel;
import com.socialmedia.rabbitmq.model.MailRegisterModel;
import com.socialmedia.rabbitmq.model.UserForgotPasswordModel;
import com.socialmedia.rabbitmq.producer.MailForgotPasswordProducer;
import com.socialmedia.rabbitmq.producer.MailRegisterProducer;
import com.socialmedia.rabbitmq.producer.UserForgotPasswordProducer;
import com.socialmedia.rabbitmq.producer.UserRegisterProducer;
import com.socialmedia.repository.IAuthRepository;
import com.socialmedia.repository.entity.Auth;
import com.socialmedia.repository.enums.EStatus;
import com.socialmedia.utility.CodeGenerator;
import com.socialmedia.utility.JwtProvider;
import com.socialmedia.utility.ServiceManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService extends ServiceManager<Auth, Long> {
    private final IAuthRepository authRepository;
    private final IUserProfileManager userProfileManager;
    private final UserRegisterProducer userRegisterProducer;
    private final UserForgotPasswordProducer userForgotPasswordProducer;
    private final MailRegisterProducer mailRegisterProducer;
    private final PasswordEncoder passwordEncoder;
    private final MailForgotPasswordProducer mailForgotPasswordProducer;
    private final JwtProvider jwtProvider;

    public AuthService(IAuthRepository authRepository, IUserProfileManager userProfileManager,
                       UserRegisterProducer userRegisterProducer, UserForgotPasswordProducer userForgotPasswordProducer,
                       MailRegisterProducer mailRegisterProducer, PasswordEncoder passwordEncoder,
                       MailForgotPasswordProducer mailForgotPasswordProducer, JwtProvider jwtProvider) {
        super(authRepository);
        this.authRepository = authRepository;
        this.userProfileManager = userProfileManager;
        this.userRegisterProducer = userRegisterProducer;
        this.userForgotPasswordProducer = userForgotPasswordProducer;
        this.mailRegisterProducer = mailRegisterProducer;
        this.passwordEncoder = passwordEncoder;
        this.mailForgotPasswordProducer = mailForgotPasswordProducer;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    public AuthRegisterResponseDto register(AuthRegisterRequestDto dto) {
        Auth auth = IAuthMapper.INSTANCE.fromRegisterDto(dto);
        if (auth.getPassword().equals(dto.getRePassword())) {
            auth.setActivationCode(CodeGenerator.generateCode());
            auth.setPassword(passwordEncoder.encode(dto.getPassword()));
            save(auth);
            // 1.yol
//            UserProfileSaveRequestDto userDto = IAuthMapper.INSTANCE.fromRegisterDtoToUserProfileSaveRequestDto(dto);
//            userDto.setAuthId(auth.getId());
//            userProfileManager.createUser(userDto);
            // 2.yol
            userProfileManager.createUser(IAuthMapper.INSTANCE.fromRegisterDtoToUserProfileSaveRequestDto(auth));
        } else {
            throw new AuthManagerException(ErrorType.PASSWORDS_NOT_MATCH);
        }
        AuthRegisterResponseDto authRegisterResponseDto = IAuthMapper.INSTANCE.fromAuth(auth);
        return authRegisterResponseDto;
    }

    public AuthRegisterResponseDto registerWithRabbitMq(AuthRegisterRequestDto dto) {
        Auth auth = IAuthMapper.INSTANCE.fromRegisterDto(dto);
        if (auth.getPassword().equals(dto.getRePassword())) {
            auth.setActivationCode(CodeGenerator.generateCode());
            auth.setPassword(passwordEncoder.encode(dto.getPassword()));
            save(auth);
            userRegisterProducer.sendRegisterMessage(IAuthMapper.INSTANCE.fromAuthToUserRegisterModel(auth));
            MailRegisterModel model = IAuthMapper.INSTANCE.fromAuthToMailRegisterModel(auth);
            model.setDecodedPassword(dto.getPassword());
            mailRegisterProducer.sendMailRegister(model);
        } else {
            throw new AuthManagerException(ErrorType.PASSWORDS_NOT_MATCH);
        }
        AuthRegisterResponseDto authRegisterResponseDto = IAuthMapper.INSTANCE.fromAuth(auth);
        return authRegisterResponseDto;
    }


    public String login(AuthLoginRequestDto dto) {
        Optional<Auth> optionalAuth = authRepository.findOptionalByEmailIgnoreCase(dto.getEmail());
        //passwordEncoder.matches(,) -> encode edilmis halini girdigim parolaya geri donusturmek icin kullanilir
        if (optionalAuth.isEmpty() || !passwordEncoder.matches(dto.getPassword(),
                optionalAuth.get().getPassword())) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        if (!optionalAuth.get().getStatus().equals(EStatus.ACTIVE)) {
            throw new AuthManagerException(ErrorType.ACCOUNT_NOT_ACTIVE);
        }
        return jwtProvider.createToken(optionalAuth.get().getId(), optionalAuth.get().getRole())
                .orElseThrow(() -> {throw new AuthManagerException(ErrorType.TOKEN_NOT_CREATED);
                });
    }

    @Transactional
    public Boolean activateStatus(ActivateRequestDto dto) {
        Optional<Auth> optionalAuth = findById(dto.getId());
        if (optionalAuth.isEmpty()) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        if (optionalAuth.get().getStatus().equals(EStatus.ACTIVE)) {
            throw new AuthManagerException(ErrorType.ALREADY_ACTIVE);
        }
        if (!optionalAuth.get().getStatus().equals(EStatus.PENDING)) {
            throw new AuthManagerException((ErrorType.USER_ACCESS_ERROR));
        }
        if (dto.getActivationCode().equals(optionalAuth.get().getActivationCode())) {
            optionalAuth.get().setStatus(EStatus.ACTIVE);
            update(optionalAuth.get());
            userProfileManager.activateStatus(dto.getId());
            return true;
        } else {
            throw new AuthManagerException(ErrorType.ACTIVATE_CODE_ERROR);
        }
    }

    public Boolean updateAuth(AuthUpdateRequestDto dto) {
        Optional<Auth> optionalAuth = authRepository.findById(dto.getAuthId());
        if (optionalAuth.isPresent()) {
            save(IAuthMapper.INSTANCE.fromAuthUpdateDto(dto, optionalAuth.get()));
            return true;
        }
        throw new RuntimeException("hata");
    }
// TODO:jwt
    @Transactional
    public Boolean deleteUser(String token) {
        Optional<Long> authId = jwtProvider.getIdFromToken(token);
        if (authId.isEmpty()){
            throw new AuthManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<Auth> optionalAuth = authRepository.findById(authId.get());
        optionalAuth.orElseThrow(() -> {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        });
//        if (optionalAuth.isEmpty() ){
//            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
//        }
        if (optionalAuth.get().getStatus().equals(EStatus.ACTIVE) || optionalAuth.get().getStatus().equals(EStatus.PENDING)) {
            optionalAuth.get().setStatus(EStatus.DELETED);
            update(optionalAuth.get());
            userProfileManager.deleteUser(authId.get());
            return true;
        } else {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
    }

    public Boolean forgotPassword(ForgotPasswordRequestDto dto) {
        Optional<Auth> optionalAuth = authRepository.findOptionalByEmailIgnoreCase(dto.getEmail());
        if (optionalAuth.isPresent()
                && optionalAuth.get().getStatus().equals(EStatus.ACTIVE)) {
            String randomPassword = UUID.randomUUID().toString();
            optionalAuth.get().setPassword(passwordEncoder.encode(randomPassword));
            update(optionalAuth.get());
            UserSetPasswordRequestDto userSetPasswordRequestDto = UserSetPasswordRequestDto.builder().
                    authId(optionalAuth.get().getId())
                    .password(optionalAuth.get().getPassword())
                    .build();
            userProfileManager.forgotPassword(userSetPasswordRequestDto);
            return true;
        } else {
            throw new AuthManagerException(ErrorType.ACCOUNT_NOT_ACTIVE);
        }
    }

    public Boolean forgotPasswordRabbitMq(ForgotPasswordRequestDto dto) {
        Optional<Auth> optionalAuth = authRepository.findOptionalByEmailIgnoreCase(dto.getEmail());
        if (optionalAuth.isPresent()
                && optionalAuth.get().getStatus().equals(EStatus.ACTIVE)) {
            String randomPassword = UUID.randomUUID().toString();
            optionalAuth.get().setPassword(randomPassword);
            System.out.println("YENI PAROLA: " + randomPassword);
            optionalAuth.get().setPassword(passwordEncoder.encode(randomPassword));
            update(optionalAuth.get());
            userForgotPasswordProducer.sendForgotPasswordMessage(UserForgotPasswordModel.builder()
                    .authId(optionalAuth.get().getId())
                    .password(optionalAuth.get().getPassword())
                    .build());
//            1.yontem --> builder
            mailForgotPasswordProducer.sendMailForgotPassword(MailForgotPasswordModel.builder()
                    .email(optionalAuth.get().getEmail())
                    .randomPassword(randomPassword)
                    .build());
            //2.yontem --> mapper
//            MailForgotPasswordModel model = IAuthMapper.INSTANCE.
//                    fromAuthToMailForgotPasswordModel(optionalAuth.get());
//            model.setRandomPassword(randomPassword);
//            mailForgotPasswordProducer.sendMailForgotPassword(model);
            return true;
        } else {
            throw new AuthManagerException(ErrorType.ACCOUNT_NOT_ACTIVE);
        }
    }

    public Boolean passwordChange(PasswordChangeRequestDto dto) {
        Optional<Auth> optionalAuth = authRepository.findById(dto.getAuthId());
        if (optionalAuth.isEmpty()) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        optionalAuth.get().setPassword(dto.getPassword());
        update(optionalAuth.get());
        return true;
    }

}
