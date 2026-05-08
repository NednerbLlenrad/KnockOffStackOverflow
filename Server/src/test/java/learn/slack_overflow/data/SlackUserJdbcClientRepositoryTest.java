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

    final static int NEXT_ID = 4;

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

    @Test
    void shouldAddUser() {
        SlackUser user = makeUser();
        SlackUser actual = repository.add(user);

        assertNotNull(actual);
        assertEquals(NEXT_ID, actual.getSlackUserId());

        SlackUser found = repository.findById(NEXT_ID);
        assertEquals("debugsloth", found.getUsername());
        assertEquals("debugsloth@example.com", found.getEmail());
        assertEquals(7, found.getChillPoints());
    }

    @Test
    void shouldUpdateUser() {
        SlackUser user = repository.findById(2);
        user.setUsername("bugnapper");
        user.setEmail("bugnapper@example.com");
        user.setChillPoints(25);

        assertTrue(repository.update(user));

        SlackUser actual = repository.findById(2);
        assertEquals("bugnapper", actual.getUsername());
        assertEquals("bugnapper@example.com", actual.getEmail());
        assertEquals(25, actual.getChillPoints());
        assertNotNull(actual.getEditedAt());
    }

    @Test
    void shouldNotUpdateMissingUser() {
        SlackUser user = makeUser();
        user.setSlackUserId(999);

        assertFalse(repository.update(user));
    }

    @Test
    void shouldDeleteUser() {
        assertTrue(repository.deleteById(3));
        assertNull(repository.findById(3));
    }

    @Test
    void shouldNotDeleteMissingUser() {
        assertFalse(repository.deleteById(999));
    }

    private SlackUser makeUser() {
        SlackUser user = new SlackUser();
        user.setUsername("debugsloth");
        user.setPasswordHash("$2a$10$debugslothfakehashvalue");
        user.setEmail("debugsloth@example.com");
        user.setChillPoints(7);
        return user;
    }
}
