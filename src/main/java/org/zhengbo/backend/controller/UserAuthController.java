package org.zhengbo.backend.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zhengbo.backend.model.user.TypeOfUser;
import org.zhengbo.backend.service.user.UserAuthService;

@RestController
@RequestMapping("/users/auth")
@RequiredArgsConstructor
@Tag(name = "UserAuth", description = "User auth api.")
public class UserAuthController {
    private final Logger log = LoggerFactory.getLogger(UserAuthController.class);

    private final UserAuthService userAuthService;

    record SignInReq(
            @NotEmpty(message = "username should not be empty")
            String username,

            @NotEmpty(message = "password should not be empty.")
            String pwd,

            @NotNull(message = "type is required.")
            TypeOfUser type
    ) {}


    record SignInRes(String token) {

    }

    @PostMapping("/sign-in")
    @Operation(summary = "sign in api")
    public ResponseEntity<SignInRes> signIn(@RequestBody @Valid SignInReq sign) {
        String token = userAuthService.signIn(sign.username, sign.pwd, sign.type);
        return ResponseEntity.ok(new SignInRes(token));
    }

    record SignUpReq(
            @NotNull(message = "username is required") @Length(min = 4, message = "username length must >= 4")
            String username,
            @NotNull(message = "pwd is required") @Length(min = 6, message = "password length must >= 6")
            String pwd,
            @NotNull(message = "type is required.")
            TypeOfUser type
    ){}

    record SignUpRs(String token) {};

    @PostMapping("/sign-up")
    @Operation(summary = "sign up api")
    public ResponseEntity<SignUpRs> signUp(@RequestBody @Valid SignUpReq req) {
        String token = userAuthService.signUp(req.username, req.pwd, req.type);
        return ResponseEntity.ok(new SignUpRs(token));
    }
}