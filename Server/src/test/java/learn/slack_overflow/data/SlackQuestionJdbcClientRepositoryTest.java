package learn.slack_overflow.data;

import learn.slack_overflow.models.SlackQuestion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SlackQuestionJdbcClientRepositoryTest {

    final static int NEXT_ID = 4;

    @Autowired
    SlackQuestionJdbcClientRepository repository;

    @Autowired
    JdbcClient jdbcClient;

    @BeforeEach
    void setup() {
        jdbcClient.sql("call set_known_good_state();").update();
    }

    @Test
    void shouldFindAll() {
        List<SlackQuestion> questions = repository.findAll();

        assertEquals(3, questions.size());
    }

    @Test
    void shouldFindByUserId() {
        List<SlackQuestion> questions = repository.findByUserId(1);

        assertEquals(2, questions.size());
        assertEquals(1, questions.get(0).getSlackUserId());
        assertEquals(1, questions.get(1).getSlackUserId());
    }

    @Test
    void shouldFindFirstQuestion() {
        SlackQuestion question = repository.findById(1);

        assertNotNull(question);
        assertEquals(1, question.getSlackQuestionId());
        assertEquals("How do I pretend to debug?", question.getTitle());
        assertEquals(1, question.getSlackUserId());
        assertEquals(21, question.getChillPoints());
    }

    @Test
    void shouldNotFindMissingQuestion() {
        SlackQuestion question = repository.findById(999);

        assertNull(question);
    }

    @Test
    void shouldAddQuestion() {
        SlackQuestion question = makeQuestion();
        SlackQuestion actual = repository.add(question);

        assertNotNull(actual);
        assertEquals(NEXT_ID, actual.getSlackQuestionId());

        SlackQuestion found = repository.findById(NEXT_ID);
        assertEquals("How do I look productive?", found.getTitle());
        assertEquals(2, found.getSlackUserId());
        assertEquals(5, found.getChillPoints());
    }

    @Test
    void shouldUpdateQuestion() {
        SlackQuestion question = repository.findById(2);
        question.setTitle("Best way to nap through sprint planning?");
        question.setBody("I need something more advanced than camera off.");
        question.setChillPoints(18);

        assertTrue(repository.update(question));

        SlackQuestion actual = repository.findById(2);
        assertEquals("Best way to nap through sprint planning?", actual.getTitle());
        assertEquals("I need something more advanced than camera off.", actual.getBody());
        assertEquals(18, actual.getChillPoints());
        assertNotNull(actual.getEditedAt());
    }

    @Test
    void shouldNotUpdateMissingQuestion() {
        SlackQuestion question = makeQuestion();
        question.setSlackQuestionId(999);

        assertFalse(repository.update(question));
    }

    @Test
    void shouldDeleteQuestion() {
        assertTrue(repository.deleteById(3));
        assertNull(repository.findById(3));
    }

    @Test
    void shouldNotDeleteMissingQuestion() {
        assertFalse(repository.deleteById(999));
    }

    private SlackQuestion makeQuestion() {
        SlackQuestion question = new SlackQuestion();
        question.setTitle("How do I look productive?");
        question.setBody("Need tips that require the least possible effort.");
        question.setSlackUserId(2);
        question.setChillPoints(5);
        return question;
    }
}
