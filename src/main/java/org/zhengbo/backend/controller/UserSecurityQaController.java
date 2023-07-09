package org.zhengbo.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zhengbo.backend.service.user.TokenService;
import org.zhengbo.backend.service.user.securityQa.UserSecurityQaService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users/security-qa")
@RequiredArgsConstructor
@Tag(name = "User security Qa", description = "Api for user security qa.")
public class UserSecurityQaController {
    private final UserSecurityQaService userSecurityQaService;
    private final TokenService tokenService;

    record SupportedQaRes(List<String> codesOfTheQuestions) {

    }
    @GetMapping("/questions/mine")
    @Operation(summary = "Get the questions that available for the user to answer")
    public ResponseEntity<SupportedQaRes> getSupportedQuestionListForCurrentUser() {
        var codes = userSecurityQaService.getTheCodesOfTheQuestionsWhichIsSupported();
        return ResponseEntity.ok(new SupportedQaRes(codes));
    }


    record UserQaRes(Map<String, String> questionAndAnswerMappings) {

    }

    @GetMapping("/mine")
    @Operation(summary = "Get the question and answer that user already uploaded.")
    public ResponseEntity<UserQaRes> getCurrentUserQa() {
        Long userId = tokenService.getCurrentUser();
        var qa = userSecurityQaService.getQuestionAndAnswerOfTheUser(userId);
        return ResponseEntity.ok(new UserQaRes(qa));
    }
}
