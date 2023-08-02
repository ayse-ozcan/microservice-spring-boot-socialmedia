package com.socialmedia.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
    //Auth Service ERROR
    VALIDATION_ERROR(3000,"validasyon hatasi",HttpStatus.BAD_REQUEST),
    BAD_REQUEST(4000,"parametre hatasi", HttpStatus.BAD_REQUEST),
    LOGIN_ERROR(4001,"kullanici adi ya da parola hatali", HttpStatus.BAD_REQUEST),
    USERNAME_DUPLICATE(4002,"bu kullanici zaten kayitli" ,HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(4003,"boyle bir kullanici bulunamadi",HttpStatus.BAD_REQUEST),
    ACCOUNT_NOT_ACTIVE(4004,"hesabiniz aktif degil",HttpStatus.BAD_REQUEST),
    ACTIVATE_CODE_ERROR(4005,"aktivasyon kod hatasi",HttpStatus.BAD_REQUEST),
    ALREADY_ACTIVE(4006,"hesabiniz zaten aktif",HttpStatus.BAD_REQUEST),
    PASSWORDS_NOT_MATCH(4007,"parolalar uyusmuyor",HttpStatus.BAD_REQUEST),
    USER_ACCESS_ERROR(4008,"kullanici,yonetici tarafindan engellenmistir",HttpStatus.BAD_REQUEST),
    TOKEN_NOT_CREATED(4009,"token olusturulamadi", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(4010,"token hatasi",HttpStatus.BAD_REQUEST),
    INTERNAL_ERROR(5000,"sunucu hatasi",HttpStatus.INTERNAL_SERVER_ERROR);

    private int code;
    private String message;
    HttpStatus httpStatus;
}
