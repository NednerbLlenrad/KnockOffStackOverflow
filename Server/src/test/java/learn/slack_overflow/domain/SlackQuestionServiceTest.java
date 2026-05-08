package learn.slack_overflow.domain;

import learn.slack_overflow.data.SlackQuestionRepository;
import learn.slack_overflow.data.SlackUserRepository;
import learn.slack_overflow.models.SlackQuestion;
import learn.slack_overflow.models.SlackUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SlackQuestionServiceTest {

    @Autowired
    SlackQuestionService service;

    @MockitoBean
    SlackQuestionRepository repository;

    @MockitoBean
    SlackUserRepository userRepository;

    @Test
    void shouldAddValidQuestion() {
        SlackQuestion question = makeQuestion();
        SlackQuestion saved = makeQuestion();
        saved.setSlackQuestionId(4);

        when(userRepository.findById(1)).thenReturn(makeUser());
        when(repository.add(question)).thenReturn(saved);

        Result<SlackQuestion> result = service.add(question);

        assertTrue(result.isSuccess());
        assertEquals(4, result.getPayload().getSlackQuestionId());
    }

    @Test
    void shouldNotAddWhenIdIsSet() {
        SlackQuestion question = makeQuestion();
        question.setSlackQuestionId(1);

        when(userRepository.findById(1)).thenReturn(makeUser());

        Result<SlackQuestion> result = service.add(question);

        assertEquals(ResultType.INVALID, result.getType());
        verify(repository, never()).add(any());
    }

    @Test
    void shouldNotAddInvalidQuestion() {
        SlackQuestion question = makeQuestion();
        question.setTitle("");

        when(userRepository.findById(1)).thenReturn(makeUser());

        Result<SlackQuestion> result = service.add(question);

        assertEquals(ResultType.INVALID, result.getType());
        verify(repository, never()).add(any());
    }

    @Test
    void shouldNotAddWhenUserIsMissing() {
        SlackQuestion question = makeQuestion();

        when(userRepository.findById(1)).thenReturn(null);

        Result<SlackQuestion> result = service.add(question);

        assertEquals(ResultType.INVALID, result.getType());
        verify(repository, never()).add(any());
    }

    @Test
    void shouldUpdateValidQuestion() {
        SlackQuestion question = makeQuestion();
        question.setSlackQuestionId(1);

        when(userRepository.findById(1)).thenReturn(makeUser());
        when(repository.update(question)).thenReturn(true);

        Result<SlackQuestion> result = service.update(question);

        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotUpdateMissingQuestion() {
        SlackQuestion question = makeQuestion();
        question.setSlackQuestionId(999);

        when(userRepository.findById(1)).thenReturn(makeUser());
        when(repository.update(question)).thenReturn(false);

        Result<SlackQuestion> result = service.update(question);

        assertEquals(ResultType.NOT_FOUND, result.getType());
    }

    @Test
    void shouldNotUpdateWithoutId() {
        SlackQuestion question = makeQuestion();

        when(userRepository.findById(1)).thenReturn(makeUser());

        Result<SlackQuestion> result = service.update(question);

        assertEquals(ResultType.INVALID, result.getType());
        verify(repository, never()).update(any());
    }

    private SlackQuestion makeQuestion() {
        SlackQuestion question = new SlackQuestion();
        question.setTitle("How do I look productive?");
        question.setBody("Need tips that require the least possible effort.");
        question.setSlackUserId(1);
        question.setChillPoints(5);
        return question;
    }

    private SlackUser makeUser() {
        SlackUser user = new SlackUser();
        user.setSlackUserId(1);
        user.setUsername("lazydev");
        user.setPasswordHash("$2a$10$abcdefghijklmnopqrstuv");
        user.setEmail("lazydev@example.com");
        user.setChillPoints(42);
        return user;
    }
}
