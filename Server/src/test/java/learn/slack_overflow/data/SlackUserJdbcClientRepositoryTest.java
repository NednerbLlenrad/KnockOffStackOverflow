package learn.slack_overflow.data;

import learn.slack_overflow.models.SlackUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SlackUserJdbcClientRepositoryTest {

    @Autowired
    SlackUserJdbcClientRepository repository;

    @Autowired
    JdbcClient jdbcClient;

    @BeforeEach
    void setup() {
        jdbcClient.sql("call set_known_good_state();").update();
    }

    @Test
    void shouldFindAll() {
        List<SlackUser> users = repository.findAll();

        assertEquals(3, users.size());
    }

    @Test
    void shouldFindLazyDev() {
        SlackUser user = repository.findById(1);

        assertNotNull(user);
        assertEquals(1, user.getSlackUserId());
        assertEquals("lazydev", user.getUsername());
        assertEquals("lazydev@example.com", user.getEmail());
        assertEquals(42, user.getChillPoints());
    }

    @Test
    void shouldNotFindMissingUser() {
        SlackUser user = repository.findById(999);

        assertNull(user);
    }
}