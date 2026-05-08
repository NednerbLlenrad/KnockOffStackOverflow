package learn.slack_overflow.domain;

import learn.slack_overflow.data.SlackUserRepository;
import learn.slack_overflow.models.SlackUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SlackUserServiceTest {

    @Autowired
    SlackUserService service;

    @MockitoBean
    SlackUserRepository repository;

    @Test
    void shouldAddValidUser() {
        SlackUser user = makeUser();
        SlackUser saved = makeUser();
        saved.setSlackUserId(4);

        when(repository.add(user)).thenReturn(saved);

        Result<SlackUser> result = service.add(user);

        assertTrue(result.isSuccess());
        assertEquals(4, result.getPayload().getSlackUserId());
    }

    @Test
    void shouldNotAddWhenIdIsSet() {
        SlackUser user = makeUser();
        user.setSlackUserId(1);

        Result<SlackUser> result = service.add(user);

        assertEquals(ResultType.INVALID, result.getType());
        verify(repository, never()).add(any());
    }

    @Test
    void shouldNotAddInvalidUser() {
        SlackUser user = makeUser();
        user.setUsername("");

        Result<SlackUser> result = service.add(user);

        assertEquals(ResultType.INVALID, result.getType());
        verify(repository, never()).add(any());
    }

    @Test
    void shouldNotAddDuplicateUsernameOrEmail() {
        SlackUser user = makeUser();
        when(repository.add(user)).thenThrow(DuplicateKeyException.class);

        Result<SlackUser> result = service.add(user);

        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldUpdateValidUser() {
        SlackUser user = makeUser();
        user.setSlackUserId(1);
        when(repository.update(user)).thenReturn(true);

        Result<SlackUser> result = service.update(user);

        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotUpdateMissingUser() {
        SlackUser user = makeUser();
        user.setSlackUserId(999);
        when(repository.update(user)).thenReturn(false);

        Result<SlackUser> result = service.update(user);

        assertEquals(ResultType.NOT_FOUND, result.getType());
    }

    @Test
    void shouldNotUpdateWithoutId() {
        SlackUser user = makeUser();

        Result<SlackUser> result = service.update(user);

        assertEquals(ResultType.INVALID, result.getType());
        verify(repository, never()).update(any());
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
