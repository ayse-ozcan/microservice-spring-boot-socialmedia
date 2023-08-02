package com.socialmedia.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterModel implements Serializable {
    private Long authId;
    private String name;
    private String surname;
    private String username;
    private String email;
    private String password;
}
