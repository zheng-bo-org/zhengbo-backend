package org.zhengbo.backend.service.user.securityQa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.zhengbo.backend.global_exceptions.user.UserSecurityQaException;
import org.zhengbo.backend.model.user.User;
import org.zhengbo.backend.model.user.UserSecurityQa;
import org.zhengbo.backend.repository.UserSecurityQaRepository;
import org.zhengbo.backend.service.user.UserGeneralService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class StaticQaUserSecurityQaTest {
    @Mock
    private UserSecurityQaRepository userSecurityQaRepository;

    @Mock
    private UserGeneralService userGeneralService;

    private UserSecurityQaService securityQa;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize the mocks
        securityQa = new StaticQaUserSecurityQaService(userSecurityQaRepository, userGeneralService);
    }

    @Test
    void should_throw_error_on_invalid_question_overwriteTheQaOfTheUser() {
        Map<String, String> qa = new HashMap<>() {{
            put("test", "sadfsf");
            put("sadf", "asdfa");
            put("sadf23", "sdfa");
        }};
        when(userGeneralService.findUserById(anyLong())).thenReturn(new User());
        UserSecurityQaException exception = null;
        try {
            securityQa.overwriteTheQaOfTheUser(1L, qa);
        } catch (UserSecurityQaException ex) {
            exception = ex;
        }

        UserSecurityQaException finalException = exception;
        assert finalException != null;
        assert finalException.getCode().equals("NOT_SUPPORTED_QUESTION");

        verify(userGeneralService, times(1)).findUserById(any());
    }

    @Test
    void should_throw_exception_on_invalid_answer_overwriteTheQaOfTheUser() {
        Map<String, String> qa = new HashMap<>() {{
            put("THE_NAME_OF_THE_PERSON_THAT_YOU_LOVED_MOST", null);
            put("THE_NAME_OF_THE_PERSON_THAT_MAKES_YOU_WANNA_CRY", null);
            put("THE_NAME_OF_THE_THING_OR_PERSON_THAT_MAKES_YOU_FELL_WARMED_WHEN_YOU_IN_A_DOWN_MOOD", null);
        }};
        when(userGeneralService.findUserById(anyLong())).thenReturn(new User());
        UserSecurityQaException exception = null;
        try {
            securityQa.overwriteTheQaOfTheUser(1L, qa);
        } catch (UserSecurityQaException ex) {
            exception = ex;
        }

        assert exception != null;
        assert exception.getCode().equals("INVALID_ANSWER");

        verify(userGeneralService, times(1)).findUserById(any());
    }

    @Test
    void should_able_to_overwrite_the_qa_with_valid_qa_overwriteTheQaOfTheUser() {
        Map<String, String> qa = new HashMap<>() {{
            put("THE_NAME_OF_THE_PERSON_THAT_YOU_LOVED_MOST", "Humble");
            put("THE_NAME_OF_THE_PERSON_THAT_MAKES_YOU_WANNA_CRY", "Humble");
            put("THE_NAME_OF_THE_THING_OR_PERSON_THAT_MAKES_YOU_FELL_WARMED_WHEN_YOU_IN_A_DOWN_MOOD", "Humble");
        }};

        when(userGeneralService.findUserById(anyLong())).thenReturn(new User());
        doNothing().when(userSecurityQaRepository).deleteAllByUser(any());
        when(userSecurityQaRepository.saveAll(any())).thenReturn(anyList());
        UserSecurityQaException exception = null;
        try {
            securityQa.overwriteTheQaOfTheUser(1L, qa);
        } catch (UserSecurityQaException ex) {
            exception = ex;
        }

        assert exception == null;

        verify(userGeneralService, times(1)).findUserById(any());
        verify(userSecurityQaRepository, times(1)).deleteAllByUser(any());
    }

    @Test
    void should_throw_exception_if_the_qa_size_is_not_3_overwriteTheQaOfTheUser() {
        Map<String, String> qa = new HashMap<>() {{
            put("THE_NAME_OF_THE_PERSON_THAT_YOU_LOVED_MOST", "Humble");
        }};

        when(userGeneralService.findUserById(anyLong())).thenReturn(new User());
        UserSecurityQaException exception = null;
        try {
            securityQa.overwriteTheQaOfTheUser(1L, qa);
        } catch (UserSecurityQaException ex) {
            exception = ex;
        }

        assert exception != null;
        assert exception.getCode().equals("BAD_QA_LENGTH");
    }

    @Test
    public void getQuestionAndAnswerOfTheUser_should_work() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        UserSecurityQa qa1 = new UserSecurityQa();
        qa1.setQuestion("Question 1");
        qa1.setAnswer("Answer 1");
        qa1.setUser(user);

        UserSecurityQa qa2 = new UserSecurityQa();
        qa2.setQuestion("Question 2");
        qa2.setAnswer("Answer 2");
        qa2.setUser(user);

        List<UserSecurityQa> qaList = Arrays.asList(qa1, qa2);

        when(userGeneralService.findUserById(userId)).thenReturn(user);
        when(userSecurityQaRepository.findAllByUser(user)).thenReturn(qaList);

        Map<String, String> result = securityQa.getQuestionAndAnswerOfTheUser(userId);

        assertEquals(2, result.size());
        assertEquals("Answer 1", result.get("Question 1"));
        assertEquals("Answer 2", result.get("Question 2"));
    }
}