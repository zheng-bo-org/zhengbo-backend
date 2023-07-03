package org.zhengbo.backend.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zhengbo.backend.service.user.TokenService;

@RestController
@RequestMapping("/users/me/tokens")
@RequiredArgsConstructor
public class UserTokenController {
    private final TokenService tokenService;

    record RefreshTokenRs(String token) {

    }
    @PutMapping()
    public ResponseEntity<?> refreshToken() {
        String token = tokenService.refreshTokenForCurrentUser();
        return ResponseEntity.ok(new RefreshTokenRs(token));
    }
}
