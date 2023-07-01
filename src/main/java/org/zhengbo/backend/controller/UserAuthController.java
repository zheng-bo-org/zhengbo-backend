package org.zhengbo.backend.controller;


import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zhengbo.backend.global_exceptions.UserAuthException;
import org.zhengbo.backend.model.user.TypeOfUser;
import org.zhengbo.backend.service.user.TokenService;
import org.zhengbo.backend.service.user.UserGeneralService;

@RestController
@RequestMapping("/users/auth")
@RequiredArgsConstructor
public class UserAuthController {
    private final TokenService tokenService;
    private final TokenService.UserRolesFinder userRolesFinder;
    private final UserGeneralService userGeneralService;

    record SignInReq(@NotNull String username, @NotNull TypeOfUser type) {

    }

    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn(@RequestBody SignInReq sign) {
        throw new UserAuthException(UserAuthException.UserAuthExceptionCode.USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
    }
}