package com.socialmedia.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRegisterRequestDto {
    @NotEmpty(message = "kullanici adini bos birakmayiniz")
    @Size(min = 3,max = 20,message = "kullanici adi en az 3 en fazla 20 karakter olabilir")
    private String username;
    @NotEmpty(message = "name alanini bos birakmayiniz")
    @Size(min = 3,max = 20,message = "name en az 2 en fazla 20 karakter olabilir")
    private String name;
    @NotEmpty(message = "surname alanini bos birakmayiniz")
    @Size(min = 3,max = 20,message = "surname en az 2 en fazla 20 karakter olabilir")
    private String surname;
    @Email(message = "lutfen gecerli bir email giriniz")
    private String email;
    @NotEmpty
    @Pattern(message = "parola en az 8 karakter olmali, buyuk kucuk harf,rakam ve ozel karakter icermelidir.",
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=*!])(?=\\S+$).{8,32}$")
    private String password;
    private String rePassword;
}
