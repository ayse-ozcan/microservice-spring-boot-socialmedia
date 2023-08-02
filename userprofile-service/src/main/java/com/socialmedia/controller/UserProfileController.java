package com.socialmedia.controller;

import com.socialmedia.dto.request.PasswordChangeRequestDto;
import com.socialmedia.dto.request.UserProfileSaveRequestDto;
import com.socialmedia.dto.request.UserProfileUpdateRequestDto;
import com.socialmedia.dto.request.UserSetPasswordRequestDto;
import com.socialmedia.rabbitmq.model.UserForgotPasswordModel;
import com.socialmedia.repository.entity.UserProfile;
import com.socialmedia.service.UserProfileService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static com.socialmedia.constant.ApiUrls.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(USER)
public class UserProfileController {
    private final UserProfileService userProfileService;

    @Hidden
    @PostMapping(CREATE_USER)
    public ResponseEntity<Boolean> createUser(@RequestBody UserProfileSaveRequestDto dto) {
        return ResponseEntity.ok(userProfileService.createUser(dto));
    }
    @GetMapping(FIND_ALL)
    public ResponseEntity<List<UserProfile>> findAll(){
        return ResponseEntity.ok(userProfileService.findAll());
    }

    @PutMapping(UPDATE)
    @Operation(summary = "kullanici giris yaptiktan sonra eksik bilgilerini doldurur.")
    public ResponseEntity<Boolean> updateUser(String token,@RequestBody UserProfileUpdateRequestDto dto) {
        return ResponseEntity.ok(userProfileService.updateUser(token,dto));
    }
    @Hidden
    @DeleteMapping("/delete/{authId}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long authId) {
        return ResponseEntity.ok(userProfileService.deleteUser(authId));
    }
    @Hidden
    @PutMapping("/activate-status/{authId}")
    public ResponseEntity<Boolean> activateStatus(@PathVariable Long authId) {
        return ResponseEntity.ok(userProfileService.activateStatus(authId));
    }
    @Hidden
    @PutMapping(FORGOT_PASSWORD)
    public ResponseEntity<Boolean> forgotPassword(@RequestBody UserSetPasswordRequestDto dto) {
        return ResponseEntity.ok(userProfileService.forgotPassword(dto));
    }
    @PutMapping(CHANGE_PASSWORD)
    public ResponseEntity<Boolean> passwordChange(@RequestBody PasswordChangeRequestDto dto){
        return ResponseEntity.ok(userProfileService.passwordChange(dto));
    }
}
