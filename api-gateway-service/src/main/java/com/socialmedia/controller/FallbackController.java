package com.socialmedia.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {
    @GetMapping("/auth-service")
    public ResponseEntity<String> authServiceFallback(){
        return ResponseEntity.ok("Auth service su anda hizmet verememektedir. Sorii");
    }
    @GetMapping("/user-profile-service")
    public ResponseEntity<String> userProfileServiceFallback(){
        return ResponseEntity.ok("User-profile service su anda hizmet verememektedir. Sorii");
    }
    @GetMapping("/mail-service")
    public ResponseEntity<String> mailServiceFallback(){
        return ResponseEntity.ok("Mail service su anda hizmet verememektedir. Sorii");
    }
}
