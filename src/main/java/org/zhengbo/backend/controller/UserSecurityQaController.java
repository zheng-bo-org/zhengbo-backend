package org.zhengbo.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zhengbo.backend.global_exceptions.user.UserQueryException;
import org.zhengbo.backend.model.user.TypeOfUser;
import org.zhengbo.backend.security.TokenNotRequired;
import org.zhengbo.backend.service.user.TokenService;
import org.zhengbo.backend.service.user.UserGeneralService;
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
    private final UserGeneralService userGeneralService;

    record SupportedQaRes(List<String> codesOfTheQuestions) {

    }

    @GetMapping("/questions")
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

    public record UserQuestionFromQaReq(@NotBlank(message = "username is required") String username,
                                        @NotNull(message = "user type is required.") TypeOfUser type) {

    }

    public record UserQuestionFromQaRes(Long id, String codeOfTheQuestion) {

    }


    @TokenNotRequired
    @GetMapping("/mine/questions")
    public ResponseEntity<List<UserQuestionFromQaRes>> getTheQuestionFromTheQaOfTheUser(@RequestParam @NotBlank(message = "username is required") String username, @RequestParam @NotNull(message = "User type is required") TypeOfUser type) {
        var user = userGeneralService.findUserByUsername(type, username);
        if (user.isEmpty()) {
            throw new UserQueryException(UserQueryException.UserQueryExceptionCode.NO_SUCH_USER, HttpStatus.NOT_FOUND);
        }

        var qa = userSecurityQaService.getQaOfTheUser(user.get().getId());
        var questions = qa.stream().map(theQa -> new UserQuestionFromQaRes(theQa.getId(), theQa.getQuestion())).toList();
        return ResponseEntity.ok(questions);
    }
}
