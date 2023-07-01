package org.zhengbo.backend.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/auth")
public class UserAuthController {

    @GetMapping("/sign-in")
    public ResponseEntity<String> signIn() {
        return ResponseEntity.ok("sdfsfdsafasdf");
    }
}
