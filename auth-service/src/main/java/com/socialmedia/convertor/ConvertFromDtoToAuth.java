package com.socialmedia.convertor;

import com.socialmedia.dto.request.AuthRegisterRequestDto;
import com.socialmedia.repository.entity.Auth;

public class ConvertFromDtoToAuth {

    public static Auth convertToAuth(AuthRegisterRequestDto dto){
        if(dto == null){
            throw new RuntimeException("Hata");
        }else{
            Auth auth = Auth.builder()
                    .username(dto.getUsername())
                    .password(dto.getPassword())
                    .email(dto.getEmail())
                    .build();
            return auth;
        }
    }
}
