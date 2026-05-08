package learn.slack_overflow.data;

import learn.slack_overflow.models.SlackUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SlackUserRepository {
    List<SlackUser> findAll();

    SlackUser findById(int userId);

    SlackUser add(SlackUser user);

    boolean update(SlackUser user);

    @Transactional
    boolean deleteById(int userId);
}
