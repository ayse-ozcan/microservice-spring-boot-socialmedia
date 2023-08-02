package com.socialmedia.manager;

import com.socialmedia.dto.request.ForgotPasswordRequestDto;
import com.socialmedia.dto.request.UserProfileSaveRequestDto;
import com.socialmedia.dto.request.UserSetPasswordRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(url = "${openfeign.user-manager-url}", name = "auth-userprofile")
public interface IUserProfileManager {
    //Buradaki metodun, user-profile daki metod donus tipiyle ayni olmasi gerekmektedir.
    //ancak metod ismi farkli olabilir. Ama ayni olmasi okunabilirlik acisindan daha iyidir.
    //metodun parametresinde bulunan veri gonderme tipi(@RequestBody,@RequestParam vb.) birebir ayni olmalidir.
    //metodun dto parametresinin ismiyle userprofile cotroller metodundaki parametre isminin ayni
    //olmasi okunabilirlik acisindan faydali olacaktir.
    //dto larin icerisindeki property ler de ayni olmali.

    @PostMapping("/create-user") //bu end-point ile userprofile controller da calisan end point in url i ayni
    //http://localhost:9091/user-profile
    public ResponseEntity<Boolean> createUser(@RequestBody UserProfileSaveRequestDto dto);

    @DeleteMapping("/delete/{authId}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long authId);

    @PutMapping("/activate-status/{authId}")
    public ResponseEntity<Boolean> activateStatus(@PathVariable Long authId);

    @PutMapping("/forgot-password")
    public ResponseEntity<Boolean> forgotPassword(@RequestBody UserSetPasswordRequestDto dto);
}