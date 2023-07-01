package org.zhengbo.backend.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zhengbo.backend.model.user.TypeOfUser;
import org.zhengbo.backend.service.user.TokenService;

@RestController
@RequestMapping("/users/auth")
@RequiredArgsConstructor
public class UserAuthController {
    private final TokenService tokenService;
    private final TokenService.UserRolesFinder userRolesFinder;

    @GetMapping("/sign-in")
    public ResponseEntity<String> signIn() {
        return ResponseEntity.ok("test");
    }
}
