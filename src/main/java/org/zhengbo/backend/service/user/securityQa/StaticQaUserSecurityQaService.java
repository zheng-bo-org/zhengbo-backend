package org.zhengbo.backend.service.user.securityQa;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.zhengbo.backend.global_exceptions.user.UserSecurityQaException;
import org.zhengbo.backend.model.user.UserSecurityQa;
import org.zhengbo.backend.repository.UserSecurityQaRepository;
import org.zhengbo.backend.service.user.UserGeneralService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaticQaUserSecurityQaService implements UserSecurityQaService {
    private final Logger log = LoggerFactory.getLogger(StaticQaUserSecurityQaService.class);
    private final UserSecurityQaRepository userSecurityQaRepository;
    private final UserGeneralService userGeneralService;

    private final List<String> questionsOfTheCode = new ArrayList<>() {{
        add("THE_NAME_OF_THE_PERSON_THAT_YOU_LOVED_MOST");
        add("THE_NAME_OF_THE_PERSON_THAT_MAKES_YOU_WANNA_CRY");
        add("THE_NAME_OF_THE_CITY_THAT_YOU_WILL_NEVER_FORGET");
        add("THE_NAME_OF_YOUR_BEST_FRIEND(DONT_FEEL_SAD_IF_YOU_DONT_HAVE_ONE, I_DONT_HAVE_ONE_TOO)");
        add("THE_NAME_OF_THE_THING_THAT_MAKES_YOUR_FELL_CALM");
        add("THE_NAME_OF_THE_THING_OR_PERSON_THAT_MAKES_YOU_FELL_WARMED_WHEN_YOU_IN_A_DOWN_MOOD");
    }};

    @Override
    public List<String> getTheCodesOfTheQuestionsWhichIsSupported() {
        return questionsOfTheCode;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void overwriteTheQaOfTheUser(Long userId, Map<String, String> qa) {
        var user = userGeneralService.findUserById(userId);
        if (qa.size()!= 3) {
            throw new UserSecurityQaException(UserSecurityQaException.UserSecurityQaExceptionCode.BAD_QA_LENGTH, HttpStatus.BAD_REQUEST);
        }
        var qaList = qa.keySet().stream().map(question -> {
            if (!questionsOfTheCode.contains(question)) {
                throw new UserSecurityQaException(UserSecurityQaException.UserSecurityQaExceptionCode.NOT_SUPPORTED_QUESTION, HttpStatus.NOT_ACCEPTABLE);
            }
            String answer = qa.get(question);
            if (answer == null) {
                throw new UserSecurityQaException(UserSecurityQaException.UserSecurityQaExceptionCode.INVALID_ANSWER, HttpStatus.BAD_REQUEST);
            }

            org.zhengbo.backend.model.user.UserSecurityQa questionAndAnswer = new org.zhengbo.backend.model.user.UserSecurityQa();
            questionAndAnswer.setQuestion(question);
            questionAndAnswer.setAnswer(answer);
            questionAndAnswer.setUser(user);

            return questionAndAnswer;
        }).toList();

        userSecurityQaRepository.deleteAllByUser(user);
        userSecurityQaRepository.saveAll(qaList);
    }

    @Override
    public Map<String, String> getQuestionAndAnswerOfTheUser(Long userId) {
        var user = userGeneralService.findUserById(userId);
        var qaList = userSecurityQaRepository.findAllByUser(user);
        return qaList.stream().collect(Collectors.toMap(UserSecurityQa::getQuestion, UserSecurityQa::getAnswer));
    }
}
