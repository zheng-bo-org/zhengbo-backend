package org.zhengbo.backend.service.user.securityQa;

import java.util.List;
import java.util.Map;

public interface UserSecurityQaService {
    List<String> getTheCodesOfTheQuestionsWhichIsSupported();

    void overwriteTheQaOfTheUser(Long userId, Map<String, String> qa);

    Map<String, String> getQuestionAndAnswerOfTheUser(Long userId);
}
