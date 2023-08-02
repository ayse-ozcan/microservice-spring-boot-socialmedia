package com.socialmedia.controller;

import com.socialmedia.dto.request.*;
import com.socialmedia.dto.response.AuthRegisterResponseDto;
import com.socialmedia.repository.entity.Auth;
import com.socialmedia.service.AuthService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.socialmedia.constant.ApiUrls.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(AUTH)
public class AuthController {
    private final AuthService authService;

    @PostMapping(REGISTER)
    public ResponseEntity<AuthRegisterResponseDto> register(@RequestBody @Valid AuthRegisterRequestDto dto) {
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping(LOGIN)
    public ResponseEntity<String> login(@RequestBody @Valid AuthLoginRequestDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping(ACTIVATE_STATUS)
    public ResponseEntity<Boolean> activateStatus(@RequestBody ActivateRequestDto dto) {
        return ResponseEntity.ok(authService.activateStatus(dto));
    }

    @Hidden
    @PutMapping(UPDATE)
    public ResponseEntity<Boolean> updateAuth(@RequestBody AuthUpdateRequestDto dto) {
        return ResponseEntity.ok(authService.updateAuth(dto));
    }

    @GetMapping(FIND_BY_ID)
    //http://localhost:9090/auth/find-by-id?id=2
    public ResponseEntity<Auth> findByIdParam(Long id) {
        return ResponseEntity.ok(authService.findById(id).get());
    }

    @GetMapping(FIND_BY_ID + "/{id}")
    //http://localhost:9090/auth/find-by-id/2
    public ResponseEntity<Auth> findByIdPathVariable(@PathVariable Long id) {
        return ResponseEntity.ok(authService.findById(id).get());
    }

    @GetMapping(FIND_ALL)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Auth>> findAll() {
        return ResponseEntity.ok(authService.findAll());
    }

    @DeleteMapping(DELETE + "/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Boolean> deleteUser(@PathVariable String token) {
        return ResponseEntity.ok(authService.deleteUser(token));
    }

    @PutMapping("/forgot-password")
    public ResponseEntity<Boolean> forgotPassword(@RequestBody @Valid ForgotPasswordRequestDto dto) {
        return ResponseEntity.ok(authService.forgotPassword(dto));
    }

    @PutMapping("/forgot-password-rabbitmq")
    public ResponseEntity<Boolean> forgotPasswordRabbitMq(@RequestBody @Valid ForgotPasswordRequestDto dto) {
        return ResponseEntity.ok(authService.forgotPasswordRabbitMq(dto));
    }

    @PostMapping(REGISTER + "-with-rabbitmq")
    public ResponseEntity<AuthRegisterResponseDto> registerWithRabbitMq(@RequestBody @Valid AuthRegisterRequestDto dto) {
        return ResponseEntity.ok(authService.registerWithRabbitMq(dto));
    }

    @Hidden
    @PutMapping("/change-password")
    public ResponseEntity<Boolean> passwordChange(@RequestBody PasswordChangeRequestDto dto) {
        return ResponseEntity.ok(authService.passwordChange(dto));
    }
}

