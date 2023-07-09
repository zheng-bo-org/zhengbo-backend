package org.zhengbo.backend.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zhengbo.backend.service.user.TokenService;

@RestController
@RequestMapping("/users/me/tokens")
@RequiredArgsConstructor
@Tag(name = "Token api", description = "Api for token, like refresh, remove etc..")
public class UserTokenController {
    private final TokenService tokenService;

    record RefreshTokenRs(String token) {

    }
    @PutMapping()
    @Operation(summary = "refresh token")
    @ApiResponse(responseCode = "200", description = "new token")
    public ResponseEntity<?> refreshToken() {
        String token = tokenService.refreshTokenForCurrentUser();
        return ResponseEntity.ok(new RefreshTokenRs(token));
    }

    @DeleteMapping(value = "/current")
    @Operation(summary = "sign out current user")
    public ResponseEntity<GlobalEmptyResponse> signOut() {
        tokenService.makeCurrentTokenInvalid();
        return ResponseEntity.ok(new GlobalEmptyResponse());
    }
}
